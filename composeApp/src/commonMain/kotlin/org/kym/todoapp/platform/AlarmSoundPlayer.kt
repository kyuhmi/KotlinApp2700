package org.kym.todoapp.platform

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class AlarmSoundPlayer {
    fun playAlarmSound()
    fun stopAlarmSound()
}