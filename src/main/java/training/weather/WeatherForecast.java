package training.weather;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherForecast {

	String searchQuery = "https://www.metaweather.com/api/location/search/?query=";
	String searchLocation = "https://www.metaweather.com/api/location/";
	String strLocation, idLocation, idEarthPosition, weatherStates, resultStateWeather;
	JSONArray jsonResult, jsonLocation, jsonWeather;
	HttpRequest requestLocation;
	HttpRequestFactory transportFactory = new NetHttpTransport().createRequestFactory();
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	boolean equals;


	public String getCityWeather(String city, Date datetime) throws IOException {

		if (datetime == null) {
			datetime = new Date();
		}

		if (datetime.before(new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * 6)))) {

			idEarthPosition = getIdPosition(searchQuery, city);
			jsonWeather = getLocation(searchLocation, idEarthPosition);
			weatherStates = getStateWeather(datetime, jsonWeather);
		}
		return weatherStates;
	}

	public String getStateWeather(Date datetime, JSONArray jsonWeather) {

		for (int i = 0; i < jsonWeather.length(); i++) {
			equals = dateFormat.format(datetime)
					.equals(jsonWeather.getJSONObject(i).get("applicable_date").toString());

			if (equals) {
				resultStateWeather = jsonWeather.getJSONObject(i).get("weather_state_name").toString();
			}
		}
		return resultStateWeather;
	}

	public JSONArray getLocation(String searchLocation, String idEarthPosition)
			throws IOException {

		requestLocation = transportFactory.buildGetRequest(new GenericUrl(searchLocation + idEarthPosition));
		strLocation = requestLocation.execute().parseAsString();
		jsonResult = new JSONObject(strLocation).getJSONArray("consolidated_weather");

		return jsonResult;
	}

	public String getIdPosition(String searchQuery, String city)
			throws IOException {

		requestLocation = transportFactory.buildGetRequest(new GenericUrl(searchQuery + city));
		strLocation = requestLocation.execute().parseAsString();
		jsonLocation = new JSONArray(strLocation);
		idLocation = jsonLocation.getJSONObject(0).get("woeid").toString();

		return idLocation;
	}

}
