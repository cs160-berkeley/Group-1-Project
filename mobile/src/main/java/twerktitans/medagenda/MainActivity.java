package twerktitans.medagenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

  Button btn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    btn = (Button) findViewById(R.id.login_btn);
    btn.setOnClickListener(new View.OnClickListener(){
      public void onClick(View v) {
        Intent patientList = new Intent(MainActivity.this,DisplayPatients.class);
        startActivity(patientList);
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
      case R.id.add:
        //TODO: Add new patient from here
        Intent newPatientIntent = new Intent(MainActivity.this, NewPatientActivity.class);
        startActivity(newPatientIntent);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}

