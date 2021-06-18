import * as React from "react"
import {SERVER_URL} from "../config";

const login = (login, password) => fetch(`${SERVER_URL}/login`, {
    method: 'GET',
    credentials: 'include',
})

const register = (login, password) => fetch(`${SERVER_URL}/user`, {
    method: 'POST',
    credentials: 'include',
    headers: {
        contentType: "application/json",
    },
    body: JSON.stringify({login: login, password: password}),
})

const Login = () => {
    return (
        <main>
            <title>Message board login</title>
            <form>
                <button onClick={() => login("user", "password")}>Login</button>
                <button onClick={() => {
                    register("user", "password")
                        .then(() => login("user", "password"))
                }}>
                    Register
                </button>
            </form>
        </main>
    )
}

export default Login