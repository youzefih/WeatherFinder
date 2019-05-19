import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;

import realtimeweb.weatherservice.WeatherService;

/**
 *  Program # 10
 * 
 * This program is a Travel program. It lets the user choose between 4 different US zones, then outputs the temperatures, and description of 
 * 5 cities in the specified zone, with the temperatures sorted depending on the user's selection. Either from warmer
 * to colder or colder to warmer.
 *  CS108-1
 *  06/25/2019
 *  @author  Yousssef Iraqi
  */
public class WeatherReport {
	static Scanner scan = new Scanner(System.in);
	static String[] cities = new String[5];
	static int[] temperature = new int[5];
	static String[] weather = new String[5];
	static HashMap<Integer, String> tempLoc = new HashMap<Integer, String>();
	static HashMap<Integer, String> real = new HashMap<Integer, String>();

	static WeatherService find = new WeatherService();

	// this method returns the cities that are in the region selected by the user
	public static void region() throws InterruptedException {

		System.out.println("Welcome to the Travel Program. \nWhich region are you interested in traveling to?");
		System.out.println("\n 1 for West     2 for midwest\n 3 for south    4 for northeast\n");
		System.out.print("Please enter city number (1-4): ");
		int region = scan.nextInt();

		if (!((region == 1) || (region == 2) || (region == 3) || (region == 4))) {
			System.out.println("Incorrect output, Resetting program \n\n");
			//Thread.sleep(1000);
			region();
		}

		else if (region == 1) {

			cities = new String[] { "Seattle, WA", "Las Vegas, NV", "Los Angeles, CA", "Anchorage, AK",
					"Honolulu, HI" };
		}

		else if (region == 2) {

			cities = new String[] { "Kansas City, MO", "Minneapolis, MN", "Chicago, IL", "Detroit, MI",
					"Indianapolis, IN" };
		}

		else if (region == 3) {
			cities = new String[] { "Miami, FL", "New Orleans, LA", "Atlanta, GA", "Dallas, TX", "Nashville, TN" };
		} else if (region == 4) {
			cities = new String[] { "Philadelphia, PA", "New York, NY", "Boston, MA", "Providence, RI",
					"Hartford, CT" };
		}

	}

	// This method returns the kind of weather the user is looking for
	// organized by either cold to warm or warm to cold

	// returns array of temperatures organized

	public static void climateOrg() throws InterruptedException {

		// temperature = new int[] {77,43,33,99,49}; // test stuff
		// cities = new String[] {"one","three","two","five","four"}; //test stuff
		for (int i = 0; i < cities.length; i++) {
			temperature[i] = find.getReport(cities[i]).getWeather().getTemp();
			Thread.sleep(1500);

		}

		System.out.println("Do you prefer a colder climate or warmer climate?\n" + " 1 for colder    2 for warmer\n");
		System.out.print("Please enter answer here: ");
		int climate = scan.nextInt();

		int i = 0;
		int j = 0;
		int temp = 0;
		if (!((climate == 1) || (climate == 2))) {
			System.out.println("Incorrect output, try again \n\n");
			// Thread.sleep(1000);
			climateOrg();
		}

		else if (climate == 1) { // cold organize by cold to warm
			String c = null;
			for (i = 1; i < 5; ++i) {
				j = i;

				while ((j >= 1) && (temperature[j] < temperature[j - 1])) {

					temp = temperature[j];
					c = cities[j];
					temperature[j] = temperature[j - 1];
					cities[j] = cities[j - 1];
					temperature[j - 1] = temp;
					cities[j - 1] = c;
					--j;
				}
			}
		}

		else if (climate == 2) { // warm organize by warm to cold
			String c = null;
			for (i = 1; i < 5; ++i) {
				j = i;

				while ((j >= 1) && (temperature[j] < temperature[j - 1])) {

					temp = temperature[j];
					c = cities[j];
					temperature[j] = temperature[j - 1];
					cities[j] = cities[j - 1];
					temperature[j - 1] = temp;
					cities[j - 1] = c;
					--j;
				}
			}
			int tempVal;
			String ex;
			// Reverse array's elements
			for (i = 0; i < (temperature.length / 2); ++i) {
				tempVal = temperature[i]; // Temp for swap
				temperature[i] = temperature[temperature.length - 1 - i]; // First part of swap
				ex = cities[i];
				cities[i] = cities[cities.length - 1 - i];
				cities[cities.length - 1 - i] = ex;
				temperature[temperature.length - 1 - i] = tempVal; // Swap complete
			}
		}

		System.out.println("\nHere are the cities you should travel to, organized by your climate preference!\n");

		for (int d = 0; d < temperature.length; d++) {
			Thread.sleep(1000);
			weather[d] = find.getReport(cities[d]).getWeather().getDescription();
			// weather[d] = "test weather";
			// System.out.println("\ncity: " + cities[d] + "\ntemp: " + temperature[d]
			// +"\nweather: " +weather[d] );
		}

	}

	public static void orderOfSteps() throws InterruptedException { // The steps that main needs to follow in order
		region();
		climateOrg();
	}

	private static JsonArray convertToJSON(ArrayList<WeatherService> ans) { // converts to json
		JsonArrayBuilder markerArray = Json.createArrayBuilder(); // Array to hold markers
		// add header row
		JsonArrayBuilder header = Json.createArrayBuilder(); // header row
		header.add("city").add("temperature").add("Description");
		markerArray.add(header);

		for (int i = 0; i < cities.length; i++) {
			JsonArrayBuilder mid = Json.createArrayBuilder(); // header row

			mid.add(temperature[i])
			.add(weather[i])
			.add(cities[i]);

			markerArray.add(mid);

		}

		JsonArray markers = markerArray.build();
		return markers;
	}

	private static void writeToOutputFile(String outputFilename, JsonArray markers) { // writes to json
		try {
			OutputStream os = new FileOutputStream(outputFilename);

			JsonWriter jsonWriter = Json.createWriter(os);
			jsonWriter.writeArray(markers);
			jsonWriter.close();
		} catch (FileNotFoundException fnfe) {
			System.out.println("Unable to open output stream for writing");
			System.exit(0);

		}
	}
	public static String getIdentificationString()  {
		return "Program 10, Youssef Iraqi";
	}

	public static void main(String[] args) throws InterruptedException { // calling the methods + json 
		
		orderOfSteps();

		ArrayList<WeatherService> ans = new ArrayList<WeatherService>();

		String outputFilename = "<Iraqi_Youssef>.json";
		JsonArray testing = convertToJSON(ans);
		writeToOutputFile(outputFilename, testing);
		for (int i = 0; i < weather.length; i++) {
			System.out
					.println("\ncity: " + cities[i] + "\ntemperature: " + temperature[i] + "\nweather: " + weather[i]);

		}
	}

}
