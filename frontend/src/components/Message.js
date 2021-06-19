import * as React from "react"
import {useRef, useState} from "react"
import {SERVER_URL} from "../config";

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
    margin: "0.2%",
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
})

const modifyMessage = (id, content) => fetch(`${SERVER_URL}/message/${id}`, {
    method: 'PUT',
    credentials: 'include',
    headers: {
        contentType: "application/json",
    },
    body: JSON.stringify(content),
})

const Message = ({id, author, createdAt, lastModifiedAt, content, show, operateReplies, addReply}) => {
    const contentRef = useRef(null)
    const [modifying, setModifying] = useState(false)

    return (
        <div style={messageStyle}>
            <div style={headStyle}>
                {author}&nbsp;Created: <time>{new Date(createdAt).toLocaleString()}</time>&nbsp;
                {lastModifiedAt !== createdAt && <>Modified: <time>{new Date(lastModifiedAt).toLocaleString()}</time>&nbsp;</>}
            </div>
            <div style={buttonsStyle}>
                <button onClick={() => {
                    setModifying(!modifying)
                    if (modifying) {
                        modifyMessage(id, contentRef.current.value)
                            .then(() => "")// TODO FIXME Reload message list
                    } else {
                        contentRef.current.focus()
                    }
                }}>
                    {modifying ? "Save changes" : "Modify"}
                </button>
                <button onClick={() => deleteMessage(id)
                    .then(() => "") // TODO FIXME Reload message list
                }>
                    Delete
                </button>
            </div>
            <p style={contentStyle} ref={contentRef} contentEditable={modifying}>{content}</p>
            <button style={repliesStyle} onClick={() => operateReplies(id)}>{show ? "Show" : "Hide"} replies</button>
            <button style={replyStyle} onClick={() => addReply()}>Reply</button>
        </div>
    )
}

export default Message
