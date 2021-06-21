const STORAGE_KEY_USER = "gatsbyUser"
const COOKIE_NAME = "token"

export const getAuthorization = () => {
    if (typeof document === `undefined`) {
        return undefined
    }
    const token = document.cookie.match(new RegExp(
        "(?:^|; )" + COOKIE_NAME.replace(/([.$?*|{}()[\]\\/+^])/g, '\\$1') + "=([^;]*)"
    ));
    return token ? decodeURIComponent(token[1]) : undefined
}

export const getUser = () => window.localStorage.getItem(STORAGE_KEY_USER)


export const setJwt = (token, afterExpiring) => {
    const jwt = JSON.parse(atob(token.split('.')[1]));
    const expiryDate = new Date(jwt.exp * 1000);
    document.cookie = `${COOKIE_NAME}=${encodeURIComponent(token)}; expires=${expiryDate.toUTCString()}; samesite=lax`
    window.localStorage.setItem(STORAGE_KEY_USER, jwt.sub)
    setTimeout(() => {
        if (getAuthorization() === undefined) {
            afterExpiring()
        }
    }, expiryDate - Date.now() + 2000);
}

export const clearAuthorization = () => {
    window.localStorage.removeItem(STORAGE_KEY_USER)
    document.cookie = `${COOKIE_NAME}=; max-age=-1`
}