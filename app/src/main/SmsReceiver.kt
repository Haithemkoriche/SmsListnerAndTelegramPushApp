package com.example.smslistner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (message in smsMessages) {
                val sender = message.displayOriginatingAddress
                val messageBody = message.messageBody
                Log.d("SmsReceiver", "From: $sender, Message: $messageBody")
                // Here, you can add your logic to send the SMS content to your server or process it further
            }
        }
    }
}
