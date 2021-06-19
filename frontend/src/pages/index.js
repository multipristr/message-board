import * as React from "react"
import MessageList from "../components/MessageList";

const IndexPage = () => {
    return (
        <main>
            <title>Message board</title>
            <MessageList level={-1}/>
        </main>
    )
}

export default IndexPage