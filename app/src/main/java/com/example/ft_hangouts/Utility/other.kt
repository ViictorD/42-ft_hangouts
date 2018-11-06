package com.example.ft_hangouts.Utility

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.Toast
import com.example.ft_hangouts.MainActivity
import com.example.ft_hangouts.R
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
fun getDateNowStr(): String
{
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val dateTime = LocalDateTime.now()
    return dateTime.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getSmsDateNow(context: Context, strdate: String): String
{
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val current = LocalDateTime.parse(strdate, formatter)
    val month = arrayOf(context.getString(R.string.january),
        context.getString(R.string.february),
        context.getString(R.string.march),
        context.getString(R.string.april),
        context.getString(R.string.may),
        context.getString(R.string.june),
        context.getString(R.string.july),
        context.getString(R.string.august),
        context.getString(R.string.september),
        context.getString(R.string.october),
        context.getString(R.string.november),
        context.getString(R.string.december))

    return current.dayOfMonth.toString() + " " + month[current.monthValue - 1] + ". " +
            context.getString(R.string.at) + " " + current.hour.toString() + ":" + current.minute.toString()
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
