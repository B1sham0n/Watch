package android.example.watch

import android.content.Context
import android.preference.PreferenceManager

@Suppress("DEPRECATION")
class UtilTimer {
    companion object{
        private val KEY_TIMER = "timer"
        private val KEY_MAX_SECONDS = "maxsec"
        //TimeForTimer - здесь храним/берем значение таймера перед/после pause
        fun getTimeForTimer(context: Context) : Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(KEY_TIMER, 0)
        }
        fun setTimeForTimer(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(KEY_TIMER, seconds)
            editor.apply()
        }
        //MaxSecondsForTimer - здесь храним/берем максимальное значение таймера, например, для перезапуска
        fun getMaxSecondsForTimer(context: Context) : Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(KEY_MAX_SECONDS, 0)
        }
        fun setMaxSecondsForTimer(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(KEY_MAX_SECONDS, seconds)
            editor.apply()
        }

    }
}