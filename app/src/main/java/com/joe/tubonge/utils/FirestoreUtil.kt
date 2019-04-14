package com.joe.tubonge.utils

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.joe.tubonge.models.User
import com.joe.tubonge.recyclerview.item.PersonItem
import com.xwray.groupie.kotlinandroidextensions.Item

private const val TAG = "FIRESTORE"

object FirestoreUtil {
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document(
            "users/${FirebaseAuth.getInstance().currentUser?.uid ?: throw NullPointerException("UID is null.")}"
        )

    fun initCurrentUser(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = User(FirebaseAuth.getInstance().currentUser?.displayName ?: "", "", null)
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            } else {
                onComplete()
            }
        }
    }

    fun updateCurrentUser(name: String = "", bio: String = "", profilePic: String? = null) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (bio.isNotBlank()) userFieldMap["bio"] = bio
        if (profilePic != null) userFieldMap["profilePicPath"] = profilePic
        currentUserDocRef.update(userFieldMap)
    }

    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get().addOnSuccessListener {
            onComplete(it.toObject(User::class.java)!!)
        }
    }

    fun addUsersListener(context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {
        return firestoreInstance.collection("users").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.e(TAG, "Users listener error.", firebaseFirestoreException)
                return@addSnapshotListener
            }

            val items = mutableListOf<Item>()
            querySnapshot?.documents?.forEach {
                if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
                    items.add(
                        PersonItem(
                            it.toObject(User::class.java)!!,
                            it.id,
                            context
                        )
                    )
            }
            onListen(items)
        }
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()
}
