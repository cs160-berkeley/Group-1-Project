package twerktitans.medagenda;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jefftan on 4/16/16.
 */
public class PatientArrayAdapter extends BaseAdapter{

    private Context context;
    private String patient_name;
    private ArrayList<String> tasks;
    private ArrayList<String> times;
    public PatientArrayAdapter(Context context, String patient_name,
                               ArrayList<String> tasks, ArrayList<String> times) {
        this.context = context;
        this.patient_name = patient_name;
        this.tasks = tasks;
        this.times = times;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View repRow = inflater.inflate(R.layout.home_list_view, parent, false);
        TextView taskView = (TextView) repRow.findViewById(R.id.task);
        TextView timeView = (TextView) repRow.findViewById(R.id.time);
        taskView.setText(tasks.get(position));
        timeView.setText(times.get(position));

        repRow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent toConfirmation = new Intent(context, confirmation_activity.class);
                toConfirmation.putExtra("index", position);
                toConfirmation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(toConfirmation);
            }
        });
        return repRow;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return this.tasks.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}
