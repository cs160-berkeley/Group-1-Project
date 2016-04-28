package twerktitans.medagenda;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jefftan on 4/27/16.
 */
public class PostInfo extends AsyncTask<String, Void, Void> {

    // New Patient variables.
    private String firstName, lastName, room_num, dob;

    // New Task and Status variables
    private String index, details, color, time_due, repeat_time;
    // For new patient
    public PostInfo(String firstName, String lastName, String room_num, String dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.room_num = room_num;
        this.dob = dob;
    }

    // To add a new task or status
    public PostInfo(String index, String details, String time_due) {
        this.index = index;
        this.details = details;
        this.time_due = time_due;
    }



    @Override
    protected Void doInBackground(String... params) {
        try {
            URL url = new URL("https://medagenda-backend.herokuapp.com/patients");

            String param="firstname=" + URLEncoder.encode(p.firstName, "UTF-8")+
                    "&lastname="+URLEncoder.encode(p.lastName,"UTF-8")+
                    "&room="+URLEncoder.encode(p.room,"UTF-8")+
                    "&dob="+URLEncoder.encode(p.dateOfBirth, "UTF-8");

            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setFixedLengthStreamingMode(param.getBytes().length);
            connection.setRequestProperty("Accept", "application/json");

            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(param);
            out.flush();
            out.close();

            String response= "";
            Scanner inStream = new Scanner(connection.getInputStream());
            //process the stream and store it in StringBuilder
            while(inStream.hasNextLine())
                response+=(inStream.nextLine());
            finish();

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
