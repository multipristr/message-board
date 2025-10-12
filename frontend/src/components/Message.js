import * as React from "react"
import {useEffect, useRef, useState} from "react"
import {SERVER_URL} from "../config";
import {getAuthorization, getUser} from "./Authorization";
import MessageHead from "./MessageHead";
import MessageReply from "./MessageReply";
import ContentEditable from "react-contenteditable";

const messageStyle = {
    display: "grid",
    grid: `
                    "head . buttons" 1fr
                    "content content content" auto
                    "replies . reply" 1fr             
                / 2fr  auto  1fr 
                `,
    height: "100%",
    backgroundColor: "black",
    color: "white",
    marginTop: "0.2%",
    padding: "0.5%",
}

const modifiersStyle = {
    gridArea: "buttons",
    alignSelf: "start",
    justifySelf: "end",
    maxWidth: "100%",
    minWidth: "0",
}

const contentStyle = {
    gridArea: "content",
    alignSelf: "start",
    justifySelf: "start",
    width: "100%",
}

const deleteMessage = (id) => fetch(`${SERVER_URL}/messages/${id}`, {
    method: 'DELETE',
    credentials: 'include',
    headers: {
        "Authorization": getAuthorization(),
    },
})

const modifyMessage = (id, content) => fetch(`${SERVER_URL}/messages/${id}`, {
    method: 'PATCH',
    credentials: 'include',
    headers: {
        "Content-Type": "application/json",
        "Accept": "application/json",
        "Authorization": getAuthorization(),
    },
    body: JSON.stringify({content: content})
})

const Message = ({id, author, createdAt, lastModifiedAt, content, show, replying, operateReplies, addReply, deleteHierarchy}) => {
    const contentRef = useRef(content)
    const [originalContent, setOriginalContent] = useState(content)
    const areaRef = useRef(null)
    const [isModifying, setModifying] = useState(false)
    const [modifiedTimestamp, setModifiedTimeStamp] = useState(lastModifiedAt)

    useEffect(() => {
        if (isModifying) {
            areaRef.current.focus();
        }
    }, [isModifying]);

    return (
        <section style={messageStyle}>
            <MessageHead author={author} createdAt={createdAt} modifiedTimestamp={modifiedTimestamp}/>
            {getUser() === author &&
            <div style={modifiersStyle}>
                <button onClick={() => {
                    if (isModifying) {
                        if (contentRef.current === originalContent) {
                            setModifying(false)
                        } else {
                            modifyMessage(id, contentRef.current)
                                .then(response => response.json())
                                .then(message => {
                                    setModifiedTimeStamp(message.lastModifiedAt)
                                    setOriginalContent(message.content)
                                })
                                .then(() => setModifying(false))
                        }
                    } else {
                        setModifying(true)
                    }
                }}>
                    {isModifying ? "Save changes" : "Modify"}
                </button>
                <button onClick={() => deleteMessage(id)
                    .then(response => {
                            if (response.status > 199 && response.status < 300) {
                                deleteHierarchy()
                            }
                        }
                    )
                }>
                    Delete
                </button>
            </div>
            }
            <ContentEditable style={contentStyle} html={contentRef.current} tagName="p" disabled={!isModifying}
                             innerRef={areaRef}
                             onChange={event => contentRef.current = event.target.value}/>
            <MessageReply onClickReplies={() => operateReplies(id)} show={show} replying={replying}
                          onClickReply={addReply}/>
        </section>
    )
}

export default Message
