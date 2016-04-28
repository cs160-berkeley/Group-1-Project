package twerktitans.medagenda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class NewStatusActivity extends AppCompatActivity {

  private final Status status = new Status();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_status);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    //setupStaticFields();
    setupFormHandlers();
  }

  private void setupStaticFields() {
//    final int index = getIntent().getIntExtra("INDEX", 0);
//    Patient p = DisplayPatients.patients.get(index);
//    TextView name = (TextView) findViewById(R.id.textNewStatusPatient);
//    TextView room = (TextView) findViewById(R.id.textNewStatusRoom);
//    name.setText(p.getName());
//    room.setText(p.room);
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

  private void setupFormHandlers() {
    final EditText detailsEdit = (EditText) findViewById(R.id.editNewStatusDetail);
    final RelativeLayout timeLayout = (RelativeLayout) findViewById(R.id.layoutNewStatus);
    final ToggleButton timedToggle = (ToggleButton) findViewById(R.id.toggleNewStatusTimed);
    final EditText timeEdit = (EditText) findViewById(R.id.editNewStatusTime);
    Button addStatus = (Button) findViewById(R.id.btnNewStatusAdd);

    timedToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          timeLayout.setVisibility(View.VISIBLE);
        }
        else {
          timeLayout.setVisibility(View.INVISIBLE);
        }
      }
    });
    final int index = getIntent().getIntExtra("INDEX", 0);
    addStatus.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (detailsEdit.getText().length() == 0) {
          Toast.makeText(NewStatusActivity.this, "Need details about the status", Toast.LENGTH_LONG).show();
        }
        else {
          if (timedToggle.isChecked() && timeEdit.getText().length() > 0) {
            //add status with time
            Calendar now = Calendar.getInstance();
            now.add(Calendar.MINUTE, Integer.parseInt(timeEdit.getText().toString()));
            status.time = now;
          }
          status.details = detailsEdit.getText().toString();
          DisplayPatients.patients.get(index).statuses.add(status);

          PostInfo post_data = new PostInfo(NewStatusActivity.this, index, index, status.details,
                                            status.time);
          post_data.executePostRequest();
//          finish();
        }
      }
    });
  }
}
