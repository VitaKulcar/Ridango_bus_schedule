package com.example.Ridango_bus_schedule;

import models.Bus;
import models.GtfsFilesReader;
import models.TimeFormat;
import models.UserInput;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RidangoBusSchedule {
    private static final LocalTime LOCAL_TIME_NOW = LocalTime.of(12, 0);
    private static final UserInput USER_INPUT_INSTANCE = UserInput.getInstance();

    public static void main(String[] args) {
        if (args.length == 3) {
            try {
                int id = Integer.parseInt(args[0]);
                int number = Integer.parseInt(args[1]);
                TimeFormat time = TimeFormat.valueOf(args[2]);
                USER_INPUT_INSTANCE.setValues(id, number, time);
                System.out.println(USER_INPUT_INSTANCE);
            } catch (NumberFormatException e) {
                System.out.println("Napaka: id_postajališča in število_avtobusov morata biti števili.");
            } catch (IllegalArgumentException e) {
                System.out.println("Napaka: časovni format more biti 'absolute' ali 'relative'.");
            }

            Map<String, List<Bus>> busStops = GtfsFilesReader.getBusLines(USER_INPUT_INSTANCE, LOCAL_TIME_NOW);
            if (!busStops.isEmpty()) {
                printRoutes(busStops, USER_INPUT_INSTANCE.getTimeFormat());
            } else System.out.println("Ni zadetkov iskanja");
        }
    }

    private static void printRoutes(Map<String, List<Bus>> busStops, TimeFormat timeFormat) {
        busStops.forEach((routeId, values) -> {
            String formattedTimes = values.stream()
                    .map(bus -> TimeFormat.relative == timeFormat ? bus.getRelativeTimeString(RidangoBusSchedule.LOCAL_TIME_NOW) : bus.getAbsoluteTimeString()).collect(Collectors.joining(", "));
            System.out.println(routeId + ": " + formattedTimes);
        });
    }
}
