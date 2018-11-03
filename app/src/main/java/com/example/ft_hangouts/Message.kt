package com.example.ft_hangouts

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.lang.Exception
import android.R.attr.phoneNumber
import android.telephony.SmsManager
import java.util.ArrayList


class Message : AppCompatActivity() {

    var phone_number: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Message"
        this.phone_number = intent.getStringExtra("phone")
        Message.Instance.instance = this

        if (ContextCompat.checkSelfPermission(applicationContext, "android.permission.RECEIVE_SMS") != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf("android.permission.RECEIVE_SMS"), 2)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

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

    fun action_send()
    {
        val sms = SmsManager.getDefault()
        if (findViewById<TextView>(R.id.message).text.length > 160)
        {
            try {
                sms.sendMultipartTextMessage(this.phone_number, null, sms.divideMessage(findViewById<TextView>(R.id.message).text.toString()), null, null)
            } catch (e: Exception)
            {
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
        else
        {
            sms.sendTextMessage(this.phone_number, null, findViewById<TextView>(R.id.message).text.toString(), null, null)
            findViewById<TextView>(R.id.message).text = ""
        }

    }

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
    
    fun add_receveid_msg(nb: String, msg: String)
    {
        Toast.makeText(applicationContext, "$nb: $msg", Toast.LENGTH_LONG).show() // mettre dans string.xml

    }

    fun add_send_msg(nb: String, msg: String)
    {

    }

    object Instance {
        @JvmStatic var instance: Message? = null
    }
}
