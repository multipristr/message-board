import * as React from "react"

const messageStyle = {
    backgroundColor: "black",
    color: "white",
    margin: "0.2%",
    padding: "0.2%",
}

const headStyle = {
    fontFamily: "monospace",
}

const Message = ({id, author, createdAt, lastModifiedAt, content, addReplies}) => {
    return (
        <div style={messageStyle} id={id}>
            <div style={headStyle}>
                {author}&nbsp;Created: <time>{new Date(createdAt).toLocaleString()}</time>&nbsp;
                {lastModifiedAt !== createdAt && <>Modified: <time>{new Date(lastModifiedAt).toLocaleString()}</time></>}
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
