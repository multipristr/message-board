import * as React from "react"
import {useRef} from "react"
import {SERVER_URL} from "../config";
import {getAuthorization} from "./Authorization";

const areaStyle = {
    display: "grid",
    marginTop: "0.2%",
}

const createMessage = (parentId, content) => {
    let url = `${SERVER_URL}/messages`
    return fetch(url, {
        method: 'POST',
        credentials: 'include',
        headers: {
            "Content-Type": "application/json",
            "Authorization": getAuthorization(),
        },
        body: JSON.stringify({content: content, parentId: parentId})
    })
}


const AddMessage = ({level, parentId, afterReply}) => {
    const ref = useRef(null);

    const addStyle = level > 0 ? {...areaStyle, marginLeft: `${level}%`} : areaStyle
    return (
        <div style={addStyle}>
            <textarea ref={ref} rows={4} placeholder="New message ..." required={true}/>
            <button onClick={() =>
                createMessage(parentId, ref.current.value)
                    .then(() => ref.current.value = "")
                    .then(afterReply)
            }>
                Post
            </button>
        </div>
    )
}

export default AddMessage
