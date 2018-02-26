package com.vperi.android.firebase.messaging

fun Fcm.sendUpstreamMessage(msg: Message) =
    sendUpstreamMessage("message", msg.toJSON())