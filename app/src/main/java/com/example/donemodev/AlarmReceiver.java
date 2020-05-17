package com.example.donemodev;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int index = intent.getIntExtra("index", -1);

        if (index == -1) return;

        EventList.readData(context);
        EventList eventList = EventList.getInstance();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "123456")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(eventList.data.get(index).name)
                .setContentText(eventList.data.get(index).description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(index, builder.build());

        if (eventList.data.get(index).frequencyId == 0) return;

        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(context.getApplicationContext().ALARM_SERVICE);
        Intent newIntent = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), index, newIntent, 0);

        long[] frequencyList = {
                0,
                1000 * 60,
                1000 * 60 * 2,
                1000 * 60 * 3,
                1000 * 60 * 5,
                1000 * 60 * 10,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                1000 * 60 * 20,
                AlarmManager.INTERVAL_HALF_HOUR,
                AlarmManager.INTERVAL_HOUR,
                AlarmManager.INTERVAL_HOUR * 2,
                AlarmManager.INTERVAL_HOUR * 8,
                AlarmManager.INTERVAL_HALF_DAY,
                AlarmManager.INTERVAL_DAY,
                AlarmManager.INTERVAL_DAY * Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_WEEK),
                AlarmManager.INTERVAL_DAY * Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH),
                AlarmManager.INTERVAL_DAY * Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_YEAR)
        };

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + frequencyList[eventList.data.get(index).frequencyId], pendingIntent);
    }
}
