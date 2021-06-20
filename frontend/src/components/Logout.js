import * as React from "react"

const logoutStyle = {
    display: "grid",
    width: "100%"
}

const Logout = ({afterLogout}) => {
    return (
        <button style={logoutStyle} onClick={afterLogout}>Log Out</button>
    )
}

export default Logout