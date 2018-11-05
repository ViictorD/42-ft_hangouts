package com.example.ft_hangouts

import android.app.ActivityOptions
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import com.example.ft_hangouts.Utility.doAsync
import com.example.ft_hangouts.database.AppDatabase
import com.example.ft_hangouts.database.User
import kotlinx.android.synthetic.main.activity_user_details.*
import java.io.ByteArrayInputStream
import java.lang.Exception
import java.util.jar.Manifest


class UserDetails : AppCompatActivity() {

    var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val t = findViewById<Toolbar>(R.id.toolbar3)
        setSupportActionBar(t)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "User details" // mettre dans string.xml

        val firstname = intent.getStringExtra("firstname")
        val lastname = intent.getStringExtra("lastname")
        findViewById<TextView>(R.id.firstname).text = firstname
        findViewById<TextView>(R.id.lastname).text = lastname
        findViewById<TextView>(R.id.phone_number).text = intent.getStringExtra("phone")

        val img = intent.getByteArrayExtra("img")
        if (img != null)
        {
            val stream = ByteArrayInputStream(img)
            val new_img = BitmapFactory.decodeStream(stream)
            findViewById<ImageButton>(R.id.image).setImageBitmap(new_img)
        }
        this.id = intent.getIntExtra("id", 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        if (requestCode == 1)
        {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
            {
                val nb = findViewById<TextView>(R.id.phone_number)
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:${nb.text}")
                startActivity(intent)
            }
            else
                Toast.makeText(applicationContext, "Need permission to call", Toast.LENGTH_LONG).show() // mettre dans string.xml
            return
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun goCall(view: View)
    {
        if (ContextCompat.checkSelfPermission(applicationContext, "android.permission.CALL_PHONE") != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf("android.permission.CALL_PHONE"), 1)
        else
        {
            val intent = Intent(Intent.ACTION_CALL)
            val nb = findViewById<TextView>(R.id.phone_number)
            intent.data = Uri.parse("tel:${nb.text}")
            startActivity(intent)

        }
    }

    fun goDelete(view: View)
    {
        doAsync {
            val db = AppDatabase.getInstance(this)
            val user = db.userDao().findById(this.id)
            db.userDao().delete(user)
            runOnUiThread {
                Toast.makeText(applicationContext, "User deleted", Toast.LENGTH_LONG).show() // mettre dans string.xml
            }
            finish()
        }.execute()
    }

    fun goEdit(view: View)
    {
        val intent = Intent(this, CreateContact::class.java)
        intent.putExtra("id", this.id)
        startActivityForResult(intent, 1)
        // sur on receive on refresh les datas
    }

    fun goSms(view: View)
    {
        val intent = Intent(this, Message::class.java)
        intent.putExtra("phone", findViewById<TextView>(R.id.phone_number).text)
        intent.putExtra("id", this.id)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == 1)
        {
            doAsync {
                val db = AppDatabase.getInstance(this)
                val user = db.userDao().findById(this.id)
                runOnUiThread {
                    findViewById<TextView>(R.id.firstname).text = user.firstName
                    findViewById<TextView>(R.id.lastname).text = user.lastName
                    findViewById<TextView>(R.id.phone_number).text = user.phone
                    if (user.image != null)
                    {
                        val stream = ByteArrayInputStream(user.image)
                        val new_img = BitmapFactory.decodeStream(stream)
                        findViewById<ImageButton>(R.id.image).setImageBitmap(new_img)
                    }
                }
            }.execute()
        }
    }
}
