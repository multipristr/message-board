import React, {useCallback, useEffect, useState} from "react";
import {SERVER_URL} from "../config";
import Message from "./Message";
import AddMessage from "./AddMessage";

const MessageList = ({message, parentId, level}) => {
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
                accept: "application/json",
            }
        })
            .then(response => response.json())
            .then(data => setMessages(data))
            .then(() => setShow(false))
    }, [])

    useEffect(() => {
        if (!message && messages.length <= 0) {
            fetchMessages(parentId)
            setShow(true)
        }
    }, [message, messages, fetchMessages, parentId]);

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
                    key={message.id}
                />
            ))}
            {(replying || level === -1) &&
            <AddMessage style={{marginLeft: `${level + 1}%`}} parentId={message?.id} afterReply={afterReply}/>
            }
        </div>
    );
}

export default MessageList;
