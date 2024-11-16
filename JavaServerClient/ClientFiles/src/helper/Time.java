package helper;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Time implements Serializable {

    LocalTime time;

    public Time() {
        time = LocalTime.now();
    }

    public Time(int minutes) {
        time = LocalTime.now().plusMinutes(minutes);
    }

    public LocalTime getTime() {
        return LocalTime.now();
    }

    public Boolean isOver() {
        return LocalTime.now().isAfter(this.time) || LocalTime.now().equals(this.time);
    }

    @Override
    public String toString() {
        return time.format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString();
    }

}
