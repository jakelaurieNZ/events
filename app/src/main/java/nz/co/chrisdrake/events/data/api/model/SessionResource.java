package nz.co.chrisdrake.events.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/** @see Session */
public class SessionResource implements Parcelable {
    public final List<Session> sessions;

    public SessionResource(List<Session> sessions) {
        this.sessions = sessions;
    }

    protected SessionResource(Parcel in) {
        sessions = in.createTypedArrayList(Session.CREATOR);
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(sessions);
    }

    @Override public int describeContents() {
        return 0;
    }

    public static final Creator<SessionResource> CREATOR = new Creator<SessionResource>() {
        @Override public SessionResource createFromParcel(Parcel in) {
            return new SessionResource(in);
        }

        @Override public SessionResource[] newArray(int size) {
            return new SessionResource[size];
        }
    };
}
