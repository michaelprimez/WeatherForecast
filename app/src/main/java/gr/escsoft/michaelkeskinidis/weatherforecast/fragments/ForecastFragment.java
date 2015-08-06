package gr.escsoft.michaelkeskinidis.weatherforecast.fragments;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gr.escsoft.michaelkeskinidis.weatherforecast.R;
import gr.escsoft.michaelkeskinidis.weatherforecast.adapters.WeatherArrayAdapter;
import gr.escsoft.michaelkeskinidis.weatherforecast.adapters.WeatherFragmentPagerAdapter;
import gr.escsoft.michaelkeskinidis.weatherforecast.model.ForecastData;
import gr.escsoft.michaelkeskinidis.weatherforecast.model.ForecastWeatherData;
import gr.escsoft.michaelkeskinidis.weatherforecast.model.WeatherData;

public class ForecastFragment extends ListFragment implements WeatherFragmentPagerAdapter.OnUpdateForecastFragmentListener {

    public static final String ARG_FORECAST_DATA = "ARG_FORECAST_DATA";
    private static final ForecastData forecastData = new ForecastData();
    private WeatherArrayAdapter weatherAdapter;

    public static ForecastFragment newInstance(ForecastData forecastData){
        Bundle args = new Bundle();
        args.putParcelable(ARG_FORECAST_DATA, forecastData);
        ForecastFragment weatherPageFragment = new ForecastFragment();
        weatherPageFragment.setArguments(args);
        return weatherPageFragment;
    }

    public ForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ForecastData tmpForecastData = getArguments().getParcelable(ARG_FORECAST_DATA);
            forecastData.setCity(tmpForecastData.getCity());
            forecastData.setList(tmpForecastData.getList());
        }
        weatherAdapter = new WeatherArrayAdapter(getActivity(), 0, R.layout.view_weather, forecastData.getList());
        setListAdapter(weatherAdapter);
    }

    @Override
    public void onUpdateForecast(ForecastData forecastData) {

        this.forecastData.setCity(forecastData.getCity());
        this.forecastData.setList(forecastData.getList());

        weatherAdapter.addAll(this.forecastData.getList());
        weatherAdapter.notifyDataSetChanged();
    }
}
