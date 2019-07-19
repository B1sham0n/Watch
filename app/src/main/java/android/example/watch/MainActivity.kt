package android.example.watch

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_stopwatch.*
import kotlinx.android.synthetic.main.content_timer.*
import java.util.*
import android.widget.ImageButton
import androidx.core.os.postDelayed
import android.content.Context.LAYOUT_INFLATER_SERVICE
import androidx.core.content.ContextCompat.getSystemService
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val handler: Handler = Handler()
    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            showCreateCategoryDialog()
            //TODO: ОБЯЗАТЕЛЬНО добавить БД, в которой будет храниться выбранное GMT, и засунуть ее в UtilWatch
            //TODO: в онпауз удалять поток с обновлением
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        //navView.setCheckedItem(R.id.nav_timer) выбор по умолчанию
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        handler.postDelayed(runnable, 0)
    }
    private fun createTimeGMT(nameCity: String, timeZoneString: String){
        val layoutInflater : LayoutInflater = LayoutInflater.from(applicationContext)
        val view : View = layoutInflater.inflate(R.layout.inflater_timezone_main, parentLayoutWatchGMT, false)
        val tz = TimeZone.getTimeZone(timeZoneString)
        val c = Calendar.getInstance(tz)
        var time = String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+
                String.format("%02d" , c.get(Calendar.MINUTE))+":"+
                String.format("%03d" , c.get(Calendar.SECOND))
        view.findViewById<TextView>(R.id.tvTimeGMT).text = time
        view.findViewById<TextView>(R.id.tvTimeGMT).tag = timeZoneString
        view.findViewById<TextView>(R.id.tvNameGMT).text = nameCity
        view.findViewById<Button>(R.id.btnCancelGMT).setOnClickListener(btnCancelListener)
        view.findViewById<Button>(R.id.btnCancelGMT).tag = "cancel"
        view.findViewById<Button>(R.id.btnCancelGMT).id = parentLayoutWatchGMT.childCount
        //handler.postDelayed(runnable,0)
        parentLayoutWatchGMT.addView(view)
        //println(view.findViewById<TextView>(R.id.tvTimeGMT).tag.toString()  + "here")
        myRun.myRun(parentLayoutWatchGMT)
        var runn  = myRun.myRun(parentLayoutWatchGMT)
        handler.postDelayed(runn.run {myRun}, 0)
    }
    class myRun  {
        companion object : Runnable {
            var handler: Handler = Handler()
            lateinit var view : LinearLayout
            public fun myRun(view1: LinearLayout){
                this.view = view1
            }
            override fun run() {
                var i = 0
                while(i < view.childCount){
                    val child = view.getChildAt(i)
                    val tz = TimeZone.getTimeZone(child.findViewById<TextView>(R.id.tvTimeGMT).tag.toString())
                    val c = Calendar.getInstance(tz)
                    var time = String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+
                            String.format("%02d" , c.get(Calendar.MINUTE))
                    child.findViewById<TextView>(R.id.tvTimeGMT).text = time
                    i++
                }
                handler.postDelayed(this, 0)
                //TODO как передать переменную?
            }
        }
    }
    var runnable: Runnable =  object:Runnable {
        var tz: TimeZone? = TimeZone.getDefault()
        override fun run() {
            val c = Calendar.getInstance(tz)
            var time = String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+
                    String.format("%02d" , c.get(Calendar.MINUTE))
            tvTimeGMT.text = time
            handler.postDelayed(this, 0)
        }
    }
    private val btnCancelListener = View.OnClickListener { view ->
        parentLayoutWatchGMT.removeViewAt(view.id)
        updateIdButton()
    }
    private fun updateIdButton() {
        var v: View? = null
        var btn: Button?
        for (i in 0 until parentLayoutWatchGMT.childCount) {
            v = parentLayoutWatchGMT.getChildAt(i)
            btn = v.findViewWithTag("cancel")
            if (btn != null) {
                btn.id = i
            }
        }
    }
    private fun showCreateCategoryDialog() {
        val context = this
        val builder = AlertDialog.Builder(context)
        var GMT = "0"
        builder.setTitle("Choose a city:")

        // https://stackoverflow.com/questions/10695103/creating-custom-alertdialog-what-is-the-root-view
        // Seems ok to inflate view with null rootView
        val view = layoutInflater.inflate(R.layout.dialog_watch, null)
        //settings for numberpickers
        val arrayString = arrayOf("Moscow GMT+3","Paris GMT+2","London GMT+1","Reykjavik GMT","Washington GMT-4")
        view.findViewById<NumberPicker>(R.id.npGMT).minValue = 0
        view.findViewById<NumberPicker>(R.id.npGMT).maxValue = arrayString.size - 1
        view.findViewById<NumberPicker>(R.id.npGMT).displayedValues = arrayString
        builder.setView(view)

        // set up the ok button
        builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
            val str = view.findViewById<NumberPicker>(R.id.npGMT).value// + ":" +
                    //view.findViewById<NumberPicker>(R.id.npMinuteGMT).value.toString()
            val nameAndGMT = arrayString.get(str)
            val nameAndGMTArray = nameAndGMT.split(" ")//[0] - city name, [1] - city GMT
            createTimeGMT(nameAndGMTArray[0], nameAndGMTArray[1])
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.cancel()
        }
        builder.show()
    }
    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("ShowToast")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_timer -> {
                val intent = Intent(this, TimerActivity::class.java)
                startActivity(intent)
                // Handle the camera action
                Toast.makeText(this, "its timer", Toast.LENGTH_LONG)
                println("its timer")
            }
            R.id.nav_stopwatch -> {
                val intent = Intent(this, StopwatchActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "its stopwatch", Toast.LENGTH_LONG)
                println("its stopwatch")
            }
            R.id.nav_about -> {
                Toast.makeText(this, "its about", Toast.LENGTH_LONG)
                println("its about")
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}