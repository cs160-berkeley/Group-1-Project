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

    public PatientInfo(){
        this.name = "";
        this.tasks = new ArrayList<String>();
        this.times = new ArrayList<String>();
    }

    protected String getName(){
        return this.name;
    }

    protected ArrayList<String> getTasks(){
        return this.tasks;
    }

    protected ArrayList<String> getTimes(){
        return this.times;
    }

    /**
     * Returns "true" if both arraylists are empty after deletion.
     * **/
    protected boolean taskComplete(int pos) {
        if (this.tasks.size() > 0 && this.times.size() > 0) {
            this.tasks.remove(pos);
            this.times.remove(pos);
            System.out.println("size of task is: " + this.tasks.size());
        }
        return this.tasks.size() == 0 && this.times.size() == 0;
    }

    /** Data will come in this format:
     *  patient_name;task1;task1_time;task2;task2_time;...
     *
     *  Parse by splitting by semicolon and assigning it to
     *  respective fields. **/
    protected void parseInfo(String data){
        String [] info = data.split(";");
        System.out.println("length of info is: " + info.length);
        this.name = info[0];
        for (int i = 1; i < info.length; i += 2) {
            System.out.println(info[i]);
            this.tasks.add(info[i]);
            System.out.println(info[i + 1]);
            this.times.add(info[i + 1]);
        }
    }
}

