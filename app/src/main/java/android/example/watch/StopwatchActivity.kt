package android.example.watch

import android.graphics.Color
import android.os.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_stopwatch.*
import kotlinx.android.synthetic.main.content_stopwatch.*
import androidx.core.os.HandlerCompat.postDelayed


class StopwatchActivity : AppCompatActivity() {
    private lateinit var timer: CountDownTimer
    var stopwatchState = StopwatchState.Paused
    var StartTime = 0L
    var TimeBuff = 0L
    var MillisecondTime = 0L
    var handler: Handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stopwatch)
        setSupportActionBar(toolbar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#4747d1")//цвет статусбара
        }
        //TODO: соединить start и pause в одну кнопку
        //TODO: сделать кнопку сохранения времени и функционал для нее
        //TODO: сделать класс UtilStopwatch и хранить там время (мб timebuff)
        fab_start.setOnClickListener { view ->
            startStopwatch()
            updateButtonsUI()
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        fab_pause.setOnClickListener { view ->
            pauseStopwatch()
            updateButtonsUI()
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        fab_restart.setOnClickListener { view ->
            restartStopwatch()
            updateButtonsUI()
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
    private fun updateButtonsUI(){
        when(stopwatchState){
            StopwatchState.Running -> {
                fab_start.isEnabled = false
                fab_restart.isEnabled = true
                fab_pause.isEnabled = true
            }
            StopwatchState.Paused -> {
                fab_start.isEnabled = true
                fab_restart.isEnabled = true
                fab_pause.isEnabled = false
            }
        }
    }
    var runnable:Runnable = object:Runnable {
         var UpdateTime = 0L
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
