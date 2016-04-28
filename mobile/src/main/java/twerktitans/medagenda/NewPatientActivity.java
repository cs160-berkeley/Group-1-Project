package twerktitans.medagenda;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NewPatientActivity extends AppCompatActivity {

  private DatePickerDialog.OnDateSetListener mOnDateSetListener;
  private static final int DATE_DIALOG_ID = 0;

  private int year;
  private int month;
  private int day;

  private final Patient p = new Patient();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_patient);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    final EditText firstName = (EditText) findViewById(R.id.editNewPatientFirstName);
    final EditText lastName = (EditText) findViewById(R.id.editNewPatientLastName);
    final EditText room = (EditText) findViewById(R.id.editNewPatientRoom);
    final TextView dateOfBirth = (TextView) findViewById(R.id.textNewPatientDoB);
    Button setDoB = (Button) findViewById(R.id.btnNewPatientSetDoB);
    Button addPatient = (Button) findViewById(R.id.btnNewPatientAdd);

    Calendar temp = Calendar.getInstance();
    year = temp.get(Calendar.YEAR);
    month = temp.get(Calendar.MONTH)+1;
    day = temp.get(Calendar.DAY_OF_MONTH);
    p.admitDate = "" + month + "-" + day + "-" + year;
    p.dateOfBirth = p.admitDate;
    dateOfBirth.setText(p.dateOfBirth);


      mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.d("a", year + " " + (monthOfYear+1) + " " + dayOfMonth);
        p.dateOfBirth = "" + (monthOfYear+1) + "-" + dayOfMonth + "-" + year;
        dateOfBirth.setText(p.dateOfBirth);
      }
    };

    setDoB.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showDialog(DATE_DIALOG_ID);
      }
    });

    addPatient.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (firstName.getText().length() == 0) {
          Toast.makeText(NewPatientActivity.this, "Need to put a first name", Toast.LENGTH_LONG).show();
        }
        else if (lastName.getText().length() == 0){
          Toast.makeText(NewPatientActivity.this, "Need to put a last name", Toast.LENGTH_LONG).show();
        }
        else if (room.getText().length() == 0) {
          Toast.makeText(NewPatientActivity.this, "Need to put a room", Toast.LENGTH_LONG).show();
        }
        else {
          p.firstName = firstName.getText().toString();
          p.lastName = lastName.getText().toString();
          p.room = "Rm. " + room.getText().toString();

          DisplayPatients.patients.add(p);
          new PostClass().execute();
          // this is where we send the post request.
          //finish();
        }
      }
    });
  }

  private class PostClass extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
      try {
        URL url = new URL("https://medagenda-backend.herokuapp.com/patients");

        String param="firstname=" + URLEncoder.encode(p.firstName,"UTF-8")+
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

  @Override
  protected Dialog onCreateDialog(int id) {
    switch (id) {
      case DATE_DIALOG_ID:
        return new DatePickerDialog(this, mOnDateSetListener, year, month, day);
    }
    return null;
  }
}
