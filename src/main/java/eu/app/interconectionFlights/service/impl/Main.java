package eu.app.interconectionFlights.service.impl;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import eu.app.interconectionFlights.model.Route;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Main {
    private static ArrayList<Route> serviceResult = new ArrayList<>();
    private static final HashMap<String, HashSet<String>> routes = new HashMap<>();

    public static void main(String[] args) {

        String json = readFile("resources/routes.json");
/*
        Gson g = new Gson();
        Route[] r = g.fromJson(json, Route[].class);
        serviceResult = new ArrayList<>(Arrays.asList(r));
*/

//        try (Reader reader = new InputStreamReader(Main.class.getResourceAsStream("resources/routes.json"))) {
            Type type = new TypeToken<List<Route>>() {
            }.getType();
            serviceResult = new Gson().fromJson(json, type);
  /*      } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        populateRoutes();

        List<String> connections = getConnectionAirport("DUB", "MAN");

        for (String airport : connections) {
            System.out.println(airport);
        }
    }

    private static String readFile(String fileName) {
        String result = "";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
        }

        return result;
    }

    private static void populateRoutes() {
        for (Route route :
                serviceResult) {

            if (!routes.containsKey(route.getAirportFrom())) {
                routes.put(route.getAirportFrom(), new HashSet<>());
            }

            HashSet<String> from = routes.get(route.getAirportFrom());

            if (!from.contains(route.getAirportTo())) {
                from.add(route.getAirportTo());
            }
        }
    }

    public static List<String> getConnectionAirport(String from, String to) {
        List<String> result = new ArrayList<>();

        HashSet<String> firstLegFlights = routes.get(from);

        for (String firstLegDestination : firstLegFlights) {
            HashSet<String> secondLegFlights = routes.get(firstLegDestination);

            if (secondLegFlights != null && secondLegFlights.contains(to)) {
                result.add(firstLegDestination);
            }
        }

        return result;
    }
}

