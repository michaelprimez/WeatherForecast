package gr.escsoft.michaelkeskinidis.weatherforecast.wetherclient;

import java.util.List;

import gr.escsoft.michaelkeskinidis.weatherforecast.model.ForecastData;
import gr.escsoft.michaelkeskinidis.weatherforecast.model.WeatherData;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Michael on 8/4/2015.
 */
public interface IWeatherAPI {
    @GET("/weather")
    void getWeatherCity(@Query("q") String cities, @Query("units") String units, @Query("APPID") String apiKey, Callback<WeatherData> callback);

    @GET("/weather")
    void getWeatherLatLon(@Query("lat") String lat, @Query("lon") String lon, @Query("units") String units, @Query("APPID") String apiKey, Callback<WeatherData> callback);

    @GET("/forecast")
    void getForecastCity(@Query("q") String cities, @Query("units") String units, @Query("APPID") String apiKey, Callback<ForecastData> callback);

    @GET("/forecast")
    void getForecastLatLon(@Query("lat") String lat, @Query("lon") String lon, @Query("units") String units, @Query("APPID") String apiKey, Callback<ForecastData> callback);
}
