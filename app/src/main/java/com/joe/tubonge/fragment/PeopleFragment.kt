package com.joe.tubonge.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ListenerRegistration
import com.joe.tubonge.ChatActivity

import com.joe.tubonge.R
import com.joe.tubonge.constants.AppConstants
import com.joe.tubonge.recyclerview.item.PersonItem
import com.joe.tubonge.utils.FirestoreUtil
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_people.*
import org.jetbrains.anko.support.v4.startActivity

class PeopleFragment : Fragment() {

    private lateinit var userListenerRegistration: ListenerRegistration

    private var initRecyclerView = true

    private lateinit var peopleSection: Section

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        userListenerRegistration = FirestoreUtil.addUsersListener(this.activity!!, this::updateRecyclerView)
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    private fun updateRecyclerView(items: List<Item>) {

        fun init() {
            recyclerView_people.apply {
                layoutManager = LinearLayoutManager(this@PeopleFragment.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                    setOnItemClickListener(onItemClick)
                }
            }
            initRecyclerView = false
        }

        fun updateItems() = peopleSection.update(items)

        if (initRecyclerView)
            init()
        else
            updateItems()
    }

    private val onItemClick = OnItemClickListener { item, view ->
        if (item is PersonItem) {
            startActivity<ChatActivity>(AppConstants.USERNAME to item.person.name, AppConstants.USER_ID to item.userId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FirestoreUtil.removeListener(userListenerRegistration)
        initRecyclerView = true
    }

}
