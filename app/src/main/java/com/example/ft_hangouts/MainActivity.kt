package com.example.ft_hangouts

import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityOptions
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity;
import android.widget.*
import com.example.ft_hangouts.Utility.doAsync
import com.example.ft_hangouts.database.AppDatabase
import com.example.ft_hangouts.database.User

import kotlinx.android.synthetic.main.activity_main.*
import com.example.ft_hangouts.Utility.getDp
import android.support.constraint.ConstraintSet
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import android.preference.PreferenceManager
import android.content.SharedPreferences



class MainActivity : AppCompatActivity() {

    var bckgnd_time: Date = Calendar.getInstance().time
    var myActivity: Boolean = true

    object getLanguage {
        @JvmStatic var language = "en"
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener {
            this.myActivity = true
            startActivityForResult(Intent(this, CreateContact::class.java),  1/*, ActivityOptions.makeSceneTransitionAnimation(this).toBundle()*/)
        }

        try {
            this.fill_user()
        } catch (e: Exception)
        {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    public override fun onResume() {
        super.onResume()
        if (this.myActivity == false)
        {
            val date = SimpleDateFormat("HH:mm:ss").format(this.bckgnd_time)
            Toast.makeText(applicationContext, date, Toast.LENGTH_SHORT).show()
        }
        else
            this.myActivity = false
    }

    public override fun onPause() {
        super.onPause()
        if (this.myActivity == false)
            this.bckgnd_time = Calendar.getInstance().time
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        val scroll = findViewById<TableLayout>(R.id.main_table)
        scroll.removeAllViews()

        this.fill_user()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun fill_user()
    {
        doAsync {
            try {
                val db = AppDatabase.getInstance(this)
                val users = db.userDao().getAll()

                runOnUiThread {
                    for (item: User in users)
                        add_user(item)
                }
            } catch (e: Exception)
            {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }

        }.execute()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun add_user(user: User)
    {
        val scroll = findViewById<TableLayout>(R.id.main_table)

        val constrain = ConstraintLayout(this)
        constrain.id = View.generateViewId()
        constrain.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, getDp(70))
        constrain.setOnClickListener {
            val intent = Intent(this, UserDetails::class.java)
            intent.putExtra("id", user.id)
            intent.putExtra("firstname", user.firstName)
            intent.putExtra("lastname", user.lastName)
            intent.putExtra("phone", user.phone)
            intent.putExtra("img", user.image)
            this.myActivity = true
            startActivityForResult(intent, 2)
        }

        val txt = TextView(this)
        txt.id = View.generateViewId()
        val txtparam = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
//        txt.setBackgroundColor(Color.BLUE)
        txt.text = user.firstName + " " + user.lastName
        txt.textSize = 30.0f
        txt.layoutParams = txtparam

        constrain.addView(txt)

        val set = ConstraintSet()
//        set.clone(constrain)
        set.connect(txt.id, ConstraintSet.TOP, constrain.id, ConstraintSet.TOP, 32)
        set.connect(txt.id, ConstraintSet.RIGHT, constrain.id, ConstraintSet.RIGHT, 64)
        set.connect(txt.id, ConstraintSet.BOTTOM, constrain.id, ConstraintSet.BOTTOM, 32)
        set.connect(txt.id, ConstraintSet.LEFT, constrain.id, ConstraintSet.LEFT, 64)
        set.applyTo(constrain)

        scroll.addView(constrain)

        val ll = LinearLayout(this)
        ll.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getDp(1))
        ll.setBackgroundColor(Color.GRAY)
        scroll.addView(ll)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         //Handle action bar item clicks here. The action bar will
         //automatically handle clicks on the Home/Up button, so long
         //as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_color -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Pick a color");
                builder.setItems(arrayOf("Blue", "Green")) { _, which ->
                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                builder.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
