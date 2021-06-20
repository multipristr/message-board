import * as React from "react"
import {useRef, useState} from "react"
import {SERVER_URL, STORAGE_KEY_USER} from "../config";
import {getAuthorization} from "./Authorization";

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

const headStyle = {
    gridArea: "head",
    alignSelf: "start",
    justifySelf: "start",
    maxWidth: "100%",
    minWidth: "0",
    fontFamily: "monospace",
}

const buttonsStyle = {
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
    maxWidth: "100%",
    minWidth: "0",
}

const repliesStyle = {
    gridArea: "replies",
    alignSelf: "start",
    justifySelf: "start",
    maxWidth: "100%",
    minWidth: "0",
}

const replyStyle = {
    gridArea: "reply",
    alignSelf: "end",
    justifySelf: "end",
    maxWidth: "100%",
    minWidth: "0",
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
    const contentRef = useRef(null)
    const [modifying, setModifying] = useState(false)
    const [modifiedTimestamp, setModifiedTimeStamp] = useState(lastModifiedAt)

    return (
        <div style={messageStyle}>
            <div style={headStyle}>
                {author}&nbsp;Created: <time>{new Date(createdAt).toLocaleString()}</time>&nbsp;
                {modifiedTimestamp !== createdAt && <>Modified: <time>{new Date(modifiedTimestamp).toLocaleString()}</time>&nbsp;</>}
            </div>
            {window.localStorage.getItem(STORAGE_KEY_USER) === author &&
            <div style={buttonsStyle}>
                <button onClick={() => {
                    if (modifying) {
                        modifyMessage(id, contentRef.current.innerHTML)
                            .then(response => response.json())
                            .then(timestamp => setModifiedTimeStamp(timestamp))
                            .then(() => setModifying(false))
                    } else {
                        setModifying(true)
                        contentRef.current.focus()
                    }
                }}>
                    {modifying ? "Save changes" : "Modify"}
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
            <p style={contentStyle} ref={contentRef} contentEditable={modifying}>{content}</p>
            <button style={repliesStyle} onClick={() => operateReplies(id)}>{show ? "Show" : "Hide"} replies</button>
            <button style={replyStyle} onClick={() => addReply()}>Reply</button>
        </div>
    )
}

export default Message
