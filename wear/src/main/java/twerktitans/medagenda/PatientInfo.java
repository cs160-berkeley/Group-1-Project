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
    private String patient_pos;
    private ArrayList<String> tasks;
    private ArrayList<String> times;
    private String information;

    public PatientInfo(String name, ArrayList<String> tasks, ArrayList<String> times){
        this.name = name;
        this.tasks = tasks;
        this.times = times;
    }

    public PatientInfo(){
        this.name = "";
        this.patient_pos = "";
        this.tasks = new ArrayList<String>();
        this.times = new ArrayList<String>();
        this.information = "";
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
            // System.out.println("size of task is: " + this.tasks.size());
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
        this.name = info[0];
        this.patient_pos = info[1];
        for (int i = 2; i < info.length; i += 2) {
            this.tasks.add(info[i]);
            this.times.add(info[i+1]);
        }
    }

    protected String getPatientPos(){
        return this.patient_pos;
    }

    protected String getInfo() {
        information += this.name + ";" + this.patient_pos + ";";
        for(int i = 0; i < this.tasks.size(); i += 1) {
            information += this.tasks.get(i) + ";" + this.times.get(i) + ";";
        }
        System.out.println("info is: " + information);
        return information.substring(0, information.length() - 1);
    }
}

