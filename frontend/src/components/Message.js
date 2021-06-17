import * as React from "react"
import MessageList from "./MessageList";
import * as ReactDOM from "react-dom";
import * as ReactDOMServer from "react-dom/server";

const messageStyle = {
    display: "grid",
    backgroundColor: "black",
    color: "white",
    margin: "0.2%",
}

const repliesStyle = {
    display: "grid",
}

const replyStyle = {
    display: "grid",
}

const Message = ({id, author, createdAt, lastModifiedAt, content, level, addReplies}) => {
    const style = level ? {marginLeft: `${level}%`, ...messageStyle} : messageStyle
    return (
        <div style={style} id={id}>
            <header>{author} Created: <time>{createdAt}</time> {lastModifiedAt && <>Modified:
                <time>{lastModifiedAt}</time></>}</header>
            <p>{content}</p>
            <button style={repliesStyle} onClick={() => addReplies(id, level + 1)}>Show replies</button>
            <button style={replyStyle}>Reply</button>
        </div>
    )
}

export default Message
