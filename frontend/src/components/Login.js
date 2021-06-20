import * as React from "react"
import {useState} from "react"
import {SERVER_URL} from "../config";

const formStyle = {
    display: "grid",
    grid: `
                    ". . . . ." auto
                    ". usernameLabel usernameLabel usernameLabel ." 2em
                    ". username username username ." 2em
                    ". passwordLabel passwordLabel passwordLabel ." 2em
                    ". password password password ." 2em
                    ". login . register ." 2em
                    ". fail fail fail ." 2em
                     ". . . . ." auto           
                / 33% auto 0.5% auto 33%
                `,
    height: "100%",
    width: "100%",
}

const usernameStyle = {
    gridArea: "username",
    alignSelf: "center",
    justifySelf: "start",
    height: "2em",
    width: "100%",
    padding: "0",
    border: "0",
}

const passwordStyle = {
    gridArea: "password",
    alignSelf: "center",
    justifySelf: "start",
    height: "2em",
    width: "100%",
    padding: "0",
    border: "0",
}

const usernameLabelStyle = {
    gridArea: "usernameLabel",
    alignSelf: "end",
    justifySelf: "start",
    height: "2em",
    width: "100%",
}

const passwordLabelStyle = {
    gridArea: "passwordLabel",
    alignSelf: "end",
    justifySelf: "start",
    height: "2em",
    width: "100%",
}

const loginStyle = {
    gridArea: "login",
    alignSelf: "center",
    justifySelf: "start",
    height: "2em",
    width: "100%",
    border: "0",
}

const registerStyle = {
    gridArea: "register",
    alignSelf: "center",
    justifySelf: "end",
    height: "2em",
    width: "100%",
    border: "0",
}

const failStyle = {
    gridArea: "fail",
    alignSelf: "center",
    justifySelf: "start",
    height: "2em",
    width: "100%",
    color: "red",
}

const Login = ({afterLogin}) => {
    const [fail, setFail] = useState(null)

    const register = (e) => {
        e.preventDefault()
        fetch(`${SERVER_URL}/register`, {
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
                } else if (response.status === 409) {
                    setFail("Username already taken. Try different one or log in.")
                }
            })
    }

    const logIn = (e) => {
        e.preventDefault()
        fetch(`${SERVER_URL}/login`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                "Content-Type": "application/json",
                "Accept": "text/plain",
            },
            body: JSON.stringify({login: e.target.form[0].value, password: e.target.form[1].value}),
        })
            .then(response => response.text())
            .then(token => afterLogin(token))
            .catch(() => setFail("Invalid username or password. Correct the credentials or register."))
    }

    return (
        <form style={formStyle} method="post" onSubmit={logIn}>
            <label style={usernameLabelStyle} htmlFor="username">Username:</label>
            <input style={usernameStyle} type="text" name="login" id="username" required={true}/>

            <label style={passwordLabelStyle} htmlFor="password">Password:</label>
            <input style={passwordStyle} type="password" name="password" id="password" required={true}/>

            <input style={loginStyle} type="submit" data-inline="true" value="Log In" onClick={logIn}/>
            <input style={registerStyle} type="submit" data-inline="true" value="Register" onClick={register}/>
            <strong style={failStyle}>{fail}</strong>
        </form>
    )
}

export default Login