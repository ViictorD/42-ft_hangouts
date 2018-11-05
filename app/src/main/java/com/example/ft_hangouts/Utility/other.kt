package com.example.ft_hangouts.Utility

import android.content.res.Resources
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.Toast
import com.example.ft_hangouts.MainActivity
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Collections.emptyList



fun getDp(dp: Int): Int
{
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

@RequiresApi(Build.VERSION_CODES.O)
fun getSmsDateNow(): String
{
    val current = LocalDateTime.now()

    val month_fr = arrayOf("JAN", "FEV", "MAR", "AVR", "MAI", "JUN", "JUL", "AOU", "SEP", "OCT", "NOV", "DEC")
    val month_en = arrayOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")

    if (MainActivity.getLanguage.language == "fr")
    {
        return current.dayOfMonth.toString() + " " + month_fr[current.monthValue - 1] + ". À " +
                current.hour.toString() + ":" + current.minute.toString()
    }
    else
    {
        return current.dayOfMonth.toString() + " " + month_en[current.monthValue - 1] + ". AT " +
                current.hour.toString() + ":" + current.minute.toString()
    }
}

fun stringToSms(data: String?): MutableList<Sms>
{
    if (data == null)
        return Collections.emptyList()
    return Gson().fromJson(data, Array<Sms>::class.java).toList().toMutableList()
}

fun smsToString(someObjects: MutableList<Sms>): String
{
    return Gson().toJson(someObjects)
}
