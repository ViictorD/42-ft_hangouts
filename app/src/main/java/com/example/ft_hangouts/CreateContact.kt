package com.example.ft_hangouts

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.content.Intent
import android.provider.MediaStore
import android.graphics.Bitmap
import android.app.Activity
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.ft_hangouts.Utility.applyTheme
import com.example.ft_hangouts.Utility.doAsync
import com.example.ft_hangouts.Utility.getDp
import com.example.ft_hangouts.database.AppDatabase
import com.example.ft_hangouts.database.User
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.Exception


class CreateContact : AppCompatActivity() {

    val GET_FROM_GALLERY = 3
    private var id: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme(this, MainActivity.Theme.theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_contact)
        val t = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(t)

        if (intent.getStringExtra("edit") != null)
            supportActionBar!!.title =  resources.getString(R.string.edit_contat)
        else
            supportActionBar!!.title =  resources.getString(R.string.add_contat)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        if (intent.getIntExtra("id", -1) != -1)
        {
            this.id = intent.getIntExtra("id", -1)
            doAsync {
                val db = AppDatabase.getInstance(this)
                val user = db.userDao().findById(this.id)
                findViewById<TextView>(R.id.input_first_name).text = user.firstName
                findViewById<TextView>(R.id.input_last_name).text = user.lastName
                findViewById<TextView>(R.id.input_phone).text = user.phone
                if (user.image != null)
                {
                    val stream = ByteArrayInputStream(user.image)
                    val new_img = BitmapFactory.decodeStream(stream)
                    findViewById<ImageButton>(R.id.image).setImageBitmap(new_img)
                }
            }.execute()
        }
        else if (CreateContact.image != null)
            findViewById<ImageButton>(R.id.image).setImageBitmap(CreateContact.image)

    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing)
            CreateContact.image = null
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun addPicture(@Suppress("UNUSED_PARAMETER") view: View)
    {
        startActivityForResult(Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            val selectedImage = data!!.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                val button = findViewById<ImageButton>(R.id.image)
                val img = findViewById<ImageButton>(R.id.image)

                if (bitmap.width > img.width)
                {
                    var height = bitmap.height / (bitmap.width / img.width)
                    CreateContact.image = Bitmap.createScaledBitmap(bitmap, img.width, height, false)
                }
                else if (bitmap.height > img.height)
                {
                    var width = bitmap.width / (bitmap.height / img.height)
                    CreateContact.image = Bitmap.createScaledBitmap(bitmap, width, img.height, false)
                }
                else
                    CreateContact.image = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, false)
                button.setImageBitmap(CreateContact.image)
            }
            catch (e: Exception)
            {
                CreateContact.image = null
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun saveContact(@Suppress("UNUSED_PARAMETER") view: View)
    {
        val firstname = findViewById<EditText>(R.id.input_first_name).text.toString()
        val lastname = findViewById<EditText>(R.id.input_last_name).text.toString()
        var phone = findViewById<EditText>(R.id.input_phone).text.toString()

        if (firstname.isEmpty() || phone.isEmpty())
        {
            Toast.makeText(applicationContext, resources.getString(R.string.field_missing), Toast.LENGTH_LONG).show()
            return
        }

        if (phone[0] == '0')
            phone = "+33" + phone.substring(1, phone.length)

        var byteArray: ByteArray? = null

        if (CreateContact.image != null)
        {
            val stream = ByteArrayOutputStream()
            CreateContact.image!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            byteArray = stream.toByteArray()
        }

        try {
            doAsync {
                val db = AppDatabase.getInstance(this)
                if (this.id != -1)
                    db.userDao().insert(User(this.id, firstname, lastname, phone, byteArray, ""))
                else
                    db.userDao().insert(User(getNewId(db), firstname, lastname, phone, byteArray, ""))

            }.execute()
        }
        catch (e: Exception)
        {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
        }

        setResult(Activity.RESULT_OK, Intent())
        if (this.id != -1)
            Toast.makeText(applicationContext, resources.getString(R.string.contact_updated), Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(applicationContext, resources.getString(R.string.contact_created), Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        @JvmStatic
        private var image: Bitmap? = null

        fun getNewId(db: AppDatabase): Int
        {
            val users = db.userDao().getAll()
            var max: Int = 0
            for (item in users)
            {
                if (item.id > max)
                    max = item.id
            }
            return ++max
        }
    }
}
