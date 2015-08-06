package gr.escsoft.michaelkeskinidis.weatherforecast.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import gr.escsoft.michaelkeskinidis.weatherforecast.R;
import gr.escsoft.michaelkeskinidis.weatherforecast.adapters.WeatherFragmentPagerAdapter;
import gr.escsoft.michaelkeskinidis.weatherforecast.model.WeatherData;
import gr.escsoft.michaelkeskinidis.weatherforecast.tasks.DownloadImageTask;

public class WeatherTodayFragment extends Fragment
                                  implements WeatherFragmentPagerAdapter.OnUpdateTodayWeatherFragmentListener {

    public static final String ARG_TODAY_WEATHER_DATA = "ARG_TODAY_WEATHER_DATA";
    private WeatherData weatherData;

    private TextView mTxtVwCity;
    private TextView mTxtVwUpdated;
    private ImageView mTxtVwWeatherIcon;
    private TextView mTxtVwCurrentTemperature;
    private TextView mTxtVwDescription;
    private TextView mTxtVwHumidity;
    private TextView mTxtVwDetails;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param weatherData Parameter 1.
     * @return A new instance of fragment WeatherTodayFragment.
     */
    public static WeatherTodayFragment newInstance(WeatherData weatherData) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_TODAY_WEATHER_DATA, weatherData);
        WeatherTodayFragment fragment = new WeatherTodayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public WeatherTodayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weatherData = getArguments().getParcelable(ARG_TODAY_WEATHER_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_today, container, false);
        mTxtVwCity = (TextView) view.findViewById(R.id.TxtVwCity);
        mTxtVwUpdated = (TextView) view.findViewById(R.id.TxtVwUpdated);
        mTxtVwWeatherIcon = (ImageView) view.findViewById(R.id.TxtVwWeatherIcon);
        mTxtVwCurrentTemperature = (TextView) view.findViewById(R.id.TxtVwCurrentTemperature);
        mTxtVwDescription = (TextView) view.findViewById(R.id.TxtVwDescription);
        mTxtVwHumidity = (TextView) view.findViewById(R.id.TxtVwHumidity);
        mTxtVwDetails = (TextView) view.findViewById(R.id.TxtVwDetails);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onUpdateTodayWeather(WeatherData weatherData) {
        this.weatherData = weatherData;
        if(this.weatherData != null) {
            if (weatherData.getName() != null && weatherData.getSys() != null)
                mTxtVwCity.setText(weatherData.getName().toUpperCase() + ", " + weatherData.getSys().getCountry());

            if (weatherData.getDt() > 0) {
                DateFormat df = DateFormat.getDateTimeInstance();
                String updatedOn = df.format(new Date(weatherData.getDt() * 1000));
                mTxtVwUpdated.setText("Last update: " + updatedOn);
            }
            if (weatherData.getWeather() != null && weatherData.getWeather().length > 0 && weatherData.getWeather()[0] != null) {
                new DownloadImageTask(mTxtVwWeatherIcon).execute(weatherData.getWeather()[0].getIcon() + ".png");
                mTxtVwDescription.setText(weatherData.getWeather()[0].getDescription().toUpperCase());
            }

            if (weatherData.getMain() != null)
                mTxtVwCurrentTemperature.setText(weatherData.getMain().getTemp() + weatherData.getSymbol());

            if (weatherData.getMain() != null) {
                mTxtVwHumidity.setText("Humidity: " + weatherData.getMain().getHumidity() + " %");
                mTxtVwDetails.setText("Min: " + weatherData.getMain().getTemp_min() + weatherData.getSymbol() + " - " +
                        "Max: " + weatherData.getMain().getTemp_max() + weatherData.getSymbol());
            }
        }
    }
}
