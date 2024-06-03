package models;

public class UserInput {
    private static UserInput instance;
    private int busId;
    private int numberOfBus;
    private TimeFormat timeFormat;

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

    public int getBusId() {
        return busId;
    }

    public int getNumberOfBus() {
        return numberOfBus;
    }

    public TimeFormat getTimeFormat() {
        return timeFormat;
    }

    @Override
    public String toString() {
        String name = GtfsFilesReader.getBusStopName(this.busId);
        return "Postajališče " + name;
    }
}