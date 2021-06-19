import * as React from "react"
import {useState} from "react"
import MessageList from "../components/MessageList";
import Login from "../components/Login";
import Logout from "../components/Logout";
import {STORAGE_KEY_USER} from "../config";

const indexStyle = {
    padding: "0.2%"
}

const IndexPage = () => {
    const [isAuthorized, setAuthorized] = useState(window.localStorage.getItem(STORAGE_KEY_USER) !== null)

    const afterLogin = (user) => {
        window.localStorage.setItem(STORAGE_KEY_USER, user)
        setAuthorized(true)
    }

    const afterLogout = () => {
        window.localStorage.removeItem(STORAGE_KEY_USER)
        setAuthorized(false)
    }

    return (
        <main style={indexStyle}>
            <title>Message board</title>
            {isAuthorized ?
                <>
                    <Logout afterLogout={afterLogout}/>
                    <MessageList level={-1}/>
                </>
                :
                <Login afterLogin={afterLogin}/>}
        </main>
    )
}

export default IndexPage