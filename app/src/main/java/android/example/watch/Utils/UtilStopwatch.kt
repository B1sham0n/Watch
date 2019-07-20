package android.example.watch.Utils

import android.content.Context
import android.preference.PreferenceManager

@Suppress("DEPRECATION")
class UtilStopwatch{
    companion object{
        private val KEY_TIME_BUFF = "timebuff"
        private val KEY_CURRENT_TIME = "currenttime"
        var listTimer: ArrayList<Long> = ArrayList()
        fun setTimeBuff(timeBuff: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(KEY_TIME_BUFF, timeBuff)
            editor.apply()
        }
        fun getTimeBuff(context: Context):Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(KEY_TIME_BUFF, 0)
        }
        fun getListTimerLength(context: Context):Int{
            return listTimer.size
        }
        fun setNewTimerInList(time: Long){
            listTimer.add(time)
        }
        fun getTimerFromList(id: Int):Long{
            return listTimer[id]
        }
        fun setCurrentTime(time: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(KEY_CURRENT_TIME, time)
            editor.apply()
        }
        fun getCurrentTime(context: Context):Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(KEY_CURRENT_TIME, 0)
        }
    }
}