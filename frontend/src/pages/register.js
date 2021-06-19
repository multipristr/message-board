import * as React from "react"
import {SERVER_URL} from "../config";

const RegisterPage = () => {
    return (
        <form method="post" action={`${SERVER_URL}/user`}>
            <label htmlFor="username">Username:</label>
            <input type="text" name="login" id="username" required={true}/>

            <label htmlFor="password">Password:</label>
            <input type="password" name="password" id="password" required={true}/>

            <input type="submit" data-inline="true" value="Register"/>
        </form>
    )
}

export default RegisterPage