package twerktitans.medagenda;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
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
        Log.d("DEBUG_TAG", "patient_lst is: " + patient_lst);
        if (p == null) {
          p = new PatientInfo();
          String s = "Eric Paulos;Check blood pressure;3:00PM;Entertain war stories;3:15PM";
          p.parseInfo(s);
        }
        TextView patient_name = (TextView)findViewById(R.id.name);
        patient_name.setText(p.getName());
//        String [] patient_names = {"Eric Paulos, Rm.160", "Derrick Hu, Rm.161", "Stephen Colbert, Rm.162"};
//        String [] patient_times = {"10:43AM", "2:00PM", "3:00"};
        final PatientArrayAdapter adapter = new PatientArrayAdapter(getApplicationContext(),
                                            p.getName(), p.getTasks(), p.getTimes());
        Log.d("DEBUG_TAG", "" + adapter);
        patient_lst.setAdapter(adapter);
        mTextView = (TextView) stub.findViewById(R.id.text);
      }
    });
  }
}
