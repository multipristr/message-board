import * as React from "react"
import MessageList from "../components/MessageList";
import Login from "../components/Login";


const IndexPage =  () => {
    const isLoggedIn = false // TODO FIXME verify login
    return (
        <main>
            <title>Message board</title>
            {isLoggedIn ? <MessageList level={-1}/> : <Login/>}
        </main>
    )
}

export default IndexPage