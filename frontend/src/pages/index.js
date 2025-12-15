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
    maxWidth: "75ch",
    justifySelf: "center",
}

export const Head = () => (
    <>
        <title>Message board</title>
        <meta name="title" content="Message board"/>
        <meta name="description" content="Public message board"/>

        <meta property="og:image" content="https://multipristr.github.io/message-board/android-chrome-512x512.png"/>
        <meta property="og:image:secure_url" content="https://multipristr.github.io/message-board/android-chrome-512x512.png"/>
        <meta property="og:image:width" content="512"/>
        <meta property="og:image:height" content="512"/>
        <meta property="og:image:type" content="image/png"/>
        <meta property="og:image:alt" content="Public message board"/>
        <meta name="twitter:card" content="summary"/>
        <meta property="og:type" content="website"/>
        <meta property="og:title" content="Message board"/>
        <meta property="og:description" content="Public message board"/>
        <meta property="og:url" content="https://multipristr.github.io/message-board/"/>
        <meta property="og:locale" content="en_US"/>

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