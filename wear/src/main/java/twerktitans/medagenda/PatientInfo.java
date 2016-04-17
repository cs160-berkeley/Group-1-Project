package twerktitans.medagenda;
import java.util.ArrayList;

/**
 * Holds all the patient information and tasks that the nurse
 * needs to do.
 *
 * Created by jefftan on 4/17/16.
 */
public class PatientInfo {

    private String name;
    private ArrayList<String> tasks;
    private ArrayList<String> times;

    public PatientInfo(String name, ArrayList<String> tasks, ArrayList<String> times){
        this.name = name;
        this.tasks = tasks;
        this.times = times;
    }

    public PatientInfo(){}

    protected String getName(){
        return this.name;
    }

    protected ArrayList<String> getTasks(){
        return this.tasks;
    }

    protected ArrayList<String> getTimes(){
        return this.times;
    }

    /** Data will come in this format:
     *  patient_name;task1;task1_time;task2;task2_time;...
     *
     *  Parse by splitting by semicolon and assigning it to
     *  respective fields. **/
    protected void parseInfo(String data){
        String [] info = data.split(";");
        this.name = info[0];
        for (int i = 1; i <= info.length; i += 2) {
            this.tasks.add(info[i]);
            this.times.add(info[i + 1]);
        }
    }
}

