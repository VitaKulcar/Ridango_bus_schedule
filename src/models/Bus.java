package models;

import java.time.Duration;
import java.time.LocalTime;

public class Bus {
    private final String tripId;
    private int routeId;
    private final LocalTime absoluteTime;


    public Bus(String tripId, LocalTime arrivalTime) {
        this.tripId = tripId;
        this.absoluteTime = arrivalTime;
    }

    public void setRouteId(int id) {
        routeId = id;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public String getTripId() {
        return tripId;
    }

    public LocalTime getAbsoluteTime() {
        return absoluteTime;
    }

    public String getAbsoluteTimeString() {
        return absoluteTime.toString();
    }

    public String getRelativeTimeString(LocalTime localTimeNow) {
        Duration duration = Duration.between(localTimeNow, absoluteTime);
        return duration.toMinutes() + " min";
    }
}
