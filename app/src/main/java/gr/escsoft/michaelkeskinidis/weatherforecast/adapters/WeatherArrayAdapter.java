package gr.escsoft.michaelkeskinidis.weatherforecast.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;
import gr.escsoft.michaelkeskinidis.weatherforecast.R;
import gr.escsoft.michaelkeskinidis.weatherforecast.model.ForecastWeatherData;
import gr.escsoft.michaelkeskinidis.weatherforecast.model.WeatherData;
import gr.escsoft.michaelkeskinidis.weatherforecast.modelview.WeatherView;

/**
 * Created by Michael on 8/5/2015.
 */
public class WeatherArrayAdapter extends ArrayAdapter<ForecastWeatherData> {

    List<ForecastWeatherData> weatherDataList;

    public WeatherArrayAdapter(Context context, int resource, int textViewResourceId, List<ForecastWeatherData> objects) {
        super(context, resource, textViewResourceId, objects);
        weatherDataList = objects;
    }

    @Override
    public int getCount() {
        return weatherDataList == null ? 0 : weatherDataList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherView weatherView = null;
        if(convertView == null){
            weatherView = (WeatherView) View.inflate(getContext(), R.layout.view_weather, null);
        } else {
            weatherView = (WeatherView) convertView;
        }

        weatherView.setWeatherData(weatherDataList.get(position));
        return weatherView;
    }
}
