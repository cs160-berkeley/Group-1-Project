package twerktitans.medagenda;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jefftan on 4/27/16.
 */
public class PostInfo {

    // New Patient variables.
    private String firstName, lastName, room_num, dob;
    private String type;
    private Context context;

    // New Task and Status variables
    private String details, option,time_expire;
    private int index, repeat_time, color, patient_id;
    private Calendar time_due;
    Activity activity;


    // For new patient
    public PostInfo(Context context, String firstName, String lastName,
                    String room_num, String dob) {
        activity = (Activity) context;
        this.firstName = firstName;
        this.lastName = lastName;
        this.room_num = room_num;
        this.dob = dob;
        this.type="newPatient";
        this.option = "";
    }

    // To add a new task
    public PostInfo(Context context, int index, int patient_id, String details,
                    Calendar time_due, int color, int repeat_time) {
        activity = (Activity) context;
        this.index = index;
        this.details = details;
        this.patient_id = patient_id;
        this.time_due = time_due;
        this.type="newTask";
        this.color = color;
        this.repeat_time = repeat_time;
    }

    // To add a new status
    public PostInfo(Context context, int index, int patient_id, String details,
                    Calendar time_expire) {
        activity = (Activity) context;
        this.index = index;
        this.type="newStatus";
        this.patient_id = patient_id;
        this.details = details;
        this.time_expire = time_expire;
    }

    /* Call this method after you've initialized your object.
       This will actually do the post request. */
    protected void executePostRequest() {
        new PostingData().execute();
    }


    private class PostingData extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                String link = "https://medagenda-backend.herokuapp.com/patients";
                String param = "";
                if (type.equals("newPatient")) { // this is for new patient.
                    param = "firstname=" + URLEncoder.encode(firstName, "UTF-8") +
                            "&lastname=" + URLEncoder.encode(lastName, "UTF-8") +
                            "&room=" + URLEncoder.encode(room_num, "UTF-8") +
                            "&dob=" + URLEncoder.encode(dob, "UTF-8");
                } else if (type.equals("newTask")){
                    link = "https://medagenda-backend.herokuapp.com/patients/" + index + "/" + option;
                    param = "patient_id=" + URLEncoder.encode(Integer.toString(patient_id), "UTF-8") +
                            "&details=" + URLEncoder.encode(details, "UTF-8") +
                            "&time_due=" + URLEncoder.encode(time_due, "UTF-8") +
                            "&color=" + URLEncoder.encode(Integer.toString(color), "UTF-8") +
                            "&repeat_time=" + URLEncoder.encode(Integer.toString(repeat_time), "UTF-8");
                } else {
                    link = "https://medagenda-backend.herokuapp.com/patients/" + index + "/" + option;
                    param = "patient_id" + URLEncoder.encode(Integer.toString(patient_id), "UTF-8") +
                            "&details" + URLEncoder.encode(details, "UTF-8") +
                            "&time_expire" + URLEncoder.encode(time_expire, "UTF-8");
                }

                URL url = new URL(link);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setFixedLengthStreamingMode(param.getBytes().length);
                connection.setRequestProperty("Accept", "application/json");

                PrintWriter out = new PrintWriter(connection.getOutputStream());
                out.print(param);
                out.flush();
                out.close();

                String response = "";
                Scanner inStream = new Scanner(connection.getInputStream());
                //process the stream and store it in StringBuilder
                while (inStream.hasNextLine())
                    response += (inStream.nextLine());
                activity.finish();

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

}