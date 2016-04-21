package twerktitans.medagenda;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

  Button btn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    btn = (Button) findViewById(R.id.login_btn);
    btn.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        new GetPatientData().execute();
//        Intent patientList = new Intent(MainActivity.this, DisplayPatients.class);
//        startActivity(patientList);
      }
    });
  }

  /* Grabs all the images and  */
  private class GetPatientData extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground(Void... arg0) {
      try {
        String json_data = grabData("http://medagenda-backend.herokuapp.com/patients/");
        return json_data;
      } catch(Exception e) {
        Log.v("Mobile Main Activity", "FetchMoreData, URL invalid");
        return null;
      }

    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String grabData(String myurl) throws IOException {
      InputStream is = null;

      try {
        URL url = new URL(myurl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty ("Content-Type", "application/json");
        urlConnection.setRequestProperty ("Accept", "application/json");

        try {
          BufferedReader bufferedReader = new BufferedReader(
                  new InputStreamReader(urlConnection.getInputStream()));
          StringBuilder stringBuilder = new StringBuilder();
          String line;
          while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
          }
          bufferedReader.close();
          return stringBuilder.toString();
        }
        finally{
          urlConnection.disconnect();
        }
      }
      catch(Exception e) {
        Log.e("ERROR", e.getMessage(), e);
        return null;
      }
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
//        System.out.println("result is: " + result);
        Intent patientList = new Intent(MainActivity.this, DisplayPatients.class);
        patientList.putExtra("json_data", result);
        startActivity(patientList);
    }
  }
}
