package com.joe.tubonge.models

data class ChatChannel(val userIds: MutableList<String>) {
    constructor(): this(mutableListOf())
}
