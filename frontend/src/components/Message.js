import * as React from "react"
import {SERVER_URL} from "../config";

const messageStyle = {
    backgroundColor: "black",
    color: "white",
    margin: "0.2%",
    padding: "0.2%",
}

const headStyle = {
    fontFamily: "monospace",
}

const deleteMessage = (id) => fetch(`${SERVER_URL}/message/${id}`, {
    method: 'DELETE',
    credentials: 'include',
})

const modifyMessage = (id, content) => fetch(`${SERVER_URL}/message${id}`, {
    method: 'PUT',
    credentials: 'include',
    headers: {
        contentType: "application/json",
    },
    body: JSON.stringify(content),
})

const Message = ({id, author, createdAt, lastModifiedAt, content, addReplies}) => {
    return (
        <div style={messageStyle}>
            <div>
                <div style={headStyle}>
                    {author}&nbsp;Created: <time>{new Date(createdAt).toLocaleString()}</time>&nbsp;
                    {lastModifiedAt !== createdAt && <>Modified: <time>{new Date(lastModifiedAt).toLocaleString()}</time>&nbsp;</>}
                </div>
                <button onClick={() => {
                    // TODO FIXME Make content editable
                    // TODO FIXME Select new content
                    modifyMessage(id, "new content")
                        .then(() => "")// TODO FIXME Reload message list
                }}>
                    Modify
                </button>
                <button onClick={() => deleteMessage(id)
                    .then(() => "") // TODO FIXME Reload message list
                }>
                    Delete
                </button>
            </div>
            <p>{content}</p>
            <div>
                <button onClick={() => addReplies(id)}>Show replies</button>
                <button>Reply</button>
            </div>
        </div>
    )
}

export default Message
