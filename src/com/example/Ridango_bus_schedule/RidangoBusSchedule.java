package com.example.Ridango_bus_schedule;

import java.time.LocalTime;
import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RidangoBusSchedule {

    private static LocalTime localTimeNow = LocalTime.of(12, 0);

    public static void main(String[] args) {
        System.out.println("Pozdravljeni!");
        UserInput userInput = userInputReader();

        Map<String, List<BusStop>> busStops = GtfsFilesReader.getBusLines(userInput, localTimeNow);
        if (!busStops.isEmpty()) {
            printRoutes(busStops, userInput.timeFormat, localTimeNow);
        } else System.out.println("Ni zadetkov iskanja");
    }

    private static UserInput userInputReader() {
        System.out.println("Vnesite podatke: id_postajališča število_avtobusov absolute/relative");
        UserInput userInput = UserInput.getInstance();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] parts = input.split(" ");
        if (parts.length == 3) {
            try {
                int id = Integer.parseInt(parts[0]);
                int number = Integer.parseInt(parts[1]);
                TimeFormat time = TimeFormat.valueOf(parts[2].toLowerCase());

                userInput.setValues(id, number, time);
                System.out.println(userInput);
            } catch (NumberFormatException e) {
                System.out.println("Napaka: id_postajališča in število_avtobusov morata biti števili.");
            } catch (IllegalArgumentException e) {
                System.out.println("Napaka: časovni format more biti 'absolute' ali 'relative'.");
            }
        } else {
            System.out.println("Napaka, vnesite točno tri argumente ločene z belimi presledki.");
        }
        scanner.close();
        return userInput;
    }

    private static void printRoutes(Map<String, List<BusStop>> busStops, TimeFormat timeFormat, LocalTime localTimeNow) {
        busStops.forEach((routeId, values) -> {
            String formattedTimes = values.stream().map(busStop -> TimeFormat.relative == timeFormat ? busStop.getRelativeTimeString(localTimeNow) : busStop.getAbsoluteTimeString()).collect(Collectors.joining(", "));
            System.out.println(routeId + ": " + formattedTimes);
        });
    }
}
