package twerktitans.medagenda;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.List;

public class NewTaskActivity extends AppCompatActivity {

  static final int RED_CODE = Color.parseColor("#EB5757");
  static final int BLUE_CODE = Color.parseColor("#4BA0D0");
  static final int PURPLE_CODE = Color.parseColor("#9B51E0");
  static final int ORANGE_CODE = Color.parseColor("#F2994A");
  static final int GREEN_CODE = Color.parseColor("#219653");

  static final int TIME_DIALOG_ID = 0;
  final Task newTask = new Task();

  private int taskHour;
  private int taskMinute;

  private TimePickerDialog.OnTimeSetListener mTimeSetListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_task);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    //setupStaticFields();
    setupFormHandler();
  }

  private void setupStaticFields() {
//    TextView patientName = (TextView) findViewById(R.id.textNewTaskPatient);
//    TextView patientDoB = (TextView) findViewById(R.id.textNewTaskDoB);
//    TextView patientRoom = (TextView) findViewById(R.id.textNewTaskRoom);
//
//    final int index = getIntent().getIntExtra("INDEX", 0);
//
//    Patient p = DisplayPatients.patients.get(index);
//    patientName.setText(p.getName());
//    patientDoB.setText(p.dateOfBirth);
//    patientRoom.setText(p.room);
  }

  private void setupFormHandler() {
    final TextView tv = (TextView) findViewById(R.id.textNewTaskTime);
    final EditText detailsEdit = (EditText) findViewById(R.id.editNewTaskDetails);

    newTask.time = Calendar.getInstance();

    tv.setText(newTask.getTaskTime());

    mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
      public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);

        if (cal.compareTo(Calendar.getInstance()) <= 0) {
          //set to next day
          cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        newTask.time = cal;
        tv.setText(newTask.getTaskTime());
      }
    };

    Button setTime = (Button) findViewById(R.id.btnNewTaskTime);
    setTime.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        showDialog(TIME_DIALOG_ID);
      }
    });

    final Spinner colorSpinner = (Spinner) findViewById(R.id.newTaskColorSpinner);
    ArrayAdapter<String> adapter = new ColorSpinnerAdapter(this, R.array.colors, Icon.icons);
    adapter.setDropDownViewResource(R.layout.color_spinner_item);
    colorSpinner.setAdapter(adapter);

    final RelativeLayout repeatsLayout = (RelativeLayout) findViewById(R.id.layoutNewTaskRepeats);

    final ToggleButton repeatsToggle = (ToggleButton) findViewById(R.id.toggleNewTaskRepeats);
    repeatsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          repeatsLayout.setVisibility(View.VISIBLE);
        }
        else {
          repeatsLayout.setVisibility(View.INVISIBLE);
        }
      }
    });

    final EditText minEdit = (EditText) findViewById(R.id.editNewTaskMinutes);
    final int index = getIntent().getIntExtra("INDEX", 0);
    Button addTask = (Button) findViewById(R.id.btnNewStatusAdd);
    addTask.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //create the task
        if (detailsEdit.getText().length() == 0) {
          Toast.makeText(NewTaskActivity.this, "Need details about the task", Toast.LENGTH_LONG).show();
        }
        else if (newTask.time == null) {
          Toast.makeText(NewTaskActivity.this, "Need to set a time", Toast.LENGTH_LONG).show();
        }
        else {

          getCategory();
          newTask.details = detailsEdit.getText().toString();
          newTask.minBtwRepeats = 0;
          getRepeats();

          Calendar temp = Calendar.getInstance();
          int year = temp.get(Calendar.YEAR);
          int month = temp.get(Calendar.MONTH)+1;
          int day = temp.get(Calendar.DAY_OF_MONTH);
          String time_due = "" + month + "-" + day + "-" + year;


          DisplayPatients.patients.get(index).tasks.add(newTask);
          PostInfo post_data = new PostInfo(NewTaskActivity.this, index, index, newTask.details,
                                            newTask.getTaskTime(), newTask.iconIndex, newTask.minBtwRepeats);
          post_data.executePostRequest();
        }
      }

      private void getCategory() {
        int iconIndex = colorSpinner.getSelectedItemPosition();
        newTask.iconIndex = iconIndex;
      }

      private void getRepeats() {
        if (repeatsToggle.isChecked()) {
          try {
            newTask.minBtwRepeats = Integer.parseInt(minEdit.getText().toString());
            Log.d("", newTask.minBtwRepeats + "");
          } catch (NumberFormatException nfe) {

          }
        } else {
          newTask.minBtwRepeats = 0;
        }
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        break;
    }
    return true;
  }

  @Override
  public void onBackPressed()
  {
    finish();
    return;
  }

  @Override
  protected Dialog onCreateDialog(int id) {
    switch (id) {
      case TIME_DIALOG_ID:
        return new TimePickerDialog(this,
          mTimeSetListener, taskHour, taskMinute, false);
    }
    return null;
  }


}

class ColorSpinnerAdapter extends ArrayAdapter<String> {
  private List<String> objects;
  private Context context;

  public ColorSpinnerAdapter(Context context, int resourceId,
                            List<String> objects) {
    super(context, resourceId, objects);
    this.objects = objects;
    this.context = context;
  }

  @Override
  public View getDropDownView(int position, View convertView,
                              ViewGroup parent) {
    return getCustomView(position, convertView, parent);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return getCustomView(position, convertView, parent);
  }

  public View getCustomView(int position, View convertView, ViewGroup parent) {

    LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
    View entry = inflater.inflate(R.layout.color_spinner_item, parent, false);
    //TextView label=(TextView)row.findViewById(R.id.spItem);
    //label.setText(objects.get(position));

    ImageView iv = (ImageView) entry.findViewById(R.id.imgColorSpinner);
    iv.setImageResource(Icon.getIconResource(position));

    return entry;
  }




}

/*
code from older design decisions

//static ArrayList<String> colorsList;
  //    if (colorsList == null) {
  //      colorsList = new ArrayList<>(5);
  //      colorsList.add("red");
  //      colorsList.add("blue");
  //      colorsList.add("orange");
  //      colorsList.add("green");
  //      colorsList.add("purple");
  //    }

ArrayAdapter<String> adapter = new ColorSpinnerAdapter(this, R.array.colors, colorsList);

switch (objects.get(position)) {
      case "red":
        iv.setImageResource(R.drawable.red);
        break;
      case "blue":
        iv.setImageResource(R.drawable.blue);
        break;
      case "orange":
        iv.setImageResource(R.drawable.orange);
        break;
      case "green":
        iv.setImageResource(R.drawable.green);
        break;
      default:
        iv.setImageResource(R.drawable.purple);
        break;
private void getColor() {
        int colorIndex = colorSpinner.getSelectedItemPosition();
        switch (colorsList.get(colorIndex)) {
          case "red":
            newTask.color = RED_CODE;
            break;
          case "blue":
            newTask.color = BLUE_CODE;
            break;
          case "orange":
            newTask.color = ORANGE_CODE;
            break;
          case "green":
            newTask.color = GREEN_CODE;
            break;
          default:
            newTask.color = PURPLE_CODE;
            break;
        }
      }
 */