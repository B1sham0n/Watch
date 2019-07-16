package android.example.watch

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_stopwatch.*
import kotlinx.android.synthetic.main.content_stopwatch.*


class StopwatchActivity : AppCompatActivity() {
    private lateinit var timer: CountDownTimer
    var stopwatchState = StopwatchState.Paused
    var enableStartBtnState = StopwatchState.Paused
    var StartTime = 0L
    var TimeBuff = 0L
    var MillisecondTime = 0L
    var UpdateTime = 0L
    var handler: Handler = Handler()
    var listTimer: ArrayList<Long> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stopwatch)
        setSupportActionBar(toolbar)
        updateEnableButtonsUI()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#4747d1")//цвет статусбара
        }
        //TODO: сделать класс UtilStopwatch и хранить там время (мб timebuff и arraylist)
        fab_start.setOnClickListener { view ->
            //startStopwatch()
            functionalStartButton()
            updateEnableButtonsUI()
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        fab_timer.setOnClickListener { view ->
            //pauseStopwatch()
            saveTimer()
            updateEnableButtonsUI()
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        fab_restart.setOnClickListener { view ->
            restartStopwatch()
            updateEnableButtonsUI()
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
    }
    enum class StopwatchState{ Running, Paused}
    private fun startStopwatch(){
        stopwatchState = StopwatchState.Running
        StartTime = SystemClock.uptimeMillis()
        handler.postDelayed(runnable, 0)

    }
    private fun pauseStopwatch(){
        stopwatchState = StopwatchState.Paused
        TimeBuff += MillisecondTime
        handler.removeCallbacks(runnable)
    }
    private fun restartStopwatch(){
        stopwatchState = StopwatchState.Paused
        StartTime = 0
        TimeBuff = 0
        MillisecondTime = 0
        tvStopwatch.text = "00:00:00"
        handler.removeCallbacks(runnable)
    }
    private fun functionalStartButton(){
        when(enableStartBtnState){
            StopwatchState.Paused ->{
                startStopwatch()
                enableStartBtnState = StopwatchState.Running
            }
            StopwatchState.Running -> {
                pauseStopwatch()
                enableStartBtnState = StopwatchState.Paused
            }
        }
    }
    private fun saveTimer(){
        var Seconds = 0L
        var Minutes = 0L
        var MilliSeconds = 0L
        var tempMillisecondTime = 0L
        MillisecondTime = SystemClock.uptimeMillis() - StartTime
        UpdateTime = TimeBuff + MillisecondTime
        listTimer.add(UpdateTime)
        Seconds = (UpdateTime / 1000)
        Minutes = Seconds / 60
        Seconds = Seconds % 60
        MilliSeconds = (UpdateTime % 1000)
        val layoutInflater : LayoutInflater = LayoutInflater.from(applicationContext)
        val view : View = layoutInflater.inflate(R.layout.inflater_timer_stopwatch, parentLayout, false)
        view.findViewById<TextView>(R.id.tvIntervalStopwatch).text = "Interval " + listTimer.size
        view.findViewById<TextView>(R.id.tvTimerStopwatch).text = (
                "" + Minutes + ":"
                        + String.format("%02d", Seconds) + ":"
                        + String.format("%03d", MilliSeconds)
                )
        if(listTimer.size > 1 && (listTimer.get(listTimer.size-1) > listTimer.get(listTimer.size-2))){
            //вторая проверка - защита от отрицательных значений после restart timer
            tempMillisecondTime = listTimer.get(listTimer.size - 2)
            tempMillisecondTime = UpdateTime - tempMillisecondTime
        }
        view.findViewById<TextView>(R.id.tvDurationStopwatch).text = (
                "Duration: " +(tempMillisecondTime / 1000)/ 60 + ":"
                        + String.format("%02d", (tempMillisecondTime / 1000)) + ":"
                        + String.format("%03d", (tempMillisecondTime % 1000))
                )
        parentLayout.addView(view)
    }
    private fun updateEnableButtonsUI(){
        when(stopwatchState){
            StopwatchState.Running -> {
                fab_start.setImageDrawable(resources.getDrawable(R.drawable.ic_pause))
                fab_restart.backgroundTintList = resources.getColorStateList(R.color.disableButtons)
                fab_timer.backgroundTintList = resources.getColorStateList(R.color.enableButtons)
                //fab_start.isEnabled = false
                fab_restart.isEnabled = false
                fab_timer.isEnabled = true
            }
            StopwatchState.Paused -> {
                fab_start.setImageDrawable(resources.getDrawable(R.drawable.ic_start))
                //fab_start.isEnabled = true
                fab_timer.backgroundTintList = resources.getColorStateList(R.color.disableButtons)
                fab_restart.backgroundTintList = resources.getColorStateList(R.color.enableButtons)
                fab_restart.isEnabled = true
                fab_timer.isEnabled = false
            }
        }
    }
    var runnable:Runnable = object:Runnable {
         var Seconds = 0L
         var Minutes = 0L
         var MilliSeconds = 0L
        override fun run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime
            UpdateTime = TimeBuff + MillisecondTime
            Seconds = (UpdateTime / 1000)
            Minutes = Seconds / 60
            Seconds = Seconds % 60
            MilliSeconds = (UpdateTime % 1000)
            tvStopwatch.text = (
               "" + Minutes + ":"
               + String.format("%02d", Seconds) + ":"
               + String.format("%03d", MilliSeconds)
            )
            handler.postDelayed(this, 0)
        }
    }
}
