package com.vperi.android.firebase.messaging

import com.google.firebase.messaging.RemoteMessage
import com.vperi.android.firebase.Firebase
import timber.log.Timber
import java.util.*

@Suppress("unused")
class Fcm(private val senderId: String?) {

  fun sendUpstreamMessage(data: Pair<String, String>) {
    sendUpstreamMessage(mapOf(data.first to data.second))
  }

  @Suppress("unused", "MemberVisibilityCanBePrivate")
  fun sendUpstreamMessage(key: String, value: String) {
    sendUpstreamMessage(mapOf(key to value))
  }

  @Suppress("MemberVisibilityCanBePrivate")
  fun sendUpstreamMessage(data: Map<String, String>) {
    Timber.d("sendUpstreamMessage: $data")

    sendUpstreamMessage(
        RemoteMessage
            .Builder("$senderId@gcm.googleapis.com")
            .setMessageId(UUID.randomUUID().toString())
            .setData(data))
  }

  private fun sendUpstreamMessage(builder: RemoteMessage.Builder) {
    try {
      Firebase.fcm.send(builder.build())
    } catch (e: Exception) {
      Timber.e(e)
      throw(e)
    }
  }

  companion object {
    private val TAG = Fcm::class.java.simpleName
    private const val MESSAGE_APP_INSTANCE = "appInstance"
  }
}