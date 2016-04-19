package twerktitans.medagenda;

import android.graphics.Color;

import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by WW on 4/12/2016.
 */
class Patient implements Comparable<Patient>{
  String firstName;
  String lastName;
  String room;
  String dateOfBirth;
  LinkedList<Task> tasks;
  LinkedList<Status> statuses;

  Patient() {
    tasks = new LinkedList<>();
    statuses = new LinkedList<>();
  }

  String getName() {
    return firstName + " " + lastName;
  }

  Task getFirstTask() {
    if (tasks.size() > 0)
    {
      return tasks.getFirst();
    }

    return Task.getEmptyTask();
  }

  int getTaskSize(){
    return tasks.size();
  }

  int getStatusSize(){
    return statuses.size();
  }

  String getFirstTaskTime() {
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
}

class Task implements Comparable<Task> {
  String details;
  Calendar time; //TODO: Placeholder for whatever we use for the timer?
  int color = Color.parseColor("#000000");
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
}
