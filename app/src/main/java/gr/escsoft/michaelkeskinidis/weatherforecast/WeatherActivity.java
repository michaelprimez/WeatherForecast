package gr.escsoft.michaelkeskinidis.weatherforecast;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.MapsInitializer;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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
    private Timer timer;
    private TimerTask timerTask;
    private final Handler handler = new Handler();
    private EditText myActionEditText;
    private MenuItem myActionMenuItem;
    String mStrFilterWord;

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
        MapsInitializer.initialize(getApplicationContext());
        loadPrefs();
        setupView();
        updateWeatherBasedOnCriteria();

    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimerTask();
    }

    private void setupView(){
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        frgPageAdapter = new WeatherFragmentPagerAdapter(getSupportFragmentManager(),
                WeatherActivity.this,
                forecastData, todayWeatherData);
        viewPager.setAdapter(frgPageAdapter);
        viewPager.setOffscreenPageLimit(WeatherFragmentPagerAdapter.PAGE_COUNT);

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
                locationManager.requestLocationUpdates(provider, 10000, Float.parseFloat("100.0"), locationChangeListener);
                setLocation(locationManager.getLastKnownLocation(provider));
            }
        }
    }

    public void updateWeatherBasedOnCriteria(){
        if (!TextUtils.isEmpty(mStrFilterWord)){
            mStrPrefCity = mStrFilterWord;
            callWeatherAPICity();
        }else {
            if ((TextUtils.isEmpty(mStrPrefCity) || mbShowWeatherNearMe) && getLocation() != null)  {
                callWeatherAPILonLat();
            } else if (!TextUtils.isEmpty(mStrPrefCity)){
                callWeatherAPICity();
            } else {
                mStrPrefCity = "Athens";
                callWeatherAPICity();
            }
        }
    }

    private void updatePrefs(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mStrPrefCity = prefs.getString(PREF_CITY, "");
        mbIsInCelsius = prefs.getBoolean(PREF_UNITS, true);
        mbShowWeatherNearMe = prefs.getBoolean(PREF_SHOW_WEATHER_NEAR_ME, true);
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, 10000);
    }

    public void stopTimerTask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        updateWeatherBasedOnCriteria();
                    }
                });
            }
        };
    }

    public synchronized void setLocation(Location location){
        this.location = location;
    }

    public synchronized Location getLocation(){
        return this.location;
    }

    public LocationChangeListener getLocationChangeListener(){
        return locationChangeListener;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_weather, menu);

        // Here we get the action view we defined
        myActionMenuItem = menu.findItem(R.id.Itm_SearchAction_Menu);
        View actionView = myActionMenuItem.getActionView();

        // We then get the edit text view that is part of the action view
        if(actionView != null) {
            myActionEditText = (EditText) actionView.findViewById(R.id.myActionEditText);
            if(myActionEditText != null) {
                myActionEditText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String strWord = myActionEditText.getText().toString();
                        if (TextUtils.isEmpty(strWord)) {
                            mStrFilterWord = "";
                            updatePrefs();
                        } else {
                            mStrFilterWord = strWord.toUpperCase(Locale.ENGLISH);
                        }
                        updateWeatherBasedOnCriteria();
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }

        return super.onCreateOptionsMenu(menu);
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
            ShowEnableLocationDialog(mbShowWeatherNearMe);
            updateWeatherBasedOnCriteria();
        }
    }

    private void ShowEnableLocationDialog(boolean show){
        if (show && !isLocationEnabled(this)){
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(this.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(this.getResources().getString(R.string.open_location_settings),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                            Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            WeatherActivity.this.startActivity(myIntent);
                        }
                    });
            dialog.setNegativeButton(this.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
            dialog.show();
        }
    }
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
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
