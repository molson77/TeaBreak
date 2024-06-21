package com.example.teabreak

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Intent
import android.media.RingtoneManager
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.teabreak.data.TeaType
import com.example.teabreak.data.Utils


class TeaTimerService : Service() {

    companion object {

        // IDs for notifications
        const val CHANNEL_ID = "Timer_Notifications"
        const val TIMER_NOTIFICATION_ID = 1470
        const val FINISHED_NOTIFICATION_ID = 1471

        // TeaTimer countdown broadcast receiver
        const val COUNTDOWN_BR: String = "your_package_name.countdown_br"
        const val COUNTDOWN_MILLIS_REMAINING: String = "COUNTDOWN_MILLIS_REMAINING"

        // intent extras
        const val TEA_ID = "TEA_ID"
        const val TEA_NAME = "TEA_NAME"
        const val TEA_TYPE = "TEA_TYPE"
        const val STEEP_TIME_SECONDS = "STEEP_TIME_SECONDS"

        // actions
        const val TIMER_ACTION = "TIMER_ACTION"
        const val MOVE_TO_BACKGROUND = "MOVE_TO_BACKGROUND"
        const val MOVE_TO_FOREGROUND = "MOVE_TO_FOREGROUND"
        const val START = "START"
        const val CANCEL = "CANCEL"

        var isTimerServiceActive: Boolean = false

    }

    // Getting access to the NotificationManager
    private lateinit var notificationManager: NotificationManager

    // countdown timer
    var countDownTimer: CountDownTimer? = null

    // broadcast receiver intent
    val broadcastIntent = Intent(COUNTDOWN_BR)


    override fun onBind(p0: Intent?): IBinder? {
        Log.d("TeaTimer", "TeaTimer onBind")
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createChannel()
        getNotificationManager()

        val action = intent?.getStringExtra(TIMER_ACTION)!!

        Log.d("Stopwatch", "onStartCommand Action: $action")

        when (action) {
            START -> {
                val teaId = intent.getIntExtra(TEA_ID, 0)
                val teaName = intent.getStringExtra(TEA_NAME) ?: ""
                val teaType = intent.getSerializableExtra(TEA_TYPE) as TeaType
                val timerDuration = (intent.getIntExtra(STEEP_TIME_SECONDS, 120) * 1000).toLong()
                startTimer(teaId, teaName, teaType, timerDuration)
            }
            CANCEL -> cancelTimer()
            MOVE_TO_FOREGROUND -> {
                val teaId = intent.getIntExtra(TEA_ID, 0)
                val teaName = intent.getStringExtra(TEA_NAME) ?: ""
                val teaType = intent.getSerializableExtra(TEA_TYPE) as TeaType
                val timerDuration = (intent.getIntExtra(STEEP_TIME_SECONDS, 120) * 1000).toLong()
                moveToForeground(teaId, teaName, teaType, timerDuration)
            }
            MOVE_TO_BACKGROUND -> moveToBackground()
        }

        return START_STICKY
    }

    private fun cancelTimer() {
        Log.d("TeaTimer", "TeaTimer cancelled")
        deleteBrewingNotification()
        countDownTimer?.cancel()
        countDownTimer = null
        isTimerServiceActive = false
    }

    private fun startTimer(teaId: Int, teaName: String, teaType: TeaType, timerDuration: Long) {
        Log.d("TeaTimer", "TeaTimer started: $teaName $timerDuration")
        countDownTimer = object: CountDownTimer(timerDuration, 1000) {
            override fun onTick(p0: Long) {
                Log.d("TeaTimer", "TeaTimer $teaName millis remaining: $p0")
                if (p0 != 0L) {
                    broadcastIntent.putExtra(COUNTDOWN_MILLIS_REMAINING, p0)
                    sendBroadcast(broadcastIntent)
                    updateNotification(teaId, teaName, teaType, p0)
                }
            }

            override fun onFinish() {
                Log.d("TeaTimer", "TeaTimer finished: $teaName $timerDuration")
                broadcastIntent.putExtra(COUNTDOWN_MILLIS_REMAINING, 0)
                sendBroadcast(broadcastIntent)
                deleteBrewingNotification()
                sendFinishedNotification(teaId, teaName, teaType)
                this@TeaTimerService.stopSelf()
            }
        }
        countDownTimer?.start()
    }

    private fun moveToBackground() {
        Log.d("TeaTimer", "TeaTimer moved to background")
        stopForeground(true)
    }

    private fun moveToForeground(teaId: Int, teaName: String, teaType: TeaType, timerDuration: Long) {
        Log.d("TeaTimer", "TeaTimer moved to foreground")
        startForeground(TIMER_NOTIFICATION_ID, buildBrewingNotification(teaId, teaName, teaType, timerDuration))
    }

    private fun createChannel() {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            "TeaTimer",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationChannel.setSound(null, null)
        notificationChannel.setShowBadge(true)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun buildBrewingNotification(teaId: Int, teaName: String, teaType: TeaType, p0: Long): Notification {

        val title = "Brewing $teaName"

        val secondsRemaining = if (p0.toInt() == 0) 0 else (p0.toInt() / 1000)
        val timeText = Utils.formatTime(secondsRemaining)

        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        val homeIntent = Intent(this, MainActivity::class.java)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(homeIntent)
        val timerIntent = Intent(this, TeaTimerActivity::class.java)
        timerIntent.putExtra(TeaTimerActivity.TEA_ID, teaId)
        timerIntent.putExtra(TeaTimerActivity.TEA_TYPE, teaType.name)
        stackBuilder.addNextIntent(timerIntent)
        val pendingIntent: PendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setOngoing(true)
            .setContentText(
                "$timeText remaining"
            )
            .setColorized(true)
            .setColor(Utils.getTeaBackgroundColor(teaType).toArgb())
            .setSmallIcon(R.drawable.tea_bag)
            .setSilent(true)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .build()
    }

    private fun buildFinishedNotification(teaId: Int, teaName: String, teaType: TeaType): Notification {

        val title = "Finished brewing $teaName!"

        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        val homeIntent = Intent(this, MainActivity::class.java)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(homeIntent)
        val timerIntent = Intent(this, TeaTimerActivity::class.java)
        timerIntent.putExtra(TeaTimerActivity.TEA_ID, teaId)
        timerIntent.putExtra(TeaTimerActivity.TEA_TYPE, teaType.name)
        stackBuilder.addNextIntent(timerIntent)
        val pendingIntent: PendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val vibration = longArrayOf(600, 600, 600)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setOngoing(false)
            .setColorized(true)
            .setColor(Utils.getTeaBackgroundColor(teaType).toArgb())
            .setSmallIcon(R.drawable.tea_bag)
            .setOnlyAlertOnce(false)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSound(alarmSound)
            .setVibrate(vibration)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }



    private fun deleteBrewingNotification() {
        notificationManager.cancel(TIMER_NOTIFICATION_ID)
    }

    private fun updateNotification(teaId: Int, teaName: String, teaType: TeaType, p0: Long) {
        notificationManager.notify(
            TIMER_NOTIFICATION_ID,
            buildBrewingNotification(teaId, teaName, teaType, p0)
        )
    }

    private fun sendFinishedNotification(teaId: Int, teaName: String, teaType: TeaType) {
        notificationManager.notify(
            FINISHED_NOTIFICATION_ID,
            buildFinishedNotification(teaId, teaName, teaType)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelTimer()
        deleteBrewingNotification()
    }

    private fun getNotificationManager() {
        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
    }
}