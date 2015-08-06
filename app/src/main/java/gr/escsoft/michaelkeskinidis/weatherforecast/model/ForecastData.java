package gr.escsoft.michaelkeskinidis.weatherforecast.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 8/5/2015.
 */
public class ForecastData implements Parcelable {

    private String symbol;
    private City city;
    private ArrayList<ForecastWeatherData> list = new ArrayList<ForecastWeatherData>();

    public ForecastData(){}

    public ForecastData(String symbol, City city, List<ForecastWeatherData> list){
        this.symbol = symbol;
        this.city = city;
        this.list.addAll(list);
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
        for (ForecastWeatherData wd : list){
            wd.setSymbol(symbol);
        }
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public ArrayList<ForecastWeatherData> getList() {
        return list;
    }

    public void setList(ArrayList<ForecastWeatherData> list) {
        this.list.clear();
        this.list.addAll(list);
    }

    public static final Parcelable.Creator<ForecastData> CREATOR = new Parcelable.Creator<ForecastData>() {
        public ForecastData createFromParcel(Parcel in) {
            return new ForecastData(in);
        }

        public ForecastData[] newArray(int size) {
            return new ForecastData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symbol);
        dest.writeParcelable(city, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeTypedList(list);
    }

    public ForecastData(Parcel in){
        symbol = in.readString();
        city = in.readParcelable(City.class.getClassLoader());
        list = in.createTypedArrayList(ForecastWeatherData.CREATOR);
    }
}
