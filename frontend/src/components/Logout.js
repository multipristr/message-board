import * as React from "react"
import {SERVER_URL} from "../config";

const logoutStyle = {
    display: "grid",
    width: "100%"
}

const Logout = ({afterLogout}) => {
    const logOut = (e) => {
        e.preventDefault()
        fetch(`${SERVER_URL}/user`, {
            method: 'OPTIONS',
            credentials: 'include',
            headers: {
                "Authorization": `Basic ${btoa(":")}`,
                "X-Requested-With": "XMLHttpRequest",
            },
        })
            .then(afterLogout)
    }

    return (
        <button style={logoutStyle} onClick={logOut}>Log Out</button>
    )
}

export default Logout