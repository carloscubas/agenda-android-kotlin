package com.example.agenda

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.media.MediaPlayer

const val SMS_BUNDLE = "pdus"

class SMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val intentExtras = intent.extras
        val subId = intentExtras?.getInt("subscription", -1)
        val sms = intentExtras?.get(SMS_BUNDLE) as Array<Any>
        var smsMessage : SmsMessage? = null

        sms.indices.forEach { i ->
            val format = intentExtras.getString("format")
            smsMessage = SmsMessage.createFromPdu( sms[i] as ByteArray, format )
        }
        if(Application.database?.contatoDao()?.isContato(smsMessage?.originatingAddress.toString()) == 1){
            val mp = MediaPlayer.create(context, R.raw.gol4)
            mp.start()
        }
    }
}
