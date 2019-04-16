package com.joe.tubonge.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.joe.tubonge.utils.FirestoreUtil

private const val TAG = "FCM"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token")

        if (FirebaseAuth.getInstance().currentUser != null)
            addTokenToFirestore(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage?.notification != null) {
            // TODO: Show notification
            Log.d(TAG, "FCM message received.")
        }
    }

    companion object {
        fun addTokenToFirestore(newRegistrationToken: String?) {
            if (newRegistrationToken == null) throw NullPointerException("The FCM token is null")
            FirestoreUtil.getFcmRegistrationTokens { tokens ->
                if (tokens.contains(newRegistrationToken))
                    return@getFcmRegistrationTokens

                tokens.add(newRegistrationToken)
                FirestoreUtil.setFcmRegistrationTokens(tokens)
            }
        }
    }
}
