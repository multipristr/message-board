import * as React from "react"
import {useRef, useState} from "react"
import {SERVER_URL} from "../config";
import {getAuthorization, getUser} from "./Authorization";
import MessageHead from "./MessageHead";
import MessageReply from "./MessageReply";
import ContentEditable from "react-contenteditable";

const messageStyle = {
    display: "grid",
    grid: `
                    "head . buttons" auto
                    "content content content" auto
                    "replies . reply" auto             
                / auto auto
                `,
    height: "100%",
    backgroundColor: "black",
    color: "white",
    marginTop: "0.2%",
    padding: "0.2%",
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
    height: "100%",
}

const deleteMessage = (id) => fetch(`${SERVER_URL}/message/${id}`, {
    method: 'DELETE',
    credentials: 'include',
    headers: {
        "Authorization": getAuthorization(),
    },
})

const modifyMessage = (id, content) => fetch(`${SERVER_URL}/message/${id}`, {
    method: 'PUT',
    credentials: 'include',
    headers: {
        "Content-Type": "application/json",
        "Authorization": getAuthorization(),
    },
    body: content,
})

const Message = ({id, author, createdAt, lastModifiedAt, content, show, operateReplies, addReply, deleteHierarchy}) => {
    const contentRef = useRef(content)
    const [isModifying, setModifying] = useState(false)
    const [modifiedTimestamp, setModifiedTimeStamp] = useState(lastModifiedAt)

    return (
        <section style={messageStyle}>
            <MessageHead author={author} createdAt={createdAt} modifiedTimestamp={modifiedTimestamp}/>
            {getUser() === author &&
            <div style={modifiersStyle}>
                <button onClick={() => {
                    if (isModifying) {
                        modifyMessage(id, contentRef.current)
                            .then(response => response.json())
                            .then(timestamp => setModifiedTimeStamp(timestamp))
                            .then(() => setModifying(false))
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
                             onChange={event => contentRef.current = event.target.value}/>
            <MessageReply onClickReplies={() => operateReplies(id)} show={show} onClickReply={addReply}/>
        </section>
    )
}

export default Message
