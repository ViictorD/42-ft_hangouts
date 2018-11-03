package com.example.ft_hangouts

import android.widget.Toast
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log


class SmsBroadcastReceiver : BroadcastReceiver() {

    val sms = SmsManager.getDefault()

    override fun onReceive(context: Context, intent: Intent) {

        // Retrieves a map of extended data from the intent.
        val bundle = intent.extras

        try {

            if (bundle != null) {

                val pdusObj = bundle.get("pdus") as Array<Any>

                var phoneNumber = ""
                var message = ""
                for (i in pdusObj.indices)
                {
                    var currentMessage = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                    phoneNumber = currentMessage.displayOriginatingAddress
                    message += currentMessage.displayMessageBody
                } // end for loop
                if (Message.Instance.instance != null)
                    Message.Instance.instance!!.add_receveid_msg(phoneNumber, message)
//                Toast.makeText(context,"senderNum: $phoneNumber, message: $message", Toast.LENGTH_LONG).show()
            } // bundle is null

        } catch (e: Exception) {
            Log.e("SmsReceiver", "Exception smsReceiver$e")
        }

    }
}