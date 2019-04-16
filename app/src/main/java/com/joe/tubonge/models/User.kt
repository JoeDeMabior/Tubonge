package com.joe.tubonge.models

data class User(
    val name: String,
    val bio: String,
    val profilePicPath: String?,
    val registrationTokens: MutableList<String>
) {
    constructor() : this("", "", null, mutableListOf())
}
