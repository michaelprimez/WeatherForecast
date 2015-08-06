package gr.escsoft.michaelkeskinidis.weatherforecast.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.ArrayList;
import java.util.List;

import gr.escsoft.michaelkeskinidis.weatherforecast.fragments.ForecastFragment;
import gr.escsoft.michaelkeskinidis.weatherforecast.fragments.WeatherMapFragment;
import gr.escsoft.michaelkeskinidis.weatherforecast.fragments.WeatherTodayFragment;
import gr.escsoft.michaelkeskinidis.weatherforecast.model.ForecastData;
import gr.escsoft.michaelkeskinidis.weatherforecast.model.WeatherData;

/**
 * Created by Michael on 8/4/2015.
 */
public class WeatherFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "Today", "Forecast", "Map" };
    public static final int PAGE_COUNT = 3;
    private Context context;
    private final static ForecastData forecastData = new ForecastData();
    private WeatherData todayWeatherData;

    public WeatherFragmentPagerAdapter(FragmentManager fm, Context context,
                                       ForecastData forecastData, WeatherData todayWeatherData) {
        super(fm);
        this.context = context;
        this.forecastData.setCity(forecastData.getCity());
        this.forecastData.setList(forecastData.getList());
        this.todayWeatherData = todayWeatherData;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return WeatherTodayFragment.newInstance(todayWeatherData);
            case 1:
                return ForecastFragment.newInstance(forecastData);
            case 2:
                return WeatherMapFragment.newInstance(todayWeatherData);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    public interface OnUpdateTodayWeatherFragmentListener {
        public void onUpdateTodayWeather(WeatherData weatherData);
    }

    public interface OnUpdateForecastFragmentListener {
        public void onUpdateForecast(ForecastData forecastData);
    }

    public interface OnUpdateMapFragmentListener {
        public void onUpdateMap(WeatherData weatherData);
    }

    public void updateTodayWeatherData(WeatherData weatherData){
        this.todayWeatherData = weatherData;
        notifyDataSetChanged();
    }

    public void updateForecastData(ForecastData forecastData){
        this.forecastData.setList(forecastData.getList());
        this.forecastData.setCity(forecastData.getCity());
        notifyDataSetChanged();
    }

    public void updateMapData(WeatherData weatherDataList){
        this.todayWeatherData = weatherDataList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof OnUpdateTodayWeatherFragmentListener) {

            ((OnUpdateTodayWeatherFragmentListener) object).onUpdateTodayWeather(todayWeatherData);

        } else if (object instanceof OnUpdateForecastFragmentListener) {

            ((OnUpdateForecastFragmentListener) object).onUpdateForecast(forecastData);

        } else if (object instanceof OnUpdateMapFragmentListener) {

            ((OnUpdateMapFragmentListener) object).onUpdateMap(todayWeatherData);

        }
        //WARNING: returning POSITION_NONE, cause fragment recreation.
        return super.getItemPosition(object);
    }
}
