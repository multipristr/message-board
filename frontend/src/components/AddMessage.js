import * as React from "react"
import {SERVER_URL} from "../config";

const areaStyle = {
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


const AddMessage = ({parentId}) => {
    return (
        <div style={areaStyle}>
            <textarea/>
            <button onClick={() =>
                createMessage(parentId, "content")
                    .then(() => "") // TODO FIXME Reload messages
            }>
                Post
            </button>
        </div>
    )
}

export default AddMessage
