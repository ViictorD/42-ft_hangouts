package com.example.ft_hangouts

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import java.lang.Exception
import android.annotation.TargetApi
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.telephony.SmsManager
import android.util.Log
import android.view.Gravity
import android.widget.*
import com.example.ft_hangouts.R.string.user
import com.example.ft_hangouts.Utility.*
import com.example.ft_hangouts.database.AppDatabase
import kotlinx.android.synthetic.main.activity_message.*
import android.view.View.OnFocusChangeListener



class Message : AppCompatActivity() {

    var phone_number: String? = null
    var user_id: Int = -1

    object Instance {
        var instance: Message? = null
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Message"
        this.phone_number = intent.getStringExtra("phone")
        this.user_id = intent.getIntExtra("id", -1)
        Message.Instance.instance = this

        if (ContextCompat.checkSelfPermission(applicationContext, "android.permission.RECEIVE_SMS") != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf("android.permission.RECEIVE_SMS"), 2)

        this.loadSms()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun goSendSms(view: View)
    {
        val txt = findViewById<TextView>(R.id.message)
        if (txt.text.isNotEmpty() && this.phone_number != null)
        {
            if (ContextCompat.checkSelfPermission(applicationContext, "android.permission.SEND_SMS") != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, arrayOf("android.permission.SEND_SMS"), 1)
            else
                this.action_send()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun action_send()
    {
        val sms = SmsManager.getDefault()
        val date = getSmsDateNow()
        val message = findViewById<TextView>(R.id.message).text.toString()
        try {
            if (findViewById<TextView>(R.id.message).text.length > 160)
                sms.sendMultipartTextMessage(this.phone_number, null, sms.divideMessage(message), null, null)
            else
                sms.sendTextMessage(this.phone_number, null, message, null, null)
            this.add_send_msg(date, message)
            findViewById<TextView>(R.id.message).text = ""
            addMessageDb(date, message, true)
        } catch (e: Exception)
        {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        if (requestCode == 1)
        {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
               this.action_send()
            else
                Toast.makeText(applicationContext, "Need permission to send sms", Toast.LENGTH_LONG).show() // mettre dans string.xml
            return
        }
        else if (requestCode == 2 && grantResults[0] == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf("android.permission.RECEIVE_SMS"), 2)
    }
    
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun add_receveid_msg(strdate: String, msg: String)
    {
        val table = findViewById<TableLayout>(R.id.message_table)

        val linear1 = LinearLayout(this)
        linear1.id = View.generateViewId()
        linear1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        linear1.gravity = Gravity.START
        linear1.setPadding(16, 8, 16, 8)

        val shape = GradientDrawable()
        shape.setColor(Color.parseColor("#FF008577"))
        shape.setStroke(0, Color.parseColor("#C4CDE0"))
        shape.cornerRadius = getDp(15).toFloat()

        val linear2 = LinearLayout(this)
        linear2.id - View.generateViewId()
        linear2.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        linear2.background = shape
        linear2.orientation = LinearLayout.VERTICAL
        linear2.setPadding(getDp(16), getDp(8), getDp(16), getDp(8))

        val message = TextView(this)
        message.id = View.generateViewId()
        message.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        message.textSize = 17.0f
        message.maxWidth = getDp(320)
        message.text = msg
        message.setTextColor(Color.WHITE)

        val date = TextView(this)
        date.id = View.generateViewId()
        date.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        date.textSize = 9.0f
        date.text = strdate
        date.setTextColor(Color.WHITE)

        runOnUiThread {
            linear2.addView(message)
            linear2.addView(date)

            linear1.addView(linear2)

            table.addView(linear1)
            this.scrollDown(View(this))
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun add_send_msg(strdate: String, msg: String)
    {
        val table = findViewById<TableLayout>(R.id.message_table)

        val linear1 = LinearLayout(this)
        linear1.id = View.generateViewId()
        linear1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        linear1.gravity = Gravity.END
        linear1.setPadding(16, 8, 16, 8)

        val shape = GradientDrawable()
        shape.setColor(Color.parseColor("#FFE0E0E0"))
        shape.setStroke(0, Color.parseColor("#C4CDE0"))
        shape.cornerRadius = getDp(15).toFloat()

        val linear2 = LinearLayout(this)
        linear2.id - View.generateViewId()
        linear2.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        linear2.background = shape
        linear2.orientation = LinearLayout.VERTICAL
        linear2.setPadding(getDp(16), getDp(8), getDp(16), getDp(8))

        val message = TextView(this)
        message.id = View.generateViewId()
        message.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        message.textSize = 17.0f
        message.maxWidth = getDp(320)
        message.text = msg

        val date = TextView(this)
        date.id = View.generateViewId()
        date.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        date.textSize = 9.0f
        date.text = strdate

        runOnUiThread {
            linear2.addView(message)
            linear2.addView(date)

            linear1.addView(linear2)

            table.addView(linear1)
            this.scrollDown(View(this))
        }
    }

    fun scrollDown(view: View)
    {
        val scrollView = findViewById<ScrollView>(R.id.scroll_sms)

        scrollView.post {
            scrollView.scrollTo(0, scrollView.getBottom())
        }
    }

    fun addMessageDb(date: String, msg: String, sent: Boolean)
    {
        doAsync {
            try {
                val db = AppDatabase.getInstance(this)
                val user = db.userDao().findById(this.user_id)
                var lst_sms: MutableList<Sms> = mutableListOf()
                if (user.sms.length > 0)
                    lst_sms = stringToSms(user.sms)
                lst_sms.add(Sms(date, msg, sent))
                user.sms = smsToString(lst_sms)
                db.userDao().updateUser(user)

            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }.execute()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun loadSms()
    {
        doAsync {
            try {
                val db = AppDatabase.getInstance(this)
                val user = db.userDao().findById(this.user_id)
                if (user.sms.length > 0)
                {
                    val lst_sms = stringToSms(user.sms)
                    for (item in lst_sms)
                    {
                        if (item.sent)
                            this.add_send_msg(item.date, item.message)
                        else
                            this.add_receveid_msg(item.date, item.message)
                    }
                }
            } catch (e: Exception)
            {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }.execute()
    }
}
