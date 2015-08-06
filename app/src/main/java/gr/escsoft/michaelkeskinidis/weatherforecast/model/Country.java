package gr.escsoft.michaelkeskinidis.weatherforecast.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Michael on 8/4/2015.
 */
public class Country implements Parcelable {

    private String country;

    public Country(){}

    public Country(String country){
        this.country = country;
    }

    public void setCountry(String country){ this.country = country; }

    public String getCountry(){ return country; }

    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>() {
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(country);
    }

    private Country(Parcel in) {
        country = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Country))
            return false;

        Country oc = (Country) o;
        return country.equals(oc.country);
    }

    @Override
    public int hashCode() {
        return country.hashCode();
    }

    @Override
    public String toString() {
        return "[country: " + country + "]";
    }
}
