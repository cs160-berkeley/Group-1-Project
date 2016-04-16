package twerktitans.medagenda;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

  private TextView mTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
    stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
      @Override
      public void onLayoutInflated(WatchViewStub stub) {

        final ListView patient_lst = (ListView) findViewById(R.id.patients_view);
        Log.d("DEBUG_TAG", "patient_lst is: " + patient_lst);
        String [] patient_names = {"Eric Paulos, Rm.160", "Derrick Hu, Rm.161", "John Doe, Rm.162"};
        String [] patient_times = {"10:43AM", "2:00PM", "3:21PM"};
        final PatientArrayAdapter adapter = new PatientArrayAdapter(getApplicationContext(), patient_names, patient_times);
        Log.d("DEBUG_TAG", "" + adapter);
        patient_lst.setAdapter(adapter);
        mTextView = (TextView) stub.findViewById(R.id.text);

      }
    });
  }
}
