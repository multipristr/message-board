import * as React from "react"
import {useCallback, useState} from "react"
import MessageList from "../components/MessageList";
import Login from "../components/Login";
import Logout from "../components/Logout";
import {clearAuthorization, getAuthorization, setJwt} from "../components/Authorization";

const indexStyle = {
    width: "100%",
    height: "100%",
    paddingTop: "0.2%",
    paddingLeft: "0.2%",
    paddingRight: "0.2%",
    boxSizing: "border-box",
}

export const Head = () => (
    <>
        <title>Message board</title>
        <meta content="Message board" name="title"/>
        <meta content="Public message board" name="description"/>

        <meta content="summary" name="twitter:card"/>
        <meta content="website" property="og:type"/>
        <meta content="https://multipristr.github.io/message-board/" property="og:url"/>
        <meta content="Message board" property="og:title"/>
        <meta content="Public message board" property="og:description"/>
        <meta content="" property="og:determiner"/>
        <meta content="en_US" property="og:locale"/>
        <meta content="https://multipristr.github.io/message-board/android-chrome-512x512.png" property="og:image"/>
        <meta content="https://multipristr.github.io/message-board/android-chrome-512x512.png" property="og:image:secure_url"/>
        <meta content="image/png" property="og:image:type"/>
        <meta content="512" property="og:image:width"/>
        <meta content="512" property="og:image:height"/>

        <link href="apple-touch-icon.png" rel="apple-touch-icon" sizes="180x180"/>
        <link href="favicon-32x32.png" rel="icon" sizes="32x32" type="image/png"/>
        <link href="favicon-16x16.png" rel="icon" sizes="16x16" type="image/png"/>
        <link href="site.webmanifest" rel="manifest"/>
        <link href="favicon.ico" rel="icon"/>
    </>
)

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