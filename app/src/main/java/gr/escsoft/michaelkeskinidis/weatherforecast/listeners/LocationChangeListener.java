package gr.escsoft.michaelkeskinidis.weatherforecast.listeners;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.lang.ref.WeakReference;

import gr.escsoft.michaelkeskinidis.weatherforecast.WeatherActivity;

/**
 * Created by Michael on 8/6/2015.
 */
public class LocationChangeListener implements LocationListener {

    private final WeakReference<Activity> mWrActivity;
    private WeakReference<GoogleMap> mWrGoogleMap;

    public LocationChangeListener(Activity activity, GoogleMap googleMap){
        this.mWrActivity = new WeakReference<Activity>(activity);
        if(googleMap != null)
            this.mWrGoogleMap = new WeakReference<GoogleMap>(googleMap);
    }

    public void setGoogleMap(GoogleMap googleMap){
        if(googleMap != null)
            this.mWrGoogleMap = new WeakReference<GoogleMap>(googleMap);

    }

    @Override
    public void onLocationChanged(Location location) {

        if(location != null){
            if(mWrActivity != null && mWrActivity.get() instanceof WeatherActivity){
                ((WeatherActivity)mWrActivity.get()).setLocation(location);
            }
            if(mWrGoogleMap != null && mWrGoogleMap.get() != null){
                float zoom = mWrGoogleMap.get().getCameraPosition().zoom;
                // Move the camera instantly with a zoom of 15.
                mWrGoogleMap.get().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
                // Zoom in, animating the camera.
                mWrGoogleMap.get().animateCamera(CameraUpdateFactory.zoomTo(zoom), 2000, null);
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
