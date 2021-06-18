import * as React from "react"
import {SERVER_URL} from "../config";

const areaStyle = {
    display: "grid",
    backgroundColor: "black",
    color: "white",
    margin: "0.2%",
}

const createMessage = (parentId, content) => {
    let url = `${SERVER_URL}/message`
    if (parentId) {
        url += `?parentId=${parentId}`
    }
    return fetch(url, {
        method: 'POST',
        credentials: 'include',
        headers: {
            contentType: "application/json",
        },
        body: JSON.stringify(content),
    })
}


const AddMessage = ({style, parentId, afterReply}) => {
    const addStyle = style ? {...areaStyle, ...style} : areaStyle
    return (
        <div style={addStyle}>
            <textarea/>
            <button onClick={() =>
                createMessage(parentId, "content")
                    .then(() => afterReply())
            }>
                Post
            </button>
        </div>
    )
}

export default AddMessage
