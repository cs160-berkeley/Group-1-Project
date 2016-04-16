package twerktitans.medagenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jefftan on 4/16/16.
 */
public class PatientArrayAdapter extends BaseAdapter{

    private Context context;
    private String [] patients;
    private String [] times;
    public PatientArrayAdapter(Context context, String [] patients,
                               String [] times) {
        this.context = context;
        this.patients = patients;
        this.times = times;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View repRow = inflater.inflate(R.layout.home_list_view, parent, false);
        TextView nameView = (TextView) repRow.findViewById(R.id.name_and_room);
        TextView timeView = (TextView) repRow.findViewById(R.id.time);
        nameView.setText(patients[position]);
        System.out.println("position is: " + position + " time is: " + times[position]);
        timeView.setText(times[position]);

        repRow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Send Toast or Launch Activity here
                Toast.makeText(context, patients[position], Toast.LENGTH_SHORT).show();
            }
        });
        return repRow;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return this.patients.length;
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
