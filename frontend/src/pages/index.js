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

    const afterLogin = (token) => {
        const jwt = JSON.parse(atob(token.split('.')[1]));
        document.cookie = `token=${encodeURIComponent(token)}; expires=${new Date(jwt.exp * 1000).toUTCString()}; samesite=lax`
        window.localStorage.setItem(STORAGE_KEY_USER, jwt.sub)
        setAuthorized(true)
    }

    const afterLogout = () => {
        window.localStorage.removeItem(STORAGE_KEY_USER)
        document.cookie = `token=; max-age=-1`
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