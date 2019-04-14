package com.joe.tubonge.models

data class User(val name: String, val bio: String, val profilePic: String?) {
    constructor(): this("", "", null)
}
