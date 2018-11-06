package com.example.ft_hangouts

import android.widget.Toast
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import com.example.ft_hangouts.Utility.getDateNowStr
import com.example.ft_hangouts.Utility.getSmsDateNow


class SmsBroadcastReceiver : BroadcastReceiver() {

    val sms = SmsManager.getDefault()

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onReceive(context: Context, intent: Intent) {

        // Retrieves a map of extended data from the intent.
        val bundle = intent.extras

        try {

            if (bundle != null) {

                val pdusObj = bundle.get("pdus") as Array<Any>

                var message = ""
                for (i in pdusObj.indices)
                {
                    var currentMessage = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                    message += currentMessage.displayMessageBody
                }
                val date_str = getDateNowStr()
                val date = getSmsDateNow(context, date_str)

                if (Message.Instance.instance != null)
                    Message.Instance.instance!!.add_receveid_msg(date, message)
                Message.Instance.instance!!.addMessageDb(date_str, message, false)
            }
        } catch (e: Exception) {
            Log.e("SmsReceiver", "Exception smsReceiver$e")
        }

    }
}