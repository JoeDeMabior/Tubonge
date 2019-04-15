package com.joe.tubonge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.joe.tubonge.constants.AppConstants
import com.joe.tubonge.models.MessageType
import com.joe.tubonge.models.TextMessage
import com.joe.tubonge.utils.FirestoreUtil
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messagesListenerRegistration: ListenerRegistration

    private var initRecyclerView = true

    private lateinit var messagesSection: Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(AppConstants.USERNAME)

        val otherUserId = intent.getStringExtra(AppConstants.USER_ID)

        FirestoreUtil.getOrCreateChatChannel(otherUserId) { channelId ->
            messagesListenerRegistration =
                FirestoreUtil.addChatMessagesListener(channelId, this, this::updateRecyclerView)

            imageView_send.setOnClickListener {
                val messageToSend = TextMessage(
                    editText_message.text.toString(),
                    Calendar.getInstance().time,
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    MessageType.TEXT
                )
                editText_message.setText("")

                FirestoreUtil.sendMessage(messageToSend, channelId)
            }

            fab_send_image.setOnClickListener {
                TODO("Send image messages")
            }
        }
    }

    private fun updateRecyclerView(messages: List<Item>) {
        fun init() {
            recyclerView_messages.apply {
                layoutManager = LinearLayoutManager(this@ChatActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messagesSection = Section(messages)
                    this.add(messagesSection)
                }
            }
            initRecyclerView = false
        }

        fun updateItems() = messagesSection.update(messages)

        if (initRecyclerView)
            init()
        else
            updateItems()

        recyclerView_messages.scrollToPosition(recyclerView_messages.adapter!!.itemCount - 1)
    }
}
