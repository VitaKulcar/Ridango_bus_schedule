package com.example.Ridango_bus_schedule;

public class UserInput {
    private static UserInput instance;

    int busId;
    int numberOfBus;
    TimeFormat timeFormat;

    private UserInput() {
    }

    public static UserInput getInstance() {
        if (instance == null) {
            instance = new UserInput();
        }
        return instance;
    }

    public void setValues(int busId, int numberOfBus, TimeFormat timeFormat) {
        this.busId = busId;
        this.numberOfBus = numberOfBus;
        this.timeFormat = timeFormat;
    }

    @Override
    public String toString() {
        String name = GtfsFilesReader.getBusStopName(this.busId);
        return "Postajališče " + name + " (id postaje " + busId + ")";
    }
}