package com.vperi.android.firebase

import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import timber.log.Timber
import java.util.UUID

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

  fun sendAppInstanceMessage(isNewUser: Boolean) {
    val user = Firebase.currentUser
    val instanceId = Firebase.instanceId
    val appInstance = AppInstance(
        userId = user!!.uid,
        appInstanceId = instanceId.id,
        instanceTokenId = instanceId.token!!,
        isNewUser = isNewUser
    )
    sendUpstreamMessage(MESSAGE_APP_INSTANCE,
        Gson().toJson(appInstance))
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