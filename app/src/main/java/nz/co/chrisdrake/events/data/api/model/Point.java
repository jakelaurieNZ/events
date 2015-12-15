package nz.co.chrisdrake.events.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;

/** @see Event */
public class Point implements Parcelable {
    public final double lat;
    public final double lng;

    public Point(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    protected Point(Parcel in) {
        lat = in.readDouble();
        lng = in.readDouble();
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    @Override public int describeContents() {
        return 0;
    }

    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        @Override public Point[] newArray(int size) {
            return new Point[size];
        }
    };
}
