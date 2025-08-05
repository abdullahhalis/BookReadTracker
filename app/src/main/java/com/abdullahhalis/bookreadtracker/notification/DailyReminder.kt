package com.abdullahhalis.bookreadtracker.notification

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import com.abdullahhalis.bookreadtracker.R
import com.abdullahhalis.bookreadtracker.data.BookRepository
import com.abdullahhalis.bookreadtracker.ui.home.MainActivity
import com.abdullahhalis.bookreadtracker.util.ID_REPEATING
import com.abdullahhalis.bookreadtracker.util.NOTIFICATION_CHANNEL_ID
import com.abdullahhalis.bookreadtracker.util.NOTIFICATION_CHANNEL_NAME
import com.abdullahhalis.bookreadtracker.util.NOTIFICATION_ID
import java.util.Calendar
import java.util.concurrent.Executors

class DailyReminder : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Executors.newSingleThreadExecutor().execute {
            val repository = BookRepository.getInstance(context)
            val totalOfCurrentlyReadBooks = repository.getTotalOfCurrentlyReadBook()

            showNotification(context, totalOfCurrentlyReadBooks)
        }
    }

    fun setDailyReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ID_REPEATING,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context) {
        val intent = Intent(context, DailyReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ID_REPEATING,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    private fun showNotification(context: Context, numberOfBooks: Int) {
        val prefManager = PreferenceManager.getDefaultSharedPreferences(context)
        val shouldNotify =
            prefManager.getBoolean(context.getString(R.string.pref_key_notify), false)

        if (!shouldNotify) return

        val notificationManager = NotificationManagerCompat.from(context)

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationContentText =
            if (numberOfBooks > 0) context.getString(R.string.notification_subtitle) else context.getString(
                R.string.notification_subtitle_no_book
            )
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(context.getString(R.string.notification_title, numberOfBooks))
            .setContentText(notificationContentText)
            .setContentIntent(pendingIntent)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}