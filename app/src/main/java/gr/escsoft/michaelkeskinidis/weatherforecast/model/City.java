package gr.escsoft.michaelkeskinidis.weatherforecast.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Michael on 8/5/2015.
 */
public class City implements Parcelable {
    private long id;
    private String name;
    private Coord coord;
    private String country;

    public City(){}

    public City(long id, String name, Coord coord, String country){
        this.id = id;
        this.name = name;
        this.coord = coord;
        this.country = country;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        public City[] newArray(int size) {
            return new City[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeParcelable(coord, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeString(country);
    }

    public City(Parcel in){
        id = in.readLong();
        name = in.readString();
        coord = in.readParcelable(Coord.class.getClassLoader());
        country = in.readString();
    }
}
