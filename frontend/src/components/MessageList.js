import React, {useCallback, useEffect, useState} from "react";
import {SERVER_URL} from "../config";
import Message from "./Message";
import AddMessage from "./AddMessage";

const MessageList = ({message, level, deleteHierarchy}) => {
    const [messages, setMessages] = useState([]);
    const [show, setShow] = useState(true);
    const [replying, setReplying] = useState(false);

    const fetchMessages = useCallback((parentId) => {
        let url = `${SERVER_URL}/messages`
        if (parentId) {
            url += `?parentId=${parentId}`
        }
        fetch(url, {
            method: 'GET',
            credentials: 'include',
            headers: {
                "Accept": "application/json",
            }
        })
            .then(response => response.json())
            .then(data => setMessages(data))
            .then(() => setShow(false))
    }, [])

    useEffect(() => {
        if (!message && messages.length <= 0) {
            fetchMessages()
            setShow(true)
        }
    }, [message, messages, fetchMessages]);

    const hideMessages = () => {
        setMessages([])
        setShow(true)
    }

    const afterReply = () => {
        setReplying(false)
        fetchMessages(message?.id)
    }

    return (
        <div style={{paddingLeft: `${level}%`}}>
            {message &&
            <>
                <Message
                    operateReplies={show ? fetchMessages : hideMessages}
                    show={show}
                    addReply={() => setReplying(true)}
                    deleteHierarchy={deleteHierarchy}
                    id={message.id}
                    author={message.author}
                    createdAt={message.createdAt}
                    lastModifiedAt={message.lastModifiedAt}
                    content={message.content}
                />
            </>
            }
            {messages.map(message => (
                <MessageList
                    message={message}
                    level={level + 1}
                    deleteHierarchy={() => setMessages(messages.filter(mes => mes.id !== message.id))}
                    key={message.id}
                />
            ))}
            {(replying || level === -1) &&
            <AddMessage level={level + 1} parentId={message?.id} afterReply={afterReply}/>
            }
        </div>
    );
}

export default MessageList;
