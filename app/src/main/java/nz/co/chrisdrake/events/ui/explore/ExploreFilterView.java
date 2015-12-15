package nz.co.chrisdrake.events.ui.explore;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.List;
import nz.co.chrisdrake.events.Config;
import nz.co.chrisdrake.events.R;
import nz.co.chrisdrake.events.data.realm.model.RealmLocation;
import nz.co.chrisdrake.events.ui.ViewState;
import nz.co.chrisdrake.events.ui.explore.adapter.ExploreLocationAdapter;

public class ExploreFilterView extends FrameLayout {

    public interface Callbacks {
        void onLocationSelected(int locationId);

        void onFreeOnlyToggle(boolean checked);

        void onFeaturedOnlyToggled(boolean checked);
    }

    @Bind(R.id.filter_featured_only) CheckedTextView featuredOnlyToggle;
    @Bind(R.id.filter_free_only) CheckedTextView freeOnlyToggle;
    @Bind(R.id.filter_location_spinner) Spinner locationSpinner;
    @Bind(R.id.filter_location_error) TextView locationError;

    private final ExploreLocationAdapter locationAdapter;

    private @ViewState int locationViewState;
    private @Nullable Callbacks callbacks;

    public ExploreFilterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExploreFilterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.locationAdapter = new ExploreLocationAdapter(
            new ContextThemeWrapper(context, R.style.AppTheme_Widget_Spinner));
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        locationSpinner.setAdapter(locationAdapter);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (callbacks != null) {
                    callbacks.onLocationSelected((int) id);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override @CallSuper protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final SavedState ss = new SavedState(superState);
        ss.locationViewState = locationViewState;
        return ss;
    }

    @Override @CallSuper public void onRestoreInstanceState(Parcelable state) {
        final SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setLocationViewState(ss.locationViewState);
    }

    public void setCallbacks(@Nullable Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public void displayLocations(List<RealmLocation> locations, int selectedLocationId) {
        locationAdapter.setLocations(locations);
        for (int i = 0; i < locations.size(); i++) {
            RealmLocation location = locations.get(i);
            if (location.getId() == selectedLocationId) {
                locationSpinner.setSelection(i);
                break;
            }
        }
    }

    public int getSelectedLocationId() {
        int selectedLocationPosition = locationSpinner.getSelectedItemPosition();
        return selectedLocationPosition == AdapterView.INVALID_POSITION ? Config.DEFAULT_LOCATION.id
            : (int) locationAdapter.getItemId(selectedLocationPosition);
    }

    public void setLocationViewState(@ViewState int locationViewState) {
        this.locationViewState = locationViewState;
        locationAdapter.setLoading(locationViewState == ViewState.REFRESHING);
        locationError.setVisibility(
            locationViewState == ViewState.ERROR ? View.VISIBLE : View.GONE);
    }

    public void setFeaturedOnly(boolean isFeatured) {
        featuredOnlyToggle.setChecked(isFeatured);
    }

    public boolean isFeaturedOnly() {
        return featuredOnlyToggle.isChecked();
    }

    public void setFreeOnly(boolean isFree) {
        freeOnlyToggle.setChecked(isFree);
    }

    public boolean isFreeOnly() {
        return freeOnlyToggle.isChecked();
    }

    @OnClick({ R.id.filter_featured_only, R.id.filter_free_only })
    protected void onCheckFeatured(CheckedTextView checkable) {
        checkable.setChecked(!checkable.isChecked());

        if (callbacks != null) {
            boolean checked = checkable.isChecked();

            if (checkable == freeOnlyToggle) {
                callbacks.onFreeOnlyToggle(checked);
            } else if (checkable == featuredOnlyToggle) {
                callbacks.onFeaturedOnlyToggled(checked);
            }
        }
    }

    protected static class SavedState extends BaseSavedState {
        int locationViewState;

        public SavedState(Parcel in) {
            super(in);
            locationViewState = in.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(locationViewState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
            new Parcelable.Creator<SavedState>() {
                @Override public SavedState createFromParcel(Parcel source) {
                    return new SavedState(source);
                }

                @Override public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
    }
}
