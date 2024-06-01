package com.example.Ridango_bus_schedule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class GtfsFilesReader {
    public static String getBusStopName(int busId) {
        String StationName = "";
        try (BufferedReader br = new BufferedReader(new FileReader("src/gtfs/stops.txt"))) {
            String line = br.readLine(); //header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                if (id == busId) {
                    StationName = parts[2];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return StationName;
    }

    public static Map<String, List<BusStop>> getBusLines(UserInput userInput, LocalTime localTimeNow) {
        List<BusStop> busStops = new ArrayList<>();
        addTripId(userInput.busId, busStops, localTimeNow);
        addRouteId(busStops);

        Map<Integer, List<BusStop>> routes = busStops.stream().collect(Collectors.groupingBy(BusStop::getRouteId));
        routes.values().forEach(x -> x.sort(Comparator.comparing(BusStop::getAbsoluteTime)));

        Map<String, List<BusStop>> busRoutes = new HashMap<>();
        Map<Integer, String> routesNames = getRoutes();
        routes.forEach((key, value) -> {
            if (value.size() > userInput.numberOfBus) {
                String routeName = routesNames.get(key);
                busRoutes.put(routeName, value.subList(0, userInput.numberOfBus));
            }
        });

        return busRoutes;
    }

    private static Map<Integer, String> getRoutes() {
        Map<Integer, String> routes = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/gtfs/routes.txt"))) {
            String line = br.readLine(); //header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[2];
                routes.put(id, name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return routes;
    }

    private static void addTripId(int busStopId, List<BusStop> busStops, LocalTime localTime) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/gtfs/stop_times.txt"))) {
            String line = br.readLine(); //header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[3]);
                LocalTime time = LocalTime.parse(parts[2]);
                // Vrni podatke za največ naslednji 2 uri od zahteve.
                if (id == busStopId && time.isAfter(localTime) && time.isBefore(localTime.plusHours(2))) {
                    busStops.add(new BusStop(parts[0], time));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addRouteId(List<BusStop> busStops) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/gtfs/trips.txt"))) {
            String line = br.readLine(); //header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[2];
                for (BusStop stop : busStops) {
                    if (stop.getTripId().equals(id)) {
                        stop.setRouteId(Integer.parseInt(parts[0]));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
