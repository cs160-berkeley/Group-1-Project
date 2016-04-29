package twerktitans.medagenda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

  private TextView mTextView;
  static PatientInfo p;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
    stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
      @Override
      public void onLayoutInflated(WatchViewStub stub) {

        final ListView patient_lst = (ListView) findViewById(R.id.patients_view);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        mTextView = (TextView) findViewById(R.id.intro_text);
        String intro_text = "Welcome! \n\n Tap on patient or badge in  \n with phone to see tasks";
        mTextView.setText(intro_text);

        if ((p == null && extras != null) || (p != null && extras != null)) {
          Log.d("WATCH_MA", "in here!");
          mTextView.setVisibility(View.GONE);
          p = new PatientInfo();
          String s = extras.getString("patient_info");
          p.parseInfo(s);
          System.out.println("patient's position in arraylist is: " + p.getPatientPos());
          TextView patient_name = (TextView)findViewById(R.id.name);
          patient_name.setText(p.getName());
          final PatientArrayAdapter adapter = new PatientArrayAdapter(getApplicationContext(),
                                            p.getName(), p.getTasks(), p.getTimes());
          patient_lst.setAdapter(adapter);
        } else {
          Log.d("WATCH_MA", "not in here!");
        }
      }
    });
  }
}
