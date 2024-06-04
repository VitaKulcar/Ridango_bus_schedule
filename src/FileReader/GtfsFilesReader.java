package FileReader;

import models.Bus;
import models.UserInput;

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
            System.out.println("napaka v funkciji getBusStopName");
        }
        return StationName;
    }

    public static Map<String, List<Bus>> getBusLines(UserInput userInput, LocalTime localTimeNow, int timeSpan) {
        List<Bus> buses = new ArrayList<>();
        addTripId(userInput.getBusId(), buses, localTimeNow, timeSpan);
        addRouteId(buses);

        Map<Integer, List<Bus>> routes = buses.stream().collect(Collectors.groupingBy(Bus::getRouteId));
        routes.values().forEach(x -> x.sort(Comparator.comparing(Bus::getAbsoluteTime)));

        Map<String, List<Bus>> busRoutes = new HashMap<>();
        Map<Integer, String> routesNames = getRoutes();
        int numOfBus = userInput.getNumberOfBus();
        routes.forEach((key, value) -> {
            String routeName = routesNames.get(key);
            if (value.size() > numOfBus) busRoutes.put(routeName, value.subList(0, userInput.getNumberOfBus()));
            else busRoutes.put(routeName, value.subList(0, value.size()));
        });

        return busRoutes;
    }

    private static void addTripId(int busStopId, List<Bus> buses, LocalTime localTime, int timeSpan) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/gtfs/stop_times.txt"))) {
            String line = br.readLine(); //header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[3]);
                LocalTime time = LocalTime.parse(parts[2]);
                if (id == busStopId && time.isAfter(localTime) && time.isBefore(localTime.plusHours(timeSpan))) {
                    buses.add(new Bus(parts[0], time));
                }
            }
        } catch (IOException e) {
            System.out.println("napaka v funkciji addTripId");
        }
    }

    private static void addRouteId(List<Bus> buses) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/gtfs/trips.txt"))) {
            String line = br.readLine(); //header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[2];
                for (Bus bus : buses) {
                    if (bus.getTripId().equals(id)) {
                        bus.setRouteId(Integer.parseInt(parts[0]));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("napaka v funkciji addRputeId");

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
            System.out.println("napaka v funkciji getRoutes");
        }
        return routes;
    }
}
