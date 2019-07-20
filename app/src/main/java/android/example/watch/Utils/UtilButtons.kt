package android.example.watch.Utils

import android.content.Context
import android.example.watch.R
import android.example.watch.StopwatchActivity
import android.example.watch.TimerActivity
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UtilButtons {
    companion object{
        private lateinit var enableColor: Color
        private lateinit var disableColor: Color
        fun updateButtonsTimer(timerState: TimerActivity.TimerState, fab_add: FloatingActionButton,
                               fab_start: FloatingActionButton, fab_pause: FloatingActionButton, fab_stop: FloatingActionButton, context: Context){
            when (timerState){
                TimerActivity.TimerState.Running -> {
                    setDisableButton(fab_add, context)
                    setDisableButton(fab_start, context)
                    setEnableButton(fab_pause, context)
                    setEnableButton(fab_stop, context)
                }
                TimerActivity.TimerState.Paused -> {
                    setDisableButton(fab_add, context)
                    setDisableButton(fab_pause, context)
                    setEnableButton(fab_start, context)
                    setEnableButton(fab_stop, context)
                }
                TimerActivity.TimerState.Stopped -> {
                    setDisableButton(fab_pause, context)
                    setDisableButton(fab_stop, context)
                    setEnableButton(fab_add, context)
                    setEnableButton(fab_start, context)
                }
            }
        }
        fun updateButtonsStopwatch(stopwatchState: StopwatchActivity.StopwatchState,
                                   fab_start: FloatingActionButton, fab_restart: FloatingActionButton, fab_timer: FloatingActionButton, context: Context){
            when(stopwatchState){
                StopwatchActivity.StopwatchState.Running -> {
                    //fab_start.setImageDrawable(resources.getDrawable(R.drawable.ic_pause))
                    fab_start.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_pause
                    ))
                    setDisableButton(fab_restart, context)
                    setEnableButton(fab_timer, context)
                    //fab_restart.backgroundTintList = resources.getColorStateList(R.color.disableButtons)
                    //fab_timer.backgroundTintList = resources.getColorStateList(R.color.enableButtons)
                    //fab_start.isEnabled = false
                    //fab_restart.isEnabled = false
                    //fab_timer.isEnabled = true
                }
                StopwatchActivity.StopwatchState.Paused -> {
                    //fab_start.setImageDrawable(resources.getDrawable(R.drawable.ic_start))
                    fab_start.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_start
                    ))
                    setEnableButton(fab_restart, context)
                    setDisableButton(fab_timer, context)
                    //fab_start.isEnabled = true
                    //fab_timer.backgroundTintList = resources.getColorStateList(R.color.disableButtons)
                    //fab_restart.backgroundTintList = resources.getColorStateList(R.color.enableButtons)
                    //fab_restart.isEnabled = true
                    //fab_timer.isEnabled = false
                }
            }
        }
        private fun setEnableButton(fab: FloatingActionButton, context: Context){
            fab.isEnabled = true
            fab.backgroundTintList = ContextCompat.getColorStateList(context,
                R.color.enableButtons
            )
        }
         private fun setDisableButton(fab: FloatingActionButton, context: Context){
            fab.isEnabled = false
            fab.backgroundTintList = ContextCompat.getColorStateList(context,
                R.color.disableButtons
            )
        }
    }
}