import * as React from "react"
import {useRef} from "react"
import {SERVER_URL} from "../config";

const areaStyle = {
    display: "grid",
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
    const ref = useRef(null);

    const addStyle = style ? {...areaStyle, ...style} : areaStyle
    return (
        <div style={addStyle}>
            <textarea ref={ref} rows={4} id="`content`" placeholder="New message ..." autoFocus required/>
            <button onClick={() =>
                createMessage(parentId, ref.current.value)
                    .then(() => afterReply())
            }>
                Post
            </button>
        </div>
    )
}

export default AddMessage