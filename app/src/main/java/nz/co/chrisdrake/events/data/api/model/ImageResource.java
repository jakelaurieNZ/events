package nz.co.chrisdrake.events.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.List;

/** @see Image */
public class ImageResource implements Parcelable {
    public final List<Image> images;

    public ImageResource(List<Image> images) {
        this.images = images;
    }

    protected ImageResource(Parcel in) {
        images = in.createTypedArrayList(Image.CREATOR);
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(images);
    }

    @Override public int describeContents() {
        return 0;
    }

    @Nullable public Image getPrimaryImage() {
        for (Image image : images) {
            if (image.isPrimary) {
                return image;
            }
        }
        return null;
    }

    public static final Creator<ImageResource> CREATOR = new Creator<ImageResource>() {
        @Override public ImageResource createFromParcel(Parcel in) {
            return new ImageResource(in);
        }

        @Override public ImageResource[] newArray(int size) {
            return new ImageResource[size];
        }
    };
}
