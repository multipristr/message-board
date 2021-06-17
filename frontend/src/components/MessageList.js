import React, {useEffect, useState} from "react";
import {SERVER_URL} from "../config";
import Message from "./Message";

const listStyle = {
    display: "grid",
}

const MessageList = ({parentId, level}) => {
    const [messages, setMessages] = useState([]);

    const fetchMessages = async (parentId, level) => {
        let url = `${SERVER_URL}/messages`
        if (parentId) {
            url += `?parentId=${parentId}`
        }
        const response = await fetch(url, {
            method: 'GET',
            credentials: 'include',
            headers: {
                accept: "application/json",
            }
        })
        const data = await response.json()
        data.forEach(mes => mes.level = level)
        setMessages(messages.concat(data))
    }

    useEffect(() => {
        if (messages.length <= 0) {
            fetchMessages(parentId, level)
        }
    }, [messages, fetchMessages, parentId, level]);


    return (
        <div style={listStyle}>
            {messages.map(message => (
                <Message
                    addReplies={fetchMessages}
                    id={message.id}
                    key={message.id}
                    author={message.author}
                    createdAt={message.createdAt}
                    lastModifiedAt={message.lastModifiedAt}
                    content={message.content}
                    level={message.level +1 }
                />
            ))}
        </div>
    );
}

export default MessageList;
