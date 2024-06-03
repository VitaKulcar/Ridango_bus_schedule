package models;

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

    public static Map<String, List<Bus>> getBusLines(UserInput userInput, LocalTime localTimeNow) {
        List<Bus> buses = new ArrayList<>();
        addTripId(userInput.getBusId(), buses, localTimeNow);
        addRouteId(buses);

        Map<Integer, List<Bus>> routes = buses.stream().collect(Collectors.groupingBy(Bus::getRouteId));
        routes.values().forEach(x -> x.sort(Comparator.comparing(Bus::getAbsoluteTime)));

        Map<String, List<Bus>> busRoutes = new HashMap<>();
        Map<Integer, String> routesNames = getRoutes();
        routes.forEach((key, value) -> {
            if (value.size() > userInput.getNumberOfBus()) {
                String routeName = routesNames.get(key);
                busRoutes.put(routeName, value.subList(0, userInput.getNumberOfBus()));
            }
        });

        return busRoutes;
    }

    private static void addTripId(int busStopId, List<Bus> buses, LocalTime localTime) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/gtfs/stop_times.txt"))) {
            String line = br.readLine(); //header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[3]);
                LocalTime time = LocalTime.parse(parts[2]);
                // Vrni podatke za največ naslednji 2 uri od zahteve.
                if (id == busStopId && time.isAfter(localTime) && time.isBefore(localTime.plusHours(2))) {
                    buses.add(new Bus(parts[0], time));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addRouteId(List<Bus> buses) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/gtfs/trips.txt"))) {
            String line = br.readLine(); //header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[2];
                for (Bus stop : buses) {
                    if (stop.getTripId().equals(id)) {
                        stop.setRouteId(Integer.parseInt(parts[0]));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
