package com.example.track.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.res.Resources.NotFoundException
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.track.R
import com.example.track.other.Constants.ACTION_PAUSE_SERVICE
import com.example.track.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.example.track.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.track.other.Constants.ACTION_STOP_SERVICE
import com.example.track.other.Constants.NOTIFICATION_CHANEL_ID
import com.example.track.other.Constants.NOTIFICATION_CHANEL_NAME
import com.example.track.other.Constants.NOTIFICATION_ID
import com.example.track.ui.MainActivity
import dagger.hilt.android.internal.Contexts
import timber.log.Timber
import java.nio.file.attribute.AclEntry.Builder

class TrackingService : LifecycleService() {

    private var isFirstRun = true
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent.let {
            when (it?.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        Timber.d("resumed service....")
                    }
                }

                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                }

                ACTION_STOP_SERVICE -> {
                    Timber.d("Stop service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentTitle("Running App")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())
        startForeground(NOTIFICATION_ID,notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        },
        FLAG_UPDATE_CURRENT
    )

    private fun createNotificationChannel(notificationManager: NotificationManager) {

        val channel = NotificationChannel(
            NOTIFICATION_CHANEL_ID,
            NOTIFICATION_CHANEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )

        notificationManager.createNotificationChannel(channel)
    }
}