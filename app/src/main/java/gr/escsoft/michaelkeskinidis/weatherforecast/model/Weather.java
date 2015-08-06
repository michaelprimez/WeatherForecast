package gr.escsoft.michaelkeskinidis.weatherforecast.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Michael on 8/4/2015.
 */
public class Weather implements Parcelable {

    private String main;
    private String description;
    private String icon;
    private Bitmap bitmap;

    public Weather(){}

    public Weather(String main, String description, String icon){
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() { return icon; }

    public void setIcon(String icon) { this.icon = icon; }

    public Bitmap getBitmap() { return bitmap; }

    public void setBitmap(Bitmap bitmap) { this.bitmap = bitmap; }

    public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(main);
        dest.writeString(description);
        dest.writeString(icon);
    }

    private Weather(Parcel in) {
        main = in.readString();
        description = in.readString();
        icon = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Weather))
            return false;

        Weather ow = (Weather) o;
        return main.equals(ow.main) && description.equals(ow.description) && icon.equals(ow.icon);
    }

    @Override
    public int hashCode() {
        return main.hashCode() + description.hashCode() + icon.hashCode();
    }

    @Override
    public String toString() {
        return "[Main: " + main + " Description: " + description + " Icon: " + icon + "]";
    }
}
