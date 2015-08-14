package gr.escsoft.michaelkeskinidis.weatherforecast.fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import gr.escsoft.michaelkeskinidis.weatherforecast.R;
import gr.escsoft.michaelkeskinidis.weatherforecast.WeatherActivity;
import gr.escsoft.michaelkeskinidis.weatherforecast.adapters.MarkerWeatherWindowAdapter;
import gr.escsoft.michaelkeskinidis.weatherforecast.adapters.WeatherFragmentPagerAdapter;
import gr.escsoft.michaelkeskinidis.weatherforecast.eventbus.BusProvider;
import gr.escsoft.michaelkeskinidis.weatherforecast.model.WeatherData;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Michael on 8/5/2015.
 */
public class WeatherMapFragment extends Fragment implements WeatherFragmentPagerAdapter.OnUpdateMapFragmentListener {

    private static final String ARG_WEATHER_DATA = "ARG_WEATHER_DATA";
    android.app.Fragment fr;
    private GoogleMap googleMap;
    private WeatherData weatherData;

    private static class ImageAvailableEvent {
        public final Bitmap image;

        ImageAvailableEvent(Bitmap image) {
            this.image = image;
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param weatherData Parameter 1.
     * @return A new instance of fragment WeatherTodayFragment.
     */
    public static WeatherMapFragment newInstance(WeatherData weatherData) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_WEATHER_DATA, weatherData);
        WeatherMapFragment fragment = new WeatherMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public WeatherMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onImageAvailable(ImageAvailableEvent event) {
        if (WeatherMapFragment.this.weatherData != null &&
                WeatherMapFragment.this.weatherData.getWeather() != null &&
                WeatherMapFragment.this.weatherData.getWeather().length > 0 &&
                WeatherMapFragment.this.weatherData.getWeather()[0] != null) {
            WeatherMapFragment.this.weatherData.getWeather()[0].setBitmap(event.image);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            weatherData = getArguments().getParcelable(ARG_WEATHER_DATA);
        }
    }

    public WeatherData getWeatherData(){
        return weatherData;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather_map, container, false);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if(status != ConnectionResult.SUCCESS){ // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
            dialog.show();
        }else{
            fr = (android.app.Fragment)getActivity().getFragmentManager().findFragmentById(R.id.map);
            googleMap = (GoogleMap)((MapFragment)fr).getMap();
            if (googleMap != null) {
                googleMap.setMyLocationEnabled(true);
                MarkerWeatherWindowAdapter markerInfoWindowAdapter = new MarkerWeatherWindowAdapter(this);
                googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);
                ((WeatherActivity)getActivity()).getLocationChangeListener().setGoogleMap(googleMap);
            }
        }

        return rootView;
    }

    @Override
    public void onUpdateMap(WeatherData weatherData) {
        this.weatherData = weatherData;

        if (weatherData != null && weatherData.getWeather() != null && weatherData.getWeather().length > 0 && weatherData.getWeather()[0] != null) {
            new AsyncTask<String, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... params) {
                    Bitmap mIcon = null;
                    if (params != null && params.length > 0 && !TextUtils.isEmpty(params[0])) {
                        String urldisplay = "http://openweathermap.org/img/w/" + params[0];
                        InputStream in = null;
                        try {
                            in = new java.net.URL(urldisplay).openStream();
                            mIcon = BitmapFactory.decodeStream(in);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    return mIcon;
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    if (result != null) {
                        if (WeatherMapFragment.this.weatherData != null &&
                                WeatherMapFragment.this.weatherData.getWeather() != null &&
                                WeatherMapFragment.this.weatherData.getWeather().length > 0 &&
                                WeatherMapFragment.this.weatherData.getWeather()[0] != null) {
                            //WeatherMapFragment.this.weatherData.getWeather()[0].setBitmap(result);
                            BusProvider.getInstance().post(new ImageAvailableEvent(result));
                        }
                    }
                }
            }.execute(weatherData.getWeather()[0].getIcon() + ".png");
        }

        if (googleMap != null)
            googleMap.clear();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setMarker();
            }
        });
    }

    public Marker setMarker(){
        if (weatherData != null && weatherData.getCoord() != null
                && weatherData.getCoord().getLat() > 0 && weatherData.getCoord().getLon() > 0) {
            LatLng location = new LatLng(Double.valueOf(weatherData.getCoord().getLat()), Double.valueOf(weatherData.getCoord().getLon()));
            if (location != null && location.latitude > 0 && location.longitude > 0) {
                Marker marker = null;
                if (googleMap != null)
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)) //BitmapDescriptorFactory.fromResource(R.drawable.green_pin_marker)
                        .title(weatherData.getName())
                        .snippet(weatherData.getMain().getTemp()));

                return marker;
            }
        }
        return null;
    }
}
