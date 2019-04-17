package com.joe.tubonge.models

import java.util.*

interface Message {
    val time: Date
    val senderId: String
    val recipientId: String
    val senderName: String
    val type: String
}

object MessageType {
    const val TEXT = "TEXT"
    const val IMAGE = "IMAGE"
}
