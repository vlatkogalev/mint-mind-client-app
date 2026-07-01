package app.data.remote

import app.Const

fun constructUrl(url: String): String {
    return when {
        url.contains(Const.BASE_URL) -> url
        url.startsWith("/") -> Const.BASE_URL + url.drop(1)
        else -> Const.BASE_URL + url
    }
}