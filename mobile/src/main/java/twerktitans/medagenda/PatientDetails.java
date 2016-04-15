package twerktitans.medagenda;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 * Created by WW on 4/13/2016.
 */
public class PatientDetails extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_patient_details);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Intent myIntent = getIntent();
    final int index = myIntent.getIntExtra("INDEX", 0);

    Patient patient = MainActivity.patients.get(index);

    TextView name = (TextView) findViewById(R.id.textPatientDetailName);
    TextView room = (TextView) findViewById(R.id.textPatientDetailRoom);

    name.setText(patient.name);
    room.setText(patient.room);

    ListView patientSchedule = (ListView) findViewById(R.id.listPatientSchedule);
    ScheduleAdapter scheduleAdapter = new ScheduleAdapter(this, patient.tasks);
    patientSchedule.setAdapter(scheduleAdapter);

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
        Log.d("T", "Add new status!");
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
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
    time.setText(task.getTaskTime());

    return taskListItem;
  }
}