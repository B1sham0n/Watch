package android.example.watch

import android.content.Context
import android.preference.PreferenceManager

@Suppress("DEPRECATION")
class Util {
    companion object{
        private val KEY_TIMER = "timer"
        private val KEY_MAX_SECONDS = "maxsec"
        fun getTimeForTimer(context: Context) : Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(KEY_TIMER, 0)
        }
        fun setTimeForTimer(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(KEY_TIMER, seconds)
            editor.apply()
        }
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