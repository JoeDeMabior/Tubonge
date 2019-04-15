package com.joe.tubonge.recyclerview.item

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.joe.tubonge.R
import com.joe.tubonge.models.TextMessage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_text_message.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.wrapContent
import java.text.SimpleDateFormat

class TextMessageItem(val message: TextMessage, val context: Context) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView_message_text.text = message.text
        setTimeText(viewHolder)
        setRootMessageGravity(viewHolder)
    }

    override fun getLayout() = R.layout.item_text_message

    private fun setTimeText(viewHolder: ViewHolder) {
        val dateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
        viewHolder.textView_message_time.text = dateFormat.format(message.time)
    }

    private fun setRootMessageGravity(viewHolder: ViewHolder) {
        if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            viewHolder.message_root.apply {
                backgroundResource = R.drawable.rect_round_white
                val layoutParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.END)
                this.layoutParams = layoutParams
            }
        } else {
            viewHolder.message_root.apply {
                backgroundResource = R.drawable.rect_round_primary_color
                val layoutParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.START)
                this.layoutParams = layoutParams
            }
        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is TextMessageItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? TextMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }
}
