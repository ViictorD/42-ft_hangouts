package com.example.ft_hangouts

import android.app.Service
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat.getExtras
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.IBinder
import android.support.annotation.Nullable


//class SmsProcessService : Service() {
//    internal var smsReceiver = SmsReceiver()
//
//    @Nullable
//    fun onBind(intent: Intent): IBinder? {
//        return null
//    }
//
//    fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        registerReceiver(smsReceiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
//
//        return START_STICKY
//    }
//
//    private inner class SmsReceiver : BroadcastReceiver() {
//        fun onReceive(context: Context, intent: Intent) {
//            var telnr = ""
//            var nachricht = ""
//
//            val extras = intent.extras
//
//            if (extras != null) {
//                val pdus = extras.get("pdus") as Array<Any>
//                if (pdus != null) {
//
//                    for (pdu in pdus) {
//                        val smsMessage = getIncomingMessage(pdu, extras)
//                        telnr = smsMessage.getDisplayOriginatingAddress()
//                        nachricht += smsMessage.getDisplayMessageBody()
//                    }
//
//                    if (Message.Instance.instance != null)
//                        Message.Instance.instance!!.add_receveid_msg(date, message)
//                    Message.Instance.instance!!.addMessageDb(date_str, message, false)
//                }
//            }
//        }
//
//        private fun getIncomingMessage(`object`: Any, bundle: Bundle): SmsMessage {
//            val smsMessage: SmsMessage
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                val format = bundle.getString("format")
//                smsMessage = SmsMessage.createFromPdu(`object` as ByteArray, format)
//            } else {
//                smsMessage = SmsMessage.createFromPdu(`object` as ByteArray)
//            }
//
//            return smsMessage
//        }
//    }
//}