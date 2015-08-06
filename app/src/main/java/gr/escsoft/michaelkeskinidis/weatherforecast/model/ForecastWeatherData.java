package gr.escsoft.michaelkeskinidis.weatherforecast.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Michael on 8/4/2015.
 */
public class ForecastWeatherData implements Parcelable {
    private String symbol;
    private Weather[] weather;
    private Main main;
    private String dt_txt;

    public ForecastWeatherData(){}

    public ForecastWeatherData(String symbol, Weather[] weather, Main main, String dt_txt){
        this.symbol = symbol;
        this.weather = weather;
        this.main = main;
        this.dt_txt = dt_txt;
    }

    public String getSymbol() { return symbol; }

    public void setSymbol(String symbol) { this.symbol = symbol; }

    public Weather[] getWeather() { return weather; }

    public void setWeather(Weather[] weather) { this.weather = weather; }

    public Main getMain() { return main; }

    public void setMain(Main main) { this.main = main; }

    public String getDt_txt() { return dt_txt; }

    public void setDt_txt(String dt_txt) { this.dt_txt = dt_txt; }

    public static final Creator<ForecastWeatherData> CREATOR = new Creator<ForecastWeatherData>() {
        public ForecastWeatherData createFromParcel(Parcel in) {
            return new ForecastWeatherData(in);
        }

        public ForecastWeatherData[] newArray(int size) {
            return new ForecastWeatherData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symbol);
        dest.writeParcelableArray(weather, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeParcelable(main, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeString(dt_txt);
    }

    public ForecastWeatherData(Parcel in){
        symbol = in.readString();
        weather = in.createTypedArray(Weather.CREATOR);
        main = in.readParcelable(Main.class.getClassLoader());
        dt_txt = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ForecastWeatherData))
            return false;

        ForecastWeatherData owd = (ForecastWeatherData) o;
        return weather.equals(owd.weather) && main.equals(owd.main);
    }

    @Override
    public int hashCode() {
        return weather.hashCode() + main.hashCode();
    }

    @Override
    public String toString() {
        return "[Weather: " + weather.toString() + main.toString() + "]";
    }
}
