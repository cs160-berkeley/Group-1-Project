package twerktitans.medagenda;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by jefftan on 4/17/16.
 */

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
//    private static final String TOAST = "/send_toast";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equals("/fromWatch") ) {

            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            // Make a toast with the String
            Context context = getApplicationContext();
            Log.d("PHONELISTENERSERVICE", "value is: " + value);
            Intent intent = new Intent(this, PatientDetails.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("tasks", value);
            Log.d("T", "about to start watch MainActivity");
            startActivity(intent);


        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}

