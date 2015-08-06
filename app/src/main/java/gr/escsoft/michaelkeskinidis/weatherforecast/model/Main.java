package gr.escsoft.michaelkeskinidis.weatherforecast.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Michael on 8/4/2015.
 */
public class Main implements Parcelable {

    private String humidity;
    private String temp;
    private String temp_max;
    private String temp_min;

    public Main(){}

    public Main(String humidity, String temp, String temp_max, String temp_min){
        this.humidity = humidity;
        this.temp = temp;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(String temp_min) {
        this.temp_min = temp_min;
    }

    public static final Parcelable.Creator<Main> CREATOR = new Parcelable.Creator<Main>() {
        public Main createFromParcel(Parcel in) {
            return new Main(in);
        }

        public Main[] newArray(int size) {
            return new Main[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(humidity);
        dest.writeString(temp);
        dest.writeString(temp_max);
        dest.writeString(temp_min);
    }

    private Main(Parcel in) {
        humidity = in.readString();
        temp = in.readString();
        temp_max = in.readString();
        temp_min = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Main))
            return false;

        Main ow = (Main) o;
        return humidity.equals(ow.humidity) && temp.equals(ow.temp) && temp_min.equals(ow.temp_min) && temp_max.equals(ow.temp_max);
    }

    @Override
    public int hashCode() {
        return humidity.hashCode() + temp.hashCode() + temp_min.hashCode() + temp_max.hashCode();
    }

    @Override
    public String toString() {
        return "[humidity: " + humidity + " temp: " + temp + " temp_min: " + temp_min + " temp_max: " + temp_max + "]";
    }
}
