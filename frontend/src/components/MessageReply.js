import * as React from "react";

const repliesStyle = {
    gridArea: "replies",
    alignSelf: "center",
    justifySelf: "start",
    maxWidth: "100%",
    minWidth: "0",
}

const replyStyle = {
    gridArea: "reply",
    alignSelf: "center",
    justifySelf: "end",
    maxWidth: "100%",
    minWidth: "0",
}

const MessageReply = ({show, replying, onClickReplies, onClickReply}) => {
    return <>
        <button style={repliesStyle} onClick={onClickReplies}>{show ? "Show" : "Hide"} replies</button>
        <button style={replyStyle} onClick={onClickReply}>{replying ? "Stop replying" : "Reply"}</button>
    </>;
}

export default MessageReply