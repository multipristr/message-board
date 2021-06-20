import * as React from "react"
import {useCallback, useState} from "react"
import MessageList from "../components/MessageList";
import Login from "../components/Login";
import Logout from "../components/Logout";
import {clearAuthorization, getAuthorization, setJwt} from "../components/Authorization";

const indexStyle = {
    padding: "0.2%"
}

const IndexPage = () => {
    const [isAuthorized, setAuthorized] = useState(getAuthorization() !== undefined)

    const afterLogout = useCallback(() => {
        clearAuthorization()
        setAuthorized(false)
    }, [])

    const afterLogin = useCallback((token) => {
        setJwt(token, afterLogout)
        setAuthorized(true)
    }, [afterLogout])

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