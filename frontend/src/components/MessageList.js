import React, {useCallback, useEffect, useState} from "react";
import {SERVER_URL} from "../config";
import Message from "./Message";
import AddMessage from "./AddMessage";
import {getAuthorization} from "./Authorization";

const MessageList = ({message, level, deleteHierarchy}) => {
    const [messages, setMessages] = useState([]);
    const [show, setShow] = useState(true);
    const [replying, setReplying] = useState(false);

    const fetchMessages = useCallback((parentId = null) => {
        let url = `${SERVER_URL}/messages`
        if (parentId) {
            url += `/${parentId}/children`
        }
        fetch(url, {
            method: 'GET',
            credentials: 'include',
            headers: {
                "Accept": "application/json",
                "Authorization": getAuthorization(),
            },
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
    }, []);

    const hideMessages = useCallback(() => {
        setMessages([])
        setShow(true)
    }, [])

    const afterReply = useCallback(() => {
        setReplying(false)
        fetchMessages(message?.id)
    }, [fetchMessages, message])

    const addReply = useCallback(() => setReplying(true), []);

    return (
        <article style={{paddingLeft: `${level}%`}}>
            {level === -1 &&
            <AddMessage level={level + 1} parentId={message?.id} afterReply={afterReply}/>
            }
            {message &&
            <>
                <Message
                    operateReplies={show ? fetchMessages : hideMessages}
                    show={show}
                    addReply={addReply}
                    deleteHierarchy={deleteHierarchy}
                    id={message.id}
                    author={message.author}
                    createdAt={message.createdAt}
                    lastModifiedAt={message.lastModifiedAt}
                    content={message.content}
                />
            </>
            }
            {messages.map(message => {
                return (
                    <MessageList
                        message={message}
                        level={level + 1}
                        deleteHierarchy={() => {
                            const remainingMessages = messages.filter(mes => mes.id !== message.id)
                            setMessages(remainingMessages)
                            if (!remainingMessages.length) {
                                setShow(true)
                            }
                        }}
                        key={message.id}
                    />
                );
            })}
            {replying &&
            <AddMessage level={level + 1} parentId={message?.id} afterReply={afterReply}/>
            }
        </article>
    );
}

export default MessageList;
