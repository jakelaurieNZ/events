package nz.co.chrisdrake.events.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

/** @see SessionResource */
public class Session implements Parcelable {
    @SerializedName("datetime_start") public final Date dateStart;
    @SerializedName("datetime_end") public final Date dateEnd;
    @SerializedName("datetime_summary") public final String dateTimeSummary;

    public Session(Date dateStart, Date dateEnd, String dateTimeSummary) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.dateTimeSummary = dateTimeSummary;
    }

    protected Session(Parcel in) {
        dateTimeSummary = in.readString();
        dateStart = new Date(in.readLong());
        dateEnd = new Date(in.readLong());
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dateTimeSummary);
        dest.writeLong(dateStart.getTime());
        dest.writeLong(dateEnd.getTime());
    }

    @Override public int describeContents() {
        return 0;
    }

    public static final Creator<Session> CREATOR = new Creator<Session>() {
        @Override public Session createFromParcel(Parcel in) {
            return new Session(in);
        }

        @Override public Session[] newArray(int size) {
            return new Session[size];
        }
    };
}
