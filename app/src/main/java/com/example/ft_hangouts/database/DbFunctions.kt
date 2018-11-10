package com.example.ft_hangouts.database

import android.content.Context
import android.widget.Toast
import com.example.ft_hangouts.CreateContact
import com.example.ft_hangouts.Utility.Sms
import com.example.ft_hangouts.Utility.doAsync
import com.example.ft_hangouts.Utility.smsToString
import com.example.ft_hangouts.Utility.stringToSms
import java.lang.Exception

class DbFunctions {
    companion object {
        fun addMessageDb(context: Context, phone_number: String, date: String, msg: String, sent: Boolean)
        {
            doAsync {
                try {
                    val db = AppDatabase.getInstance(context)
                    var user = db.userDao().findByNumber(phone_number)

                    if (user == null && !sent)
                    {
                        user = User(CreateContact.getNewId(db), phone_number, "", phone_number, null, "")
                        db.userDao().insert(user)
                    }

                    var lst_sms: MutableList<Sms> = mutableListOf()
                    if (user.sms.length > 0)
                        lst_sms = stringToSms(user.sms)
                    lst_sms.add(Sms(date, msg, sent))

                    user.sms = smsToString(lst_sms)
                    db.userDao().updateUser(user)

                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }.execute()
        }
    }
}
