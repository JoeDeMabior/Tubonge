package com.joe.tubonge.models

data class User(val name: String, val bio: String, val profilePicPath: String?) {
    constructor(): this("", "", null)
}
