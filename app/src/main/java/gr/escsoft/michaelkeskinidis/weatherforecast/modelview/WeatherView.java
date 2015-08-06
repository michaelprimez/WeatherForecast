package gr.escsoft.michaelkeskinidis.weatherforecast.modelview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import gr.escsoft.michaelkeskinidis.weatherforecast.R;
import gr.escsoft.michaelkeskinidis.weatherforecast.model.ForecastWeatherData;
import gr.escsoft.michaelkeskinidis.weatherforecast.model.WeatherData;
import gr.escsoft.michaelkeskinidis.weatherforecast.tasks.DownloadImageTask;

/**
 * Created by Michael on 8/5/2015.
 */
public class WeatherView extends RelativeLayout {
    private ForecastWeatherData weatherData;
    private ImageView mImgView;
    private TextView mTxtVwDate;
    private TextView mTxtVwDescription;
    private TextView mTxtVwDetails;
    private TextView mTxtVwCurrTemperature;

    public WeatherView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mImgView = (ImageView) findViewById(R.id.ImgWeatherImage);
        mTxtVwDate = (TextView) findViewById(R.id.TxtVwDate);
        mTxtVwDescription = (TextView) findViewById(R.id.TxtVwDescription);
        mTxtVwDetails = (TextView) findViewById(R.id.TxtVwDetails);
        mTxtVwCurrTemperature = (TextView) findViewById(R.id.TxtVwCurrTemperature);
    }

    public void setWeatherData(ForecastWeatherData weather){
        this.weatherData = weather;
        if(this.weatherData != null) {
            new DownloadImageTask(mImgView).execute(weatherData.getWeather()[0].getIcon() + ".png");
            mTxtVwDate.setText(weatherData.getDt_txt());
            mTxtVwDescription.setText(weatherData.getWeather()[0].getDescription().toUpperCase());
            mTxtVwDetails.setText("Min: " + weatherData.getMain().getTemp_min() + weatherData.getSymbol() + " - " +
                                  "Max: " + weatherData.getMain().getTemp_max() + weatherData.getSymbol());
            mTxtVwCurrTemperature.setText(weatherData.getMain().getTemp() + weatherData.getSymbol());
        }
    }
}
