package com.example.Ridango_bus_schedule;

import java.time.Duration;
import java.time.LocalTime;

public class BusStop {
    String tripId;
    int routeId;
    LocalTime absoluteTime;


    public BusStop(String tripId, LocalTime arrivalTime) {
        this.tripId = tripId;
        this.absoluteTime = arrivalTime;
    }

    public void setRouteId(int i) {
        this.routeId = i;
    }

    public Integer getRouteId() {
        return this.routeId;
    }

    public String getTripId() {
        return this.tripId;
    }

    public LocalTime getAbsoluteTime() {
        return this.absoluteTime;
    }

    public String getAbsoluteTimeString() {
        return this.absoluteTime.toString();
    }

    public String getRelativeTimeString(LocalTime localTimeNow) {
        Duration duration = Duration.between(localTimeNow, absoluteTime);
        return duration.toMinutes() + " min";

    }
}
