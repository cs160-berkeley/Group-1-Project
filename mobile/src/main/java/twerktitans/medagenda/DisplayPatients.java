package twerktitans.medagenda;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;

public class DisplayPatients extends AppCompatActivity {

    public static class AlarmTuple<task, time> {
        public String task;
        public long time;
        public AlarmTuple(String taskName, long alarmTime) {
            this.task = taskName;
            this.time = alarmTime;
        }
    }


    static ArrayList<AlarmTuple> alarmTimes = new ArrayList<AlarmTuple>();
    static LinkedList<Patient> patients;
    static boolean firstTime = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_patients);

        if (Icon.icons == null) {
            Icon.setup();
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("tasks")) {
                String [] indices = extras.getString("tasks").split(";");
                int indx = Integer.parseInt(indices[0]);
                int pos = Integer.parseInt(indices[1]);
                Task t = patients.get(indx).tasks.get(pos);
                if (t.minBtwRepeats > 0) {
                    t.time.add(Calendar.MINUTE, t.minBtwRepeats);
                    NotificationMaker.makeAlarm(this, t);
                }
                else {
                    patients.get(indx).deleteTask(pos);
                }
            } else if (firstTime && extras.containsKey("json_data")) {
                patients = new LinkedList<>();
                parseJson(extras.getString("json_data"));
                firstTime = false;
            }
        }

        refreshList();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {

                NdefRecord ndr = ((NdefMessage) rawMsgs[0]).getRecords()[0];
                String nfcStr = new String(Arrays.copyOfRange(ndr.getPayload(), 3, ndr.getPayload().length) );
                Log.d("NFC Works", nfcStr);
                Toast.makeText(getApplicationContext(), nfcStr, Toast.LENGTH_LONG);

                // Given the room number, it will get the index and then fire off the
                // detailed view activity and to - do list activity.
                int patient_index = -1;
                for (int i = 0; i < patients.size(); i += 1) {
                    if (patients.get(i).room.equals(nfcStr)) {
                        patient_index = i;
                        break;
                    }
                }
                if (patient_index != -1) {
                    // sends information to both mobile detailed view and watch.
                    sendIntents(patient_index);
                }
            }
        }

    }

    private void parseJson(String json_data) {
        try {
            JSONArray jsonarr = new JSONArray(json_data);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss.SSS");
            for (int i = 0; i < jsonarr.length(); i += 1) {
                Patient p = new Patient();
                JSONObject jsonobj = jsonarr.getJSONObject(i);
                p.firstName = jsonobj.get("firstname").toString();
                p.lastName = jsonobj.get("lastname").toString();
                p.dateOfBirth = jsonobj.get("dob").toString();
                p.admitDate = "3/22/2016";
                p.room = "Rm. " + jsonobj.get("room").toString();
                try {
                    Date date = sdf.parse(jsonobj.get("dob").toString());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    p.dateOfBirth = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_WEEK) + "/" +
                                    cal.get(Calendar.YEAR);
                } catch(Exception e){
                    Log.d("DisplayPatient", "Unable to parse dob" + e.getMessage());
                }
                JSONArray task_list = jsonobj.getJSONArray("tasks");
                for (int j = 0; j < task_list.length(); j += 1) {
                    Task t = new Task();
                    JSONObject lst = task_list.getJSONObject(j);
                    t.details = lst.get("details").toString();
//                    System.out.println("details is: " + t.details);
                    t.iconIndex = -1;
                    try {
                        Date date = sdf.parse(lst.get("time_due").toString());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        t.time = calendar;
                        p.tasks.add(t);
                    } catch(Exception e){
                        Log.d("DisplayPatient", "Unable to parse time " + e.getMessage());
                    }
                    NotificationMaker.makeAlarm(this, t);
                }

                JSONArray status_list = jsonobj.getJSONArray("status");
                for (int k = 0; k < status_list.length(); k += 1) {
                    Status s = new Status();
                    JSONObject lst = status_list.getJSONObject(k);
                    s.details = lst.get("details").toString();
                    p.statuses.add(s);
                }
                patients.add(p);
                System.out.println("-------");
            }
        } catch(JSONException e) {
            Log.d("DisplayPatients", "could not parse json string");
        }
    }

    private void refreshList() {
        ListView patientList = (ListView) findViewById(R.id.listPatients);
        Collections.sort(patients);
        PatientAdapter patientAdapter = new PatientAdapter(this, patients);
        patientList.setAdapter(patientAdapter);

        patientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendIntents(position);
            }
        });
    }

    protected void sendIntents(int position) {
        Intent detailIntent = new Intent(DisplayPatients.this, PatientDetails.class);
        detailIntent.putExtra("INDEX", position);
        startActivity(detailIntent);

        Patient p = patients.get(position);
        String s = p.getName() + ";" + position + ";";
        for (int i = 0; i < p.getTaskSize(); i += 1) {
            s += p.getTasks().get(i).details+ ";";
            s += p.getTasks().get(i).getTaskTime() + ";";
        }
        s = s.substring(0, s.length() - 1);
        Log.v("MAIN_ACTIVITY", "s is: " + s);
        Intent sendToWear = new Intent(DisplayPatients.this, PhoneToWatchService.class);
        sendToWear.putExtra("Data", s);
        sendToWear.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        DisplayPatients.this.startService(sendToWear);
        Log.v("DEBUG_TAG", "started Wear Activity");
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
    Calendar now;
    Calendar later;

    public PatientAdapter(Context context, LinkedList<Patient> patients) {
        this.context = context;
        this.patients = patients;
        now = Calendar.getInstance();
        later = Calendar.getInstance();
        later.add(Calendar.MINUTE, 60);
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
        ImageView icon = (ImageView) patientListItem.findViewById(R.id.imgPatientTaskIcon);

        name.setText(patient.getName());
        room.setText(patient.room);

        Task firstTask = patient.getFirstTask();
        task.setText(firstTask.details);

        icon.setImageResource(Icon.getIconResource(firstTask.iconIndex));

        taskTime.setText(patient.getFirstTaskTime()); //TODO: We'll need to change this when we figure out how to do timers
        if (firstTask.time != null) {
            if (firstTask.time.compareTo(now) <= 0) {
                taskTime.setTextColor(context.getResources().getColor(R.color.urgent_red));
                ImageView lateIcon = (ImageView) patientListItem.findViewById(R.id.imgPatientLateIcon);
                lateIcon.setAlpha((float) 1.0);
            }
            else if (firstTask.time.compareTo(later) <= 0)
            {
                taskTime.setTextColor(context.getResources().getColor(R.color.urgent_yellow));
            }
            else {
                taskTime.setTextColor(context.getResources().getColor(R.color.urgent_green));
            }
        }
        return patientListItem;
    }
}

