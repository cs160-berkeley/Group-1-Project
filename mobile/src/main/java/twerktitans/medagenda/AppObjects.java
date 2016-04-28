package twerktitans.medagenda;

import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by WW on 4/12/2016.
 */
class Patient implements Comparable<Patient>{
  String firstName;
  String lastName;
  String room;
  String dateOfBirth;
  String admitDate;
  int patient_id;
  LinkedList<Task> tasks;
  LinkedList<Status> statuses;

  Patient() {
    tasks = new LinkedList<>();
    statuses = new LinkedList<>();
  }

  String getName() {
    return firstName + " " + lastName;
  }

  LinkedList<Task> getTasks(){
    return tasks;
  }

  LinkedList<Status> getStatuses(){
    return statuses;
  }

  Task getFirstTask() {
    if (tasks.size() > 0)
    {
      return tasks.getFirst();
    }

    return Task.getEmptyTask();
  }

  void setPatientID(int pid){
      patient_id = pid;
  }

  int getPatientID(){
      return patient_id;
  }

  int getTaskSize(){
    return tasks.size();
  }

  int getStatusSize(){
    return statuses.size();
  }

  String getFirstTaskTime() {
    Collections.sort(tasks);
    if (tasks.size() > 0)
    {
      return tasks.getFirst().getTaskTime();
    }
    return "";
  }

  @Override
  public int compareTo(Patient other) {
    if (this.tasks.size() == 0 && other.tasks.size() == 0) {
      return 0;
    }
    else if (this.tasks.size() == 0){
      return 1;
    }
    else if (other.tasks.size() == 0) {
      return -1;
    }

    Task thisFirstTask = this.tasks.getFirst();
    Task otherFirstTask = other.tasks.getFirst();

    int result = thisFirstTask.compareTo(otherFirstTask);
    if (result == 0) {
      return 0;
    }
    else if (result < 0) {
      return -1;
    }
    else {
      return 1;
    }
  }

  public void deleteTask(int i) {
    tasks.remove(i);
  }

  public void deleteStatus(int i) {
    statuses.remove(i);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Name: ").append(getName()).append("\n");
    sb.append("Room: ").append(room).append("\n");
    sb.append("DOB: ").append(dateOfBirth).append("\n");
    sb.append("Admit: ").append(admitDate).append("\n");
    sb.append("Tasks:\n");
    for (int i = 0; i < tasks.size(); i++) {
      sb.append("\t" + tasks.get(i).toString() + "\n");
    }
    sb.append("Statuses:\n");
    for (int i = 0; i < statuses.size(); i++) {
      sb.append(statuses.get(i).toString() + "\n");
    }
    return sb.toString();
  }
}

class Task implements Comparable<Task> {
  String details;
  Calendar time; //TODO: Placeholder for whatever we use for the timer?
  //int color = Color.parseColor("#000000");
  int iconIndex;
  int minBtwRepeats;

  static Task getEmptyTask() {
    Task temp = new Task();
    temp.details = "";
    temp.minBtwRepeats = 0;

    return temp;
  }

  String getTaskTime() {
    if (time == null) {
      return "";
    }

    String AMOrPM;
    if (this.time.get(Calendar.AM_PM) == Calendar.PM) {
      AMOrPM = "PM";
    }
    else {
      AMOrPM = "AM";
    }

    int hour = this.time.get(Calendar.HOUR);

    if (hour == 0) {
      return (hour+12) + ":" +
        String.format("%02d", this.time.get(Calendar.MINUTE)) + " " +
        AMOrPM;
    }
    else {
      return this.time.get(Calendar.HOUR) + ":" +
        String.format("%02d", this.time.get(Calendar.MINUTE)) + " " +
        AMOrPM;
    }
  }

  @Override
  public int compareTo(Task another) {
    int result = this.time.compareTo(another.time);
    if (result == 0) {
      return 0;
    }
    else if (result < 0) {
      return -1;
    }
    else {
      return 1;
    }
  }

  public String toString()
  {
    return details + "\t" + getTaskTime() + "\t" + iconIndex + "\t" + minBtwRepeats;
  }
}

class Status {
  String details;
  Calendar time;

  String getStatusTime() {
    if (time == null) {
      return "";
    }

    String AMOrPM;
    if (this.time.get(Calendar.AM_PM) == Calendar.PM) {
      AMOrPM = "PM";
    }
    else {
      AMOrPM = "AM";
    }

    int hour = this.time.get(Calendar.HOUR);

    if (hour == 0) {
      return (hour+12) + ":" +
        String.format("%02d", this.time.get(Calendar.MINUTE)) + " " +
        AMOrPM;
    }
    else {
      return this.time.get(Calendar.HOUR) + ":" +
        String.format("%02d", this.time.get(Calendar.MINUTE)) + " " +
        AMOrPM;
    }
  }

  public String toString() {
    return details + "\t" + getStatusTime();
  }
}

class Icon {
  static LinkedList<String> icons;

  static void setup() {
    if (icons == null) {
      icons = new LinkedList<String>();
      icons.add("pills");
      icons.add("stethoscope");
      icons.add("heartbeat");
      icons.add("syringe");
    }
  }

  static int getIconResource(int i) {
    try {
      switch (icons.get(i)) {
        case "pills":
          return R.drawable.pills_icon;
        case "stethoscope":
          return R.drawable.stethoscope_icon;
        case "heartbeat":
          return R.drawable.heartbeat_icon;
        case "syringe":
          return R.drawable.syringe_icon;
        default:
          return R.drawable.logo;
      }
    } catch (IndexOutOfBoundsException ioobe) {
      return R.drawable.logo;
    }
  }
}