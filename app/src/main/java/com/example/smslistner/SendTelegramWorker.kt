package com.example.smslistner

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URLEncoder

class SendTelegramWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val messageText = inputData.getString("messageText") ?: return Result.failure()
        return try {
            if (sendToTelegram(messageText)) {
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: IOException) {
            Result.retry()
        }
    }

    private suspend fun sendToTelegram(message: String): Boolean = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val encodedMessage = URLEncoder.encode(message, "UTF-8")
        val url = "https://api.telegram.org/bot6950283126:AAEIQisYlUz6fgo26kFz4SDrL9WKImwdEQM/sendMessage?chat_id=@mysmsinmyphone&text=$encodedMessage"

        val request = Request.Builder()
            .url(url)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                response.isSuccessful
            }
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}
