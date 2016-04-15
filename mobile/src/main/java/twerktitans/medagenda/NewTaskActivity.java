package twerktitans.medagenda;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewTaskActivity extends AppCompatActivity {

  static final int RED_CODE = Color.parseColor("#EB5757");
  static final int BLUE_CODE = Color.parseColor("#4BA0D0");
  static final int PURPLE_CODE = Color.parseColor("#9B51E0");
  static final int ORANGE_CODE = Color.parseColor("#F2994A");
  static final int GREEN_CODE = Color.parseColor("#219653");

  static ArrayList<String> colorsList;
  private int taskHour;
  private int taskMinute;
  static final int TIME_DIALOG_ID = 0;
  final Task newTask = new Task();

  private TimePickerDialog.OnTimeSetListener mTimeSetListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_task);

    if (colorsList == null) {
      colorsList = new ArrayList<>(5);
      colorsList.add("red");
      colorsList.add("blue");
      colorsList.add("orange");
      colorsList.add("green");
      colorsList.add("purple");
    }

    final TextView tv = (TextView) findViewById(R.id.textNewTaskTime);

    mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
      public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        taskHour = hourOfDay;
        taskMinute = minute;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, taskHour);
        cal.set(Calendar.MINUTE, taskMinute);

        if (cal.compareTo(Calendar.getInstance()) <= 0) {
          //set to next day
          cal.add(Calendar.DAY_OF_YEAR, 1);
        }

//        Log.d("day", cal.get(Calendar.HOUR) + " " + cal.get(Calendar.MINUTE) + " " + cal.get(Calendar.MONTH) + " " + cal.get(Calendar.DAY_OF_MONTH));

        if (taskHour == 0) {
          tv.setText((taskHour+12) + ":" + String.format("%02d", taskMinute) + " AM");
        }
        else if (taskHour > 12) {
          tv.setText((taskHour-12) + ":" + String.format("%02d", taskMinute) + " PM");
        }
        else {
          tv.setText(taskHour + ":" + String.format("%02d", taskMinute) + " AM");
        }
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
    ArrayAdapter<String> adapter = new ColorSpinnerAdapter(this, R.array.colors, colorsList);
    adapter.setDropDownViewResource(R.layout.color_spinner_item);
    colorSpinner.setAdapter(adapter);

    ImageButton addTask = (ImageButton) findViewById(R.id.btnNewTaskAdd);
    addTask.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //create the task
        int colorIndex = colorSpinner.getSelectedItemPosition();
      }
    });
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
    }

    return entry;
  }

}