package android.example.watch
import android.example.watch.Utils.UtilButtons
import android.example.watch.Utils.UtilTimer
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.content_timer.*

class TimerActivity : AppCompatActivity() {
    private lateinit var timer: CountDownTimer
    private var timerState = TimerState.Stopped
    var secondsRemaining: Long = 65
    private var maxSeconds: Long = 65
    private var secondsNow: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setSupportActionBar(toolbar)
        //TODO: возможно, нужно сделать отдельный UtilButtons для обновления цветов (и мб enable) кнопок
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#000033")//цвет статусбара
        }
        fab_start.setOnClickListener { view ->
            startTimer()
            UtilButtons.updateButtonsTimer(timerState, fab_add, fab_start, fab_pause, fab_stop, this)
            //Snackbar.make(view, "Timer started", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        fab_stop.setOnClickListener { view ->
            stopTimer()
            UtilButtons.updateButtonsTimer(timerState, fab_add, fab_start, fab_pause, fab_stop, this)
            //Snackbar.make(view, "Timer stopped", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        fab_pause.setOnClickListener { view ->
            pauseTimer()
            UtilButtons.updateButtonsTimer(timerState, fab_add, fab_start, fab_pause, fab_stop, this)
            //Snackbar.make(view, "Timer paused", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        fab_add.setOnClickListener { view ->
            showCreateCategoryDialog()
        }
        UtilTimer.setMaxSecondsForTimer(maxSeconds, this)//потом нужно заменить на get и делать set при выборе времени
        UtilTimer.setTimeForTimer(UtilTimer.getMaxSecondsForTimer(this), this)
        progressBarTimer.max = maxSeconds.toInt()
        progressBarTimer.progress = 0

        updateCountdownUI()
        UtilButtons.updateButtonsTimer(timerState, fab_add, fab_start, fab_pause, fab_stop, this)
    }
    enum class TimerState{
        Stopped, Paused, Running
    }
    private fun startTimer(){
        timerState = TimerState.Running
        secondsRemaining = UtilTimer.getTimeForTimer(this)
        println("its" + UtilTimer.getTimeForTimer(this))

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
        secondsNow = 0
        progressBarTimer.max = UtilTimer.getMaxSecondsForTimer(this).toInt()
        //PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = maxSeconds

        //updateButtons()
        updateCountdownUI()
    }
    private fun pauseTimer(){
        timerState = TimerState.Paused
        UtilTimer.setTimeForTimer(secondsRemaining, this)
        timer.cancel()
    }
    private fun stopTimer(){
        secondsNow = 0
        progressBarTimer.progress = 0
        timerState = TimerState.Stopped
        timer.cancel()
        UtilTimer.setTimeForTimer(maxSeconds, this)
        secondsRemaining = maxSeconds
        updateCountdownUI()
    }
    private fun showCreateCategoryDialog() {
        val context = this
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Min:sec")

        // https://stackoverflow.com/questions/10695103/creating-custom-alertdialog-what-is-the-root-view
        // Seems ok to inflate view with null rootView
        val view = layoutInflater.inflate(R.layout.dialog_timer, null)

        //val categoryEditText = view.findViewById(R.id.etTime) as EditText
        //settings for numberpickers
        view.findViewById<NumberPicker>(R.id.npMinute).minValue = 0
        view.findViewById<NumberPicker>(R.id.npMinute).maxValue = 100
        view.findViewById<NumberPicker>(R.id.npMinute).value = 1
        view.findViewById<NumberPicker>(R.id.npSecond).minValue = 0
        view.findViewById<NumberPicker>(R.id.npSecond).maxValue = 59
        view.findViewById<NumberPicker>(R.id.npSecond).value = 10

        builder.setView(view)

        // set up the ok button
        builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
            /*UtilTimer.setMaxSecondsForTimer(categoryEditText.text.toString().toLong(), context)
            UtilTimer.setTimeForTimer(categoryEditText.text.toString().toLong(), context)*/
            val time: Long =  view.findViewById<NumberPicker>(R.id.npMinute).value * 60L + view.findViewById<NumberPicker>(R.id.npSecond).value
            UtilTimer.setMaxSecondsForTimer(time, context)
            UtilTimer.setTimeForTimer(time, context)
            maxSeconds = UtilTimer.getMaxSecondsForTimer(context)
            secondsRemaining = maxSeconds
            progressBarTimer.max = maxSeconds.toInt()
            progressBarTimer.progress = 0
            updateCountdownUI()
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.cancel()
        }

        builder.show()
    }
}