package nz.co.chrisdrake.events.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/** @see TransformResource */
public class Transform implements Parcelable {
    /**
     * Represents different image sizes
     *
     * @see "http://www.eventfinda.co.nz/api/v2/image"
     */
    public enum TransformSize {
        @SerializedName("2")_80X80,
        @SerializedName("7")_650x280,
        @SerializedName("8")_190x127,
        @SerializedName("15")_75x75,
        @SerializedName("27")_350x350,
        OTHER
    }

    public final String url;
    public final int width;
    public final int height;

    @SerializedName("transformation_id") public final TransformSize transformSize;

    public Transform(String url, int width, int height, TransformSize transformSize) {
        this.url = url;
        this.width = width;
        this.height = height;
        this.transformSize = transformSize;
    }

    protected Transform(Parcel in) {
        url = in.readString();
        width = in.readInt();
        height = in.readInt();
        transformSize = TransformSize.values()[in.readInt()];
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeInt(
            transformSize == null ? TransformSize.OTHER.ordinal() : transformSize.ordinal());
    }

    @Override public int describeContents() {
        return 0;
    }

    public static final Creator<Transform> CREATOR = new Creator<Transform>() {
        @Override public Transform createFromParcel(Parcel in) {
            return new Transform(in);
        }

        @Override public Transform[] newArray(int size) {
            return new Transform[size];
        }
    };
}
