package gr.escsoft.michaelkeskinidis.weatherforecast.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Michael on 8/5/2015.
 */
public class Coord implements Parcelable {
    private float lon;
    private float lat;

    public Coord(){}

    public Coord(float lon, float lat){
        this.lon = lon;
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public static final Parcelable.Creator<Coord> CREATOR = new Parcelable.Creator<Coord>() {
        public Coord createFromParcel(Parcel in) {
            return new Coord(in);
        }

        public Coord[] newArray(int size) {
            return new Coord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(lon);
        dest.writeFloat(lat);
    }

    public Coord(Parcel in){
        lon = in.readFloat();
        lat = in.readFloat();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Country))
            return false;

        Coord oc = (Coord) o;
        return lon == oc.lon && lat == oc.lat;
    }

    @Override
    public int hashCode() {
        return (int) lon + (int) lat;
    }

    @Override
    public String toString() {
        return "[lon: " + lon + " lat: " + lat + "]";
    }
}
