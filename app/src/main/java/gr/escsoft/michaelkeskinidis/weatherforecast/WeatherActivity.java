package gr.escsoft.michaelkeskinidis.weatherforecast;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import gr.escsoft.michaelkeskinidis.weatherforecast.activities.InfoActivity;
import gr.escsoft.michaelkeskinidis.weatherforecast.activities.SetPreferenceActivity;
import gr.escsoft.michaelkeskinidis.weatherforecast.adapters.WeatherFragmentPagerAdapter;
import gr.escsoft.michaelkeskinidis.weatherforecast.listeners.LocationChangeListener;
import gr.escsoft.michaelkeskinidis.weatherforecast.model.ForecastData;
import gr.escsoft.michaelkeskinidis.weatherforecast.model.WeatherData;
import gr.escsoft.michaelkeskinidis.weatherforecast.wetherclient.APIHandler;
import gr.escsoft.michaelkeskinidis.weatherforecast.wetherclient.IWeatherAPI;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WeatherActivity extends AppCompatActivity
                             implements OnSharedPreferenceChangeListener {

    public static final String TAG_FRAG_TODAY_WEATHER = "TAG_FRAG_TODAY_WEATHER";
    public static final String TAG_FRAG_FORECAST = "TAG_FRAG_FORECAST";
    public static final String TAG_FRAG_MAP_WEATHER = "TAG_FRAG_MAP_WEATHER";

    private static final int REQUEST_PREF_RESULT = 1;
    private static final String PREF_SHOW_WEATHER_NEAR_ME = "PREF_SHOW_WEATHER_NEAR_ME";
    private static final String PREF_CITY = "PREF_CITY";
    private static final String PREF_UNITS = "PREF_UNITS";

    private LocationChangeListener locationChangeListener;
    private LocationManager locationManager;
    private Location location;
    private String provider;

    private WeatherFragmentPagerAdapter frgPageAdapter;
    private static final ForecastData forecastData = new ForecastData();
    private static WeatherData todayWeatherData;
    private boolean mbShowWeatherNearMe;
    private String mStrPrefCity;
    private boolean mbIsInCelsius;
    private String mStrUnits = "";
    private String mStrUnitsSymbol = "";
    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        apiKey = getResources().getString(R.string.openweather_api_key);

        loadPrefs();
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        frgPageAdapter = new WeatherFragmentPagerAdapter(getSupportFragmentManager(),
                                                         WeatherActivity.this,
                                                         forecastData, todayWeatherData);
        viewPager.setAdapter(frgPageAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if(status != ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Google Play Services are available
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            provider = locationManager.getBestProvider(criteria, true);
            if(provider != null){
                location = locationManager.getLastKnownLocation(provider);

                locationChangeListener = new LocationChangeListener(this, null);
                locationManager.requestLocationUpdates(provider, 2500, Float.parseFloat("2.0"), locationChangeListener);
                setLocation(locationManager.getLastKnownLocation(provider));
            }
        }

        if ((TextUtils.isEmpty(mStrPrefCity) || mbShowWeatherNearMe) && getLocation() != null)  {
            callWeatherAPILonLat();
        } else if (!TextUtils.isEmpty(mStrPrefCity)){
            callWeatherAPICity();
        } else {
            mStrPrefCity = "Athens";
            callWeatherAPICity();
        }
    }

    public synchronized void setLocation(Location location){
        this.location = location;
    }

    public synchronized Location getLocation(){
        return this.location;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_weather, menu);

        return true;    // super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                Intent intentSetPref = new Intent(this, SetPreferenceActivity.class);
                startActivityForResult(intentSetPref, REQUEST_PREF_RESULT);
                return true;
            case R.id.Itm_SearchAction_Menu:
                return true;
            case R.id.action_infos:
                Intent infoIntent = new Intent(WeatherActivity.this, InfoActivity.class);
                startActivity(infoIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PREF_RESULT){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            if (TextUtils.isEmpty(mStrPrefCity) || mbShowWeatherNearMe) {
                callWeatherAPILonLat();
            } else if (!TextUtils.isEmpty(mStrPrefCity)){
                callWeatherAPICity();
            }
        }
    }

    private void callWeatherAPICity(){

        final IWeatherAPI weatherAPI = APIHandler.getApiInterface();
        mStrUnits = mbIsInCelsius ? "metric" : "";
        mStrUnitsSymbol = mbIsInCelsius ? " \u2103" : " \u2109";

        weatherAPI.getWeatherCity(mStrPrefCity, mStrUnits, apiKey, new Callback<WeatherData>() {

            @Override
            public void success(WeatherData weatherData, retrofit.client.Response response) {
                todayWeatherData = weatherData;
                todayWeatherData.setSymbol(mStrUnitsSymbol);
                frgPageAdapter.updateTodayWeatherData(todayWeatherData);
            }

            @Override
            public void failure(RetrofitError error) {
                String t = error.toString();
            }
        });

        weatherAPI.getForecastCity(mStrPrefCity, mStrUnits, apiKey, new Callback<ForecastData>() {

            @Override
            public void success(ForecastData forecastData, Response response) {
                WeatherActivity.this.forecastData.setCity(forecastData.getCity());
                WeatherActivity.this.forecastData.setList(forecastData.getList());
                WeatherActivity.this.forecastData.setSymbol(mStrUnitsSymbol);
                frgPageAdapter.updateForecastData(forecastData);
            }

            @Override
            public void failure(RetrofitError error) {
                String t = error.toString();
            }
        });
    }

    private void callWeatherAPILonLat(){

        final IWeatherAPI weatherAPI = APIHandler.getApiInterface();
        mStrUnits = mbIsInCelsius ? "metric" : "";
        mStrUnitsSymbol = mbIsInCelsius ? " \u2103" : " \u2109";

        if (getLocation() != null) {
            Double dLon = getLocation().getLongitude();
            Double dLat = getLocation().getLatitude();
            String strLon = "" + dLon;
            String strLat = "" + dLat;
            weatherAPI.getWeatherLatLon(strLat, strLon, mStrUnits, apiKey, new Callback<WeatherData>() {

                @Override
                public void success(WeatherData weatherData, retrofit.client.Response response) {
                    todayWeatherData = weatherData;
                    todayWeatherData.setSymbol(mStrUnitsSymbol);
                    frgPageAdapter.updateTodayWeatherData(todayWeatherData);
                }

                @Override
                public void failure(RetrofitError error) {
                    String t = error.toString();
                }
            });

            weatherAPI.getForecastLatLon(strLat, strLon, mStrUnits, apiKey, new Callback<ForecastData>() {

                @Override
                public void success(ForecastData forecastData, Response response) {
                    WeatherActivity.this.forecastData.setCity(forecastData.getCity());
                    WeatherActivity.this.forecastData.setList(forecastData.getList());
                    WeatherActivity.this.forecastData.setSymbol(mStrUnitsSymbol);
                    frgPageAdapter.updateForecastData(forecastData);
                }

                @Override
                public void failure(RetrofitError error) {
                    String t = error.toString();
                }
            });
        }
    }

    private void loadPrefs(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        mStrPrefCity = prefs.getString(PREF_CITY, "");
        mbIsInCelsius = prefs.getBoolean(PREF_UNITS, true);
        mbShowWeatherNearMe = prefs.getBoolean(PREF_SHOW_WEATHER_NEAR_ME, true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        switch (key) {
            case PREF_CITY:
                mStrPrefCity = prefs.getString(PREF_CITY, "");
                break;
            case PREF_UNITS:
                mbIsInCelsius = prefs.getBoolean(PREF_UNITS, false);
                break;
            case PREF_SHOW_WEATHER_NEAR_ME:
                mbShowWeatherNearMe = prefs.getBoolean(PREF_SHOW_WEATHER_NEAR_ME, true);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.getString(PREF_CITY, mStrPrefCity);
        prefs.getBoolean(PREF_UNITS, mbIsInCelsius);
        prefs.getBoolean(PREF_SHOW_WEATHER_NEAR_ME, mbShowWeatherNearMe);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }
}
