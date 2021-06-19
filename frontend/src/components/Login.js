import * as React from "react"
import {SERVER_URL} from "../config";

const Login = ({afterLogin}) => {
    const register = (e) => {
        e.preventDefault()
        fetch(`${SERVER_URL}/user`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({login: e.target.form[0].value, password: e.target.form[1].value}),
        })
            .then(response => {
                if (response.status > 199 && response.status < 300) {
                    logIn(e)
                }
            })
    }

    const logIn = (e) => {
        e.preventDefault()
        fetch(`${SERVER_URL}/messages`, {
            method: 'OPTIONS',
            credentials: 'include',
            headers: {
                "Authorization": `Basic ${btoa(e.target.form[0].value + ":" + e.target.form[1].value)}`,
                "X-Requested-With": "XMLHttpRequest",
            },
        }).then(response => {
            if (response.status > 199 && response.status < 300) {
                afterLogin(e.target.form[0].value)
            }
        })
    }

    return (
        <form method="post" onSubmit={logIn}>
            <label htmlFor="username">Username:</label>
            <input type="text" name="login" id="username" required={true}/>

            <label htmlFor="password">Password:</label>
            <input type="password" name="password" id="password" required={true}/>

            <input type="submit" data-inline="true" value="Log In" onClick={logIn}/>
            <input type="submit" data-inline="true" value="Register" onClick={register}/>
        </form>
    )
}

export default Login