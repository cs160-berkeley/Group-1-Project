package twerktitans.medagenda;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
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

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by WW on 4/13/2016.
 */
public class PatientDetails extends AppCompatActivity {

  Patient patient;
  ListView patientSchedule;
  ListView patientStatuses;
  final PatientDetails thisContext = this;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_patient_details);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Intent myIntent = getIntent();
    final int index = myIntent.getIntExtra("INDEX", 0);

    patient = DisplayPatients.patients.get(index);
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      if (extras.containsKey("tasks")) {
        String [] indices = extras.getString("tasks").split(";");
        int indx = Integer.parseInt(indices[0]);
        int pos = Integer.parseInt(indices[1]);
        DisplayPatients.patients.get(indx).deleteTask(pos);
      }
    }

    Collections.sort(patient.tasks);

    TextView name = (TextView) findViewById(R.id.textPatientDetailName);
    TextView room = (TextView) findViewById(R.id.textPatientDetailRoom);

    name.setText(patient.getName());
    room.setText(patient.room);

    setupLists();
    refreshLists();

    FloatingActionButton newTask = (FloatingActionButton) findViewById(R.id.btnAddTask);
    FloatingActionButton newStatus = (FloatingActionButton) findViewById(R.id.btnAddStatus);
    newTask.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent newTaskIntent = new Intent(PatientDetails.this, NewTaskActivity.class);
        newTaskIntent.putExtra("INDEX", index);
        startActivity(newTaskIntent);
      }
    });

    newStatus.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent newStatusIntent = new Intent(PatientDetails.this, NewStatusActivity.class);
        newStatusIntent.putExtra("INDEX", index);
        startActivity(newStatusIntent);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.patient_action_bar, menu);
    return true;
  }

  public void onStart() {
    super.onStart();
    refreshLists();
  }

  private void setupLists() {
    patientSchedule = (ListView) findViewById(R.id.listPatientSchedule);
    patientStatuses = (ListView) findViewById(R.id.listPatientStatus);
    final Patient p = patient;
    patientSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final int i = position;

        new AlertDialog.Builder(thisContext)
          .setTitle("Delete Patient")
          .setMessage("Are you sure you want to delete this task?")
          .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              // continue with delete
              p.tasks.remove(i);
              thisContext.refreshLists();
            }
          })
          .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              // do nothing
            }
          })
          .setIcon(android.R.drawable.ic_dialog_alert) //TODO: replace with our own icon
          .show();
      }
    });

    patientStatuses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final int i = position;

        new AlertDialog.Builder(thisContext)
          .setTitle("Delete Patient")
          .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              // continue with delete
              p.deleteStatus(i);
              thisContext.refreshLists();
            }
          })
          .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              // do nothing
            }
          })
          .setIcon(android.R.drawable.ic_dialog_alert) //TODO: replace with our own icon
          .show();
      }
    });
  }

  private void refreshLists() {
    Collections.sort(patient.tasks);

    ScheduleAdapter scheduleAdapter = new ScheduleAdapter(this, patient.tasks);
    patientSchedule.setAdapter(scheduleAdapter);

    StatusAdapter statusAdapter = new StatusAdapter(this, patient.statuses);
    patientStatuses.setAdapter(statusAdapter);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        // Respond to the action bar's Up/Home button
        NavUtils.navigateUpFromSameTask(this);
        return true;
      case R.id.delete:
        // Make dialog to delete patient
        new AlertDialog.Builder(this)
          .setTitle("Delete Patient")
          .setMessage("Are you sure you want to delete this patient?")
          .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              // continue with delete
              Log.d("T", "This patient would have been deleted!");
              DisplayPatients.patients.remove(patient);
              thisContext.finish();
            }
          })
          .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              // do nothing
            }
          })
          .setIcon(android.R.drawable.ic_dialog_alert) //TODO: replace with our own icon
          .show();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}

class ScheduleAdapter extends BaseAdapter {
  private Context context;
  private LinkedList<Task> tasks;

  public ScheduleAdapter(Context context, LinkedList<Task> tasks) {
    this.context = context;
    this.tasks = tasks;
  }

  @Override
  public int getCount() {
    return tasks.size();
  }

  @Override
  public Object getItem(int position) {
    return tasks.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
      .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    Task task = tasks.get(position);

    final View taskListItem = inflater.inflate(R.layout.task_list_item, parent, false);
    TextView details = (TextView) taskListItem.findViewById(R.id.textTaskDetails);
    TextView time = (TextView) taskListItem.findViewById(R.id.textTaskTime);

    details.setText(task.details);
    details.setTextColor(task.color);
    time.setText(task.getTaskTime());
    time.setTextColor(task.color);

    return taskListItem;
  }
}

class StatusAdapter extends BaseAdapter {
  private Context context;
  private LinkedList<Status> statuses;

  public StatusAdapter(Context context, LinkedList<Status> statuses) {
    this.context = context;
    this.statuses = statuses;
  }

  @Override
  public int getCount() {
    return statuses.size();
  }

  @Override
  public Object getItem(int position) {
    return statuses.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
      .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    Status status = statuses.get(position);

    final View taskListItem = inflater.inflate(R.layout.status_list_item, parent, false);

    TextView details = (TextView) taskListItem.findViewById(R.id.textStatusDetails);

    if (status.time != null) {
      details.setText(status.details + " until " + status.getStatusTime());
    }
    else {
      details.setText(status.details);
    }

    return taskListItem;
  }
}