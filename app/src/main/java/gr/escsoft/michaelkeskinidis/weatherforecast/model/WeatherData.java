package gr.escsoft.michaelkeskinidis.weatherforecast.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Michael on 8/4/2015.
 */
public class WeatherData implements Parcelable {
    private String symbol;
    private String name;
    private Coord coord;
    private Weather[] weather;
    private Main main;
    private Sys sys;
    private long dt;

    public WeatherData(){}

    public WeatherData(String symbol, String name, Coord coord, Weather[] weather, Main main, Sys sys, long dt){
        this.symbol = symbol;
        this.name = name;
        this.coord = coord;
        this.weather = weather;
        this.main = main;
        this.sys = sys;
        this.dt = dt;
    }

    public String getSymbol() { return symbol; }

    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getName() {
        return name;
    }

    public Coord getCoord() { return coord; }

    public void setCoord(Coord coord) { this.coord = coord; }

    public Weather[] getWeather() { return weather; }

    public void setWeather(Weather[] weather) { this.weather = weather; }

    public void setName(String name) {
        this.name = name;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Sys getSys() { return sys; }

    public void setSys(Sys sys) { this.sys = sys; }

    public Long getDt() { return dt; }

    public void setDt(Long dt) { this.dt = dt; }

    public void setDt(long dt) { this.dt = dt; }

    public static final Parcelable.Creator<WeatherData> CREATOR = new Parcelable.Creator<WeatherData>() {
        public WeatherData createFromParcel(Parcel in) {
            return new WeatherData(in);
        }

        public WeatherData[] newArray(int size) {
            return new WeatherData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symbol);
        dest.writeString(name);
        dest.writeParcelable(coord, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeParcelableArray(weather, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeParcelable(main, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeParcelable(sys, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeLong(dt);
    }

    public WeatherData(Parcel in){
        symbol = in.readString();
        name = in.readString();
        coord = in.readParcelable(Coord.class.getClassLoader());
        weather = in.createTypedArray(Weather.CREATOR);
        main = in.readParcelable(Main.class.getClassLoader());
        sys = in.readParcelable(Sys.class.getClassLoader());
        dt = in.readLong();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof WeatherData))
            return false;

        WeatherData owd = (WeatherData) o;
        return weather.equals(owd.weather) && coord.equals(owd.coord) &&
               name.equals(owd.name) && main.equals(owd.main) && sys.equals(owd.sys) && dt == owd.dt;
    }

    @Override
    public int hashCode() {
        return weather.hashCode() + coord.hashCode() + name.hashCode() + main.hashCode() + sys.hashCode() + (int) dt;
    }

    @Override
    public String toString() {
        return "[Weather: " + weather.toString() + " Coord: " + coord.toString() + " Name: " + name + " " + main.toString() + " " + sys.toString() + " Update: " + dt + "]";
    }
}
