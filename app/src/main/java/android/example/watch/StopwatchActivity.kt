package android.example.watch

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
    var MillisecondTime = 0L
    var UpdateTime = 0L
    var handler: Handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stopwatch)
        setSupportActionBar(toolbar)
        UtilButtons.updateButtonsStopwatch(stopwatchState, fab_start, fab_restart, fab_timer, this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#000033")//цвет статусбара
        }
        //TODO:в основном экране сделать текущее время (мб сделать добавление времени других стран)
        fab_start.setOnClickListener { view ->
            //startStopwatch()
            functionalStartButton()
            UtilButtons.updateButtonsStopwatch(stopwatchState, fab_start, fab_restart, fab_timer, this)
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        fab_timer.setOnClickListener { view ->
            //pauseStopwatch()
            saveTimer()
            UtilButtons.updateButtonsStopwatch(stopwatchState, fab_start, fab_restart, fab_timer, this)
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        fab_restart.setOnClickListener { view ->
            restartStopwatch()
            UtilButtons.updateButtonsStopwatch(stopwatchState, fab_start, fab_restart, fab_timer, this)
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        printSavedTimer()
        //printInTV(tvStopwatch, UtilStopwatch.getCurrentTime(this))
    }

    override fun onPause() {
        super.onPause()
        UtilStopwatch.setCurrentTime(UpdateTime, this)
        handler.removeCallbacks(runnable)
        println((
                "" + (UpdateTime/ 1000) / 60 + ":"
                        + String.format("%02d", (UpdateTime / 1000) % 60) + ":"
                        + String.format("%03d", UpdateTime % 1000)
                ))
    }
    enum class StopwatchState{ Running, Paused}
    private fun startStopwatch(){
        stopwatchState = StopwatchState.Running
        StartTime = SystemClock.uptimeMillis()
        handler.postDelayed(runnable, 0)

    }
    private fun pauseStopwatch(){
        stopwatchState = StopwatchState.Paused
        UtilStopwatch.setTimeBuff((UtilStopwatch.getTimeBuff(this) + MillisecondTime), this)
        //TimeBuff += MillisecondTime
        handler.removeCallbacks(runnable)
    }
    private fun restartStopwatch(){
        stopwatchState = StopwatchState.Paused
        StartTime = 0
        UtilStopwatch.setTimeBuff(0, this)
        UtilStopwatch.setCurrentTime(0, this)
        UpdateTime = 0
        //TimeBuff = 0
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
        var tempMillisecondTime = 0L
        MillisecondTime = SystemClock.uptimeMillis() - StartTime
        //UpdateTime = TimeBuff + MillisecondTime
        UpdateTime = UtilStopwatch.getTimeBuff(this) + MillisecondTime
        UtilStopwatch.setNewTimerInList(UpdateTime)
        val layoutInflater : LayoutInflater = LayoutInflater.from(applicationContext)
        val view : View = layoutInflater.inflate(R.layout.inflater_timer_stopwatch, parentLayout, false)
        view.findViewById<TextView>(R.id.tvIntervalStopwatch).text = "Interval " + UtilStopwatch.getListTimerLength(applicationContext)
        printInTV(view.findViewById<TextView>(R.id.tvTimerStopwatch), UpdateTime)
        if(UtilStopwatch.getListTimerLength(applicationContext) > 1 &&
            (UtilStopwatch.getTimerFromList(UtilStopwatch.getListTimerLength(applicationContext) - 1) > (UtilStopwatch
                .getTimerFromList(UtilStopwatch.getListTimerLength(applicationContext) - 2)))){
            //вторая проверка - защита от отрицательных значений после restart timer
            tempMillisecondTime = UtilStopwatch.getTimerFromList(UtilStopwatch.getListTimerLength(applicationContext) - 2)
            tempMillisecondTime = UpdateTime - tempMillisecondTime
        }
        printInTV( view.findViewById<TextView>(R.id.tvDurationStopwatch), tempMillisecondTime)
        parentLayout.addView(view)
    }
    private fun printSavedTimer(){
        if(UtilStopwatch.getCurrentTime(this) > 0){
            UpdateTime = UtilStopwatch.getCurrentTime(applicationContext) + MillisecondTime
            printInTV(tvStopwatch, UpdateTime)
            UtilStopwatch.setTimeBuff(UpdateTime, this)//чтобы включить таймер с сохраненного времени
        }
    }
    private fun printInTV(textView: TextView, time: Long){
        textView.text = (
                "" + (time / 1000) / 60 + ":"
                        + String.format("%02d", (time / 1000) % 60) + ":"
                        + String.format("%03d", time % 1000)
                )
    }
    var runnable:Runnable = object:Runnable {
         var Seconds = 0L
         var Minutes = 0L
         var MilliSeconds = 0L
        override fun run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime
            //UpdateTime = TimeBuff + MillisecondTime
            UpdateTime = UtilStopwatch.getTimeBuff(applicationContext) + MillisecondTime
            Seconds = (UpdateTime / 1000)
            Minutes = Seconds / 60
            Seconds = Seconds % 60
            MilliSeconds = (UpdateTime % 1000)
            printInTV(tvStopwatch, UpdateTime)
            //UtilStopwatch.setCurrentTime(UpdateTime, applicationContext)
            handler.postDelayed(this, 0)
        }
    }
}
