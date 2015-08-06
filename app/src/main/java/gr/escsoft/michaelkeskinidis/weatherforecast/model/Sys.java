package gr.escsoft.michaelkeskinidis.weatherforecast.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Michael on 8/4/2015.
 */
public class Sys implements Parcelable {

    private String country;
    private long sunrise;
    private long sunset;

    public Sys(){}

    public Sys(String country, long sunrise, long sunset){

        this.country = country;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String getCountry() {
        return country;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static final Parcelable.Creator<Sys> CREATOR = new Parcelable.Creator<Sys>() {
        public Sys createFromParcel(Parcel in) {
            return new Sys(in);
        }

        public Sys[] newArray(int size) {
            return new Sys[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeParcelable(sys, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeString(country);
        dest.writeLong(sunrise);
        dest.writeLong(sunset);
    }

    public Sys(Parcel in){

//        sys = in.readParcelable(Sys.class.getClassLoader());
        country = in.readString();
        sunrise = in.readLong();
        sunset = in.readLong();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Sys))
            return false;

        Sys oc = (Sys) o;
        return country.equals(oc.country) && sunrise == oc.sunrise && sunset == oc.sunset;
    }

    @Override
    public int hashCode() {
        return country.hashCode() + (int) sunrise + (int) sunset;
    }

    @Override
    public String toString() {
        return "[Country: " + country + " Sunrise: " + sunrise + " Sunset: " + sunset + "]";
    }
}
