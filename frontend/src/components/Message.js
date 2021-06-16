import * as React from "react"

const messageStyle = {
    display: "grid",
    backgroundColor: "black",
    color: "white",
    margin: "0.2%",
}

const Message = ({author, createdAt, modifiedAt, content, level}) => {
    const style = level ? {marginLeft: `${level}%`, ...messageStyle} : messageStyle
    return (
        <div style={style}>
            <header>{author} Created: <time>{createdAt}</time> {modifiedAt && <>Modified:
                + <time>{modifiedAt}</time></>}</header>
            <p>{content}</p>
            <button>Show replies</button>
            <button>Reply</button>
        </div>
    )
}

export default Message
