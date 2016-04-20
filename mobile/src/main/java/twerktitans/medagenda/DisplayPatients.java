package twerktitans.medagenda;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;

public class DisplayPatients extends AppCompatActivity {

    static LinkedList<Patient> patients;
    static boolean firstTime = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_patients);

        if (firstTime) {
            System.out.println("DisplayPatients FIRSTTIME!");
            patients = new LinkedList<>();
            Patient p = new Patient();
            Task t = new Task();
            p.firstName = "Eric";
            p.lastName = "Paulos";
            p.room = "Jacobs 310";
            t.details = "Give coffee";
            t.time = Calendar.getInstance();
            t.color = Color.parseColor("#00FF00");
            p.tasks.add(t);
            patients.add(p);

            p = new Patient();
            t = new Task();
            p.firstName = "Stephen";
            p.lastName = "Colbert";
            p.room = "Rm. 123";
            t.details = "Applaud him";
            t.time = Calendar.getInstance();
            t.time.add(Calendar.HOUR, 1);
            t.color = Color.parseColor("#0000FF");
            p.tasks.add(t);
            patients.add(p);

            Log.d("t", patients.get(0).toString());
            Log.d("t", patients.get(1).toString());
            Collections.sort(patients);
            firstTime = false;
        }

        System.out.println("DisplayPatients IN HERE!");
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("tasks")) {
                String [] indices = extras.getString("tasks").split(";");
                int indx = Integer.parseInt(indices[0]);
                int pos = Integer.parseInt(indices[1]);
                patients.get(indx).deleteTask(pos);
            }
        }
        refreshList();


//        Intent intent = getIntent();

        Log.d("before NFC", "9o9u,kgh");
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Log.d("NFC discovered", "before rawMsgs check");
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {

                NdefRecord ndr = ((NdefMessage) rawMsgs[0]).getRecords()[0];
                String nfcStr = new String(Arrays.copyOfRange(ndr.getPayload(), 3, ndr.getPayload().length) );
                Log.d("NFC Works", nfcStr);

                Intent detailIntent = new Intent(DisplayPatients.this, PatientDetails.class);
                int position = 0; // dummy
                detailIntent.putExtra("INDEX", position);
                startActivity(detailIntent);
            }
        }

    }

    private void refreshList() {
        ListView patientList = (ListView) findViewById(R.id.listPatients);
        PatientAdapter patientAdapter = new PatientAdapter(this, patients);
        patientList.setAdapter(patientAdapter);

        patientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(DisplayPatients.this, PatientDetails.class);
                detailIntent.putExtra("INDEX", position);
                startActivity(detailIntent);

                //Start Wear Service
                Patient p = patients.get(position);
                String s = p.getName() + ";" + position + ";" + p.getFirstTask().details + ";" + p.getFirstTaskTime();
                Log.v("MAIN_ACTIVITY", "s is: " + s);
                Intent sendToWear = new Intent(DisplayPatients.this, PhoneToWatchService.class);
                sendToWear.putExtra("Data", s);
                sendToWear.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                DisplayPatients.this.startService(sendToWear);
                Log.v("DEBUG_TAG", "started Wear Activity");
            }
        });
    }

    public void onStart()
    {
        super.onStart();
        refreshList();
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
            case R.id.add:
                //TODO: Add new patient from here
                Intent newPatientIntent = new Intent(DisplayPatients.this, NewPatientActivity.class);
                startActivity(newPatientIntent);
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

        name.setText(patient.getName());
        room.setText(patient.room);

        Task firstTask = patient.getFirstTask();
        task.setText(firstTask.details);
        task.setTextColor(firstTask.color);
        taskTime.setText(patient.getFirstTaskTime()); //TODO: We'll need to change this when we figure out how to do timers

        return patientListItem;
    }
}

