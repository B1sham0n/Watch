package android.example.watch

import android.os.Bundle
import android.os.CountDownTimer
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.content_timer.*

class TimerActivity : AppCompatActivity() {
    private lateinit var timer: CountDownTimer
    private var timerState = TimerState.Stopped
    var secondsRemaining: Long = 0
    private var maxSeconds: Long = 65
    private var secondsNow: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setSupportActionBar(toolbar)
        //TODO: progressbar.progress = максимальная длина - (время таймера - текущее время)
        //пример: = 60 - (60 - 2) = 2; 60 - (60 - 3) = 3 -> текущее время = progressbar.progress
        fab_start.setOnClickListener { view ->
            startTimer()
            Snackbar.make(view, "Timer started", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        fab_stop.setOnClickListener { view ->
            stopTimer()
            Snackbar.make(view, "Timer stopped", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        fab_pause.setOnClickListener { view ->
            pauseTimer()
            Snackbar.make(view, "Timer paused", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        Util.setMaxSecondsForTimer(maxSeconds, this)//потом нужно заменить на get и делать set при выборе времени
        Util.setTimeForTimer(Util.getMaxSecondsForTimer(this), this)
        progressBarTimer.max = maxSeconds.toInt()
        progressBarTimer.progress = 0
    }
    enum class TimerState{
        Stopped, Paused, Running
    }
    private fun startTimer(){
        timerState = TimerState.Running
        secondsRemaining = Util.getTimeForTimer(this)
        println("its" + Util.getTimeForTimer(this))

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000){
            override fun onFinish() = onTimerFinished()

            override fun onTick(p0: Long) {
                secondsRemaining = p0 / 1000
                secondsNow++
                updateCountdownUI()
            }
        }.start()
    }
    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        tvTimerTime.text = "$minutesUntilFinished:${
        if(secondsStr.length == 2) secondsStr
        else "0" + secondsStr}"
        progressBarTimer.progress = secondsNow.toInt()
    }
    private fun onTimerFinished(){
        timerState = TimerState.Stopped

        //setNewTimerLength()

        progressBarTimer.progress = 0

        //PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = maxSeconds

        //updateButtons()
        updateCountdownUI()
    }
    private fun pauseTimer(){
        timerState = TimerState.Paused
        Util.setTimeForTimer(secondsRemaining, this)
        timer.cancel()
    }
    private fun stopTimer(){
        timerState = TimerState.Stopped
        timer.cancel()
        Util.setTimeForTimer(maxSeconds, this)
        secondsRemaining = maxSeconds
        updateCountdownUI()
    }
}
