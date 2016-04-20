package twerktitans.medagenda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class confirmation_activity extends Activity {

    private ImageView back_button, confirm_button;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_activity);
        back_button = (ImageView) findViewById(R.id.back_button);
        confirm_button = (ImageView) findViewById(R.id.confirm_button);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            index = extras.getInt("index");
        } else {
            Log.d("DEBUG_TAG","CRAP! NO INDEX WAS FOUND!!!");
        }

        back_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                finish();
            }
        });

        confirm_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(MainActivity.p.taskComplete(index)) {
                    // all complete page
                    Intent all_done = new Intent(getApplicationContext(), all_tasks_done.class);
                    all_done.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(all_done);
                } else {
                    // go back to to - do list.
                    Intent back_to_list = new Intent(getApplicationContext(), MainActivity.class);
                    back_to_list.putExtra("patient_info", MainActivity.p.getInfo());
                    back_to_list.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(back_to_list);
                }

                Intent sendToWear = new Intent(confirmation_activity.this, WatchToPhoneService.class);
                sendToWear.putExtra("patient_position", MainActivity.p.getPatientPos());
                sendToWear.putExtra("task_status_pos", index);
                sendToWear.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(sendToWear);
            }
        });
    }
}
