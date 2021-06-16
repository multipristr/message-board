import * as React from "react"
import Message from "../components/Message"
import AddMeesage from "../components/AddMeesage";

const IndexPage = () => {
    return (
        <main>
            <title>Message board</title>
            <Message author="author" createdAt="cre" modifiedAt="mod" content="gjdfjgbdjdgdfjkdf"/>
            <Message author="fgd" createdAt="csdfsdfsdfsre" content="gjdfjgsdfsdfsdfsdfbdjdgdfjkdf" level={1}/>
            <Message author="fgd" createdAt="csdfsdfsdfsre" content="gjdfjgsdfsdfsdfsdfbdjdgdfjkdf" level={5}/>
            <Message author="ausdfdsfthor" createdAt="csdfsdfre" modifiedAt="modsfdsfsdfsdfd"
                     content="gjdfjdsfsdfgbdjdgdfjkdf"/>
            <AddMeesage/>
        </main>
    )
}

export default IndexPage
