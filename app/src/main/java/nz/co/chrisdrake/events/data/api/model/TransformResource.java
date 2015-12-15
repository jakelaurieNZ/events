package nz.co.chrisdrake.events.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.List;

import static nz.co.chrisdrake.events.data.api.model.Transform.TransformSize;

/** @see Transform */
public class TransformResource implements Parcelable {
    public final List<Transform> transforms;

    public TransformResource(List<Transform> transforms) {
        this.transforms = transforms;
    }

    protected TransformResource(Parcel in) {
        transforms = in.createTypedArrayList(Transform.CREATOR);
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(transforms);
    }

    @Override public int describeContents() {
        return 0;
    }

    /**
     * Attempts to find a {@link Transform} object whose size matches the
     * {@link TransformSize} argument.
     *
     * @param size the size to match
     * @return the Transform object with the matching size
     */
    @Nullable public Transform getTransformWithSize(@NonNull TransformSize size) {
        for (Transform transform : transforms) {
            if (transform.transformSize == size) {
                return transform;
            }
        }
        return null;
    }

    public static final Creator<TransformResource> CREATOR = new Creator<TransformResource>() {
        @Override public TransformResource createFromParcel(Parcel in) {
            return new TransformResource(in);
        }

        @Override public TransformResource[] newArray(int size) {
            return new TransformResource[size];
        }
    };
}
