package gr.escsoft.michaelkeskinidis.weatherforecast.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import gr.escsoft.michaelkeskinidis.weatherforecast.R;
import gr.escsoft.michaelkeskinidis.weatherforecast.fragments.WeatherMapFragment;
import gr.escsoft.michaelkeskinidis.weatherforecast.tasks.DownloadImageTask;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import java.lang.ref.WeakReference;

/**
 * Created by Michael on 8/6/2015.
 */
public class MarkerWeatherWindowAdapter implements InfoWindowAdapter {

    private final WeakReference<Fragment> refFragment;
    private final View markerContents;

    public MarkerWeatherWindowAdapter(Fragment fragment){
        refFragment = new WeakReference<Fragment>(fragment);
        markerContents = refFragment.get().getActivity().getLayoutInflater().inflate(
                R.layout.marker_content, (ViewGroup)refFragment.get().getActivity().findViewById(R.layout.fragment_weather_map)); //(ViewGroup)refFragment.get().getActivity().findViewById(R.layout.fragment_weather_map)
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        if(refFragment != null && refFragment.get() instanceof WeatherMapFragment){
            Fragment frag = refFragment.get();
            if (frag instanceof WeatherMapFragment) {
                WeatherMapFragment weatherMapFragment = (WeatherMapFragment) frag;
                String strTemp = weatherMapFragment.getWeatherData().getMain().getTemp();
                String strMinTemp = weatherMapFragment.getWeatherData().getMain().getTemp_min();
                String strMaxTemp = weatherMapFragment.getWeatherData().getMain().getTemp_max();
                TextView txtDetails = (TextView) markerContents.findViewById(R.id.TxtVwDetailsMC);
                TextView txtCurrTemp = (TextView) markerContents.findViewById(R.id.TxtVwCurrTemperatureMC);
                ImageView imgWeather = (ImageView) markerContents.findViewById(R.id.ImgWeatherImageMC);
                imgWeather.setImageBitmap(weatherMapFragment.getWeatherData().getWeather()[0].getBitmap());

                if (txtDetails != null && !TextUtils.isEmpty(strMinTemp) && !TextUtils.isEmpty(strMaxTemp)) {
                    // Spannable string allows us to edit the formatting of the text.
                    String strDetails = "Min: " + strMinTemp + weatherMapFragment.getWeatherData().getSymbol() + " - " +
                                        "Max: " + strMaxTemp + weatherMapFragment.getWeatherData().getSymbol();
                    SpannableString detailsText = new SpannableString(strDetails);
                    detailsText.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, detailsText.length(), 0);
                    txtDetails.setText(detailsText);

                } else {
                    txtDetails.setText("");
                }

                if (txtCurrTemp != null && !TextUtils.isEmpty(strTemp)) {
                    // Spannable string allows us to edit the formatting of the text.
                    SpannableString cuurTempText = new SpannableString(strTemp + weatherMapFragment.getWeatherData().getSymbol());
                    cuurTempText.setSpan(new ForegroundColorSpan(Color.BLUE), 0, cuurTempText.length(), 0);
                    txtCurrTemp.setText(cuurTempText);

                } else {
                    txtDetails.setText("");
                }
            }
        }
        return markerContents;
    }

//    private Bitmap getWeatherBitmap(String strFileName){
//        String urldisplay = "http://openweathermap.org/img/w/" + strFileName;
//        Bitmap mIcon = null;
//        InputStream in = null;
//        try {
//            in = new java.net.URL(urldisplay).openStream();
//            mIcon = BitmapFactory.decodeStream(in);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            if(in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return mIcon;
//    }
//    @Override
//    public void onInfoWindowClick(Marker marker) {
//
//    }
}
