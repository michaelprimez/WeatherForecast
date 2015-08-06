package gr.escsoft.michaelkeskinidis.weatherforecast.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import gr.escsoft.michaelkeskinidis.weatherforecast.R;
import gr.escsoft.michaelkeskinidis.weatherforecast.adapters.WeatherFragmentPagerAdapter;
import gr.escsoft.michaelkeskinidis.weatherforecast.model.WeatherData;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

/**
 * Created by Michael on 8/5/2015.
 */
public class WeatherMapFragment extends Fragment implements WeatherFragmentPagerAdapter.OnUpdateMapFragmentListener {

    android.app.Fragment fr;
    private GoogleMap googleMap;

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
//                MarkerInfoWindowAdapter markerInfoWindowAdapter = new MarkerInfoWindowAdapter(this);
//                googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);
//                googleMap.setOnInfoWindowClickListener(markerInfoWindowAdapter);
//                ((MainActivity)getActivity()).getLocationChangeListener().setGoogleMap(googleMap);
            }
        }

        return rootView;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onUpdateMap(WeatherData weatherData) {

    }
}
