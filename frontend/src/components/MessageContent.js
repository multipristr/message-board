import * as React from "react";

const contentStyle = {
    gridArea: "content",
    alignSelf: "start",
    justifySelf: "start",
    maxWidth: "100%",
    minWidth: "0",
}

const MessageContent = ({ref, contentEditable, content}) => {
    return <p style={contentStyle} ref={ref} contentEditable={contentEditable}>{content}</p>;
}

export default MessageContent