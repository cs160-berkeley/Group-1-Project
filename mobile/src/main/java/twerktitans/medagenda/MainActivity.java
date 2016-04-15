package twerktitans.medagenda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

  static LinkedList<Patient> patients;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    patients = new LinkedList<>();
    Patient p = new Patient();
    Task t = new Task();
    p.name = "Eric Paulos";
    p.room = "Jacobs 310";
    t.details = "Give coffee";
    t.time = new GregorianCalendar(2016, 4, 13, 14, 30);
    p.tasks.add(t);
    patients.add(p);

    p = new Patient();
    t = new Task();
    p.name = "Stephen Colbert";
    p.room = "Rm. 123";
    t.details = "Applaud him";
    t.time = new GregorianCalendar(2016, 4, 13, 11, 0);
    p.tasks.add(t);
    patients.add(p);

    Log.d("t", patients.get(0).toString());
    Log.d("t", patients.get(1).toString());
    Collections.sort(patients);

    ListView patientList = (ListView) findViewById(R.id.listPatients);
    PatientAdapter patientAdapter = new PatientAdapter(this, patients);
    patientList.setAdapter(patientAdapter);

    patientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detailIntent = new Intent(MainActivity.this, PatientDetails.class);
        detailIntent.putExtra("INDEX", position);
        startActivity(detailIntent);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.action_bar, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case R.id.add:
        //TODO: Add new patient from here
        Log.d("T", "Add patient!");
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}

class PatientAdapter extends BaseAdapter {
  private Context context;
  private LinkedList<Patient> patients;

  public PatientAdapter(Context context, LinkedList<Patient> patients) {
    this.context = context;
    this.patients = patients;
  }

  @Override
  public int getCount() {
    return patients.size();
  }

  @Override
  public Object getItem(int position) {
    return patients.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
      .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    Patient patient = patients.get(position);

    final View patientListItem = inflater.inflate(R.layout.patient_list_item, parent, false);
    TextView name = (TextView) patientListItem.findViewById(R.id.textPatientName);
    TextView room = (TextView) patientListItem.findViewById(R.id.textPatientRoom);
    TextView task = (TextView) patientListItem.findViewById(R.id.textPatientTask);
    TextView taskTime = (TextView) patientListItem.findViewById(R.id.textPatientTaskTime);

    name.setText(patient.name);
    room.setText(patient.room);

    task.setText(patient.getFirstTask());
    taskTime.setText(patient.getFirstTaskTime()); //TODO: We'll need to change this when we figure out how to do timers

    return patientListItem;
  }
}