package twerktitans.medagenda;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by WW on 4/27/2016.
 */
public class NotificationMaker extends BroadcastReceiver
{
  @Override
  public void onReceive(Context context, Intent intent)
  {
    // Make and send notification here
    int notificationId = 101;
    // Build intent for notification content
    Intent viewIntent = new Intent(context, MainActivity.class);
    PendingIntent viewPendingIntent =
      PendingIntent.getActivity(context, 0, viewIntent, 0);

    Bundle b = intent.getExtras();
    String patientName = b.getString("PATIENT");
    String taskDetails = b.getString("TASK");

    //Building notification layout
    NotificationCompat.Builder notificationBuilder =
      new NotificationCompat.Builder(context)
        .setSmallIcon(R.drawable.logo)
        .setContentTitle("MedAgenda")
        .setContentText(patientName + " needs you to \"" + taskDetails + "\" soon!")
        .setContentIntent(viewPendingIntent);

    // instance of the NotificationManager service
    NotificationManagerCompat notificationManager =
      NotificationManagerCompat.from(context);

    // Build the notification and notify it using notification manager.
    notificationManager.notify(notificationId, notificationBuilder.build());
  }

  static void makeAlarm(Context context, Patient p, Task t)
  {
    //Should only make an alarm if the task time exists / is in the future
    if (t.time == null) {
      return;
    }
    else if (t.time.compareTo(Calendar.getInstance()) < 0) {
      return;
    }

    //activate about 2.5 minutes before
    Long time = t.time.getTimeInMillis() - 150 * 1000;
    Intent intentAlarm = new Intent(context, NotificationMaker.class);
    intentAlarm.putExtra("PATIENT", p.getName());
    intentAlarm.putExtra("TASK", t.details);

    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(context, 1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
  }
}