import * as React from "react"

const areaStyle = {
    display: "grid",
    backgroundColor: "black",
    color: "white",
    margin: "0.2%",
}

const AddMessage = ({parentId}) => {
    return (
        <div style={areaStyle}>
            <textarea/>
            <button>Post</button>
        </div>
    )
}

export default AddMessage
