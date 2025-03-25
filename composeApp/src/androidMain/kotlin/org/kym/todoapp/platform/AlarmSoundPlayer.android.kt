package org.kym.todoapp.platform

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class AlarmSoundPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var vibrationJob: Job? = null
    private var coroutineScope = CoroutineScope(Dispatchers.Main)

    actual fun playAlarmSound() {
        stopAlarmSound() // ensure that no other sound is playing

        try {
            // get the default alarm sound
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

            // if the alarm sound is unavailable, try getting the notification sound
            val soundUri = if (alarmUri != Uri.EMPTY) alarmUri else RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            // create media player
            mediaPlayer = MediaPlayer.create(context, soundUri).apply {
                isLooping = true // loop the sound until stopped
                start() // start playing the sound
            }

            // start continuous vibration
            startContinuousVibration()
        } catch (e: Exception) {
            e.printStackTrace() // log the exception
        }
    }

    actual fun stopAlarmSound() {
        // stop and release the media player if it is playing
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release() // release resources
        }
        mediaPlayer = null // reset the media player reference

        // stop vibration
        stopContinuousVibration()
    }

    private fun startContinuousVibration() {
        vibrationJob?.cancel() // cancel any existing vibration job

        vibrationJob = coroutineScope.launch {
            // get vibrator
            val vibrator = getVibrator(context)

            // pattern for vibration: vibrate for 500ms, pause for 300ms, repeat
            val pattern = longArrayOf(0, 500, 300)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // for android 8+, use VibrationEffect
                val vibrationEffect = VibrationEffect.createWaveform(pattern, 1) // repeat from index 1
                vibrator.vibrate(vibrationEffect)
            } else {
                // for older versions, use deprecated vibrate method
                @Suppress("DEPRECATION")
                vibrator.vibrate(pattern, 1) // repeat from index 1
            }

            // keep coroutine running until vibration is stopped
            while (isActive) {
                delay(1000) // delay for 1 second to keep coroutine alive
            }
        }
    }

    private fun stopContinuousVibration() {
        // cancel the vibration job
        vibrationJob?.cancel()
        vibrationJob = null

        // stop vibration
        val vibrator = getVibrator(context)
        vibrator.cancel()
    }

    private fun getVibrator(context: Context): Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // for android 12+
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            // for older versions use depricated method
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    fun cleanup() {
        stopAlarmSound()
        stopContinuousVibration()
        coroutineScope.cancel()
    }
}