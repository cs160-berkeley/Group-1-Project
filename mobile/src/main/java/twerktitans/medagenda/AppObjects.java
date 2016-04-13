package twerktitans.medagenda;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by WW on 4/12/2016.
 */
class Patient implements Comparable<Patient>{
  String name;
  String room;
  LinkedList<Task> tasks;

  Patient() {
    tasks = new LinkedList<>();
  }

  String getFirstTask() {
    if (tasks.size() > 0)
    {
      return tasks.getFirst().details;
    }
    return "";
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

    int result = thisFirstTask.time.compareTo(otherFirstTask.time);
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

class Task {
  String details;
  GregorianCalendar time; //TODO: Placeholder for whatever we use for the timer?

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
    return this.time.get(Calendar.HOUR) + ":" +
           String.format("%02d", this.time.get(Calendar.MINUTE)) + " " +
           AMOrPM;
  }
}
