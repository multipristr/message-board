import * as React from "react"
import AddMessage from "../components/AddMessage";
import MessageList from "../components/MessageList";


const IndexPage =  () => {
    return (
        <main>
            <title>Message board</title>
            <MessageList level={0}/>
            <AddMessage/>
        </main>
    )
}

export default IndexPage