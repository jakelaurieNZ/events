package nz.co.chrisdrake.events.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

/** @see EventResource */
public class Event implements Parcelable {
    public final long id;
    public final String name;
    public final String url;
    public final String description;
    public final String address;
    public final Point point;
    public final String restrictions;

    @SerializedName("datetime_start") public final Date dateStart;
    @SerializedName("datetime_end") public final Date dateEnd;
    @SerializedName("datetime_summary") public final String dateTimeSummary;

    @SerializedName("location_summary") public final String locationSummary;

    @SerializedName("is_free") public final boolean isFree;
    @SerializedName("is_featured") public final boolean isFeatured;

    @SerializedName("images") public final ImageResource imageResource;
    @SerializedName("sessions") public final SessionResource sessionResource;

    public Event(long id, String name, String url, String description, Date dateStart, Date dateEnd,
        Point point, String locationSummary, String address, boolean isFree, boolean isFeatured,
        String restrictions, String dateTimeSummary, ImageResource imageResource,
        SessionResource sessionResource) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.description = description;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.point = point;
        this.locationSummary = locationSummary;
        this.address = address;
        this.isFree = isFree;
        this.isFeatured = isFeatured;
        this.restrictions = restrictions;
        this.dateTimeSummary = dateTimeSummary;
        this.imageResource = imageResource;
        this.sessionResource = sessionResource;
    }

    protected Event(Parcel in) {
        id = in.readLong();
        name = in.readString();
        url = in.readString();
        description = in.readString();
        dateStart = new Date(in.readLong());
        dateEnd = new Date(in.readLong());
        point = in.readParcelable(Point.class.getClassLoader());
        locationSummary = in.readString();
        address = in.readString();
        isFree = in.readByte() == 1;
        isFeatured = in.readByte() == 1;
        restrictions = in.readString();
        dateTimeSummary = in.readString();
        imageResource = in.readParcelable(ImageResource.class.getClassLoader());
        sessionResource = in.readParcelable(SessionResource.class.getClassLoader());
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(description);
        dest.writeLong(dateStart.getTime());
        dest.writeLong(dateEnd.getTime());
        dest.writeParcelable(point, flags);
        dest.writeString(locationSummary);
        dest.writeString(address);
        dest.writeByte((byte) (isFree ? 1 : 0));
        dest.writeByte((byte) (isFeatured ? 1 : 0));
        dest.writeString(restrictions);
        dest.writeString(dateTimeSummary);
        dest.writeParcelable(imageResource, flags);
        dest.writeParcelable(sessionResource, flags);
    }

    @Override public int describeContents() {
        return 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
