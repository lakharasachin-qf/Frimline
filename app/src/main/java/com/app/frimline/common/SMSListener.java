package com.app.frimline.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.app.frimline.intefaces.Common;

public class SMSListener extends BroadcastReceiver {

    private static Common.OTPListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {


        Bundle data = intent.getExtras();

        Object[] pdus = new Object[0];
        if (data != null) {
            pdus = (Object[]) data.get("pdus");
        }

        if (pdus != null) {
            for (Object pdu : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                if (mListener != null)
                    mListener.onOTPReceived(HELPER.getConsecutiveOTP(smsMessage.getMessageBody()));
                break;
            }
        }
    }

    public static void bindListener(Common.OTPListener listener) {
        mListener = listener;
    }

    public static void unbindListener() {
        mListener = null;
    }
}
