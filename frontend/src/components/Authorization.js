export const getAuthorization = () => decodeURIComponent(document.cookie.match(new RegExp(
    "(?:^|; )" + "token".replace(/([.$?*|{}()[\]\\/+^])/g, '\\$1') + "=([^;]*)"
))[1])