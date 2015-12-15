package nz.co.chrisdrake.events.ui.explore.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.List;
import nz.co.chrisdrake.events.R;
import nz.co.chrisdrake.events.data.realm.model.RealmLocation;
import nz.co.chrisdrake.events.ui.widget.BaseThemedSpinnerAdapter;

public class ExploreLocationAdapter extends BaseThemedSpinnerAdapter {

    private List<RealmLocation> locations;
    private boolean loading = false;

    public ExploreLocationAdapter(Context context) {
        super(context);
    }

    @Override public boolean hasStableIds() {
        return true;
    }

    @Override public int getCount() {
        int count = locations != null ? locations.size() : 0;
        return loading ? count + 1 : count;
    }

    @Override public RealmLocation getItem(int position) {
        return isLoadingView(position) ? null : locations.get(position);
    }

    @Override public long getItemId(int position) {
        return isLoadingView(position) ? -1 : locations.get(position).getId();
    }

    @Override public boolean isEnabled(int position) {
        return !isLoadingView(position);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView =
                getDropDownViewInflater().inflate(R.layout.spinner_item_explore_filter_location,
                    parent, false);
        }

        ((TextView) convertView).setText(getName(position));
        return convertView;
    }

    @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (isLoadingView(position)) {
            if (convertView == null || !(convertView instanceof ProgressBar)) {
                convertView =
                    getDropDownViewInflater().inflate(R.layout.spinner_dropdown_item_loading,
                        parent, false);
            }

            return convertView;
        } else {
            return super.getDropDownView(position, convertView, parent);
        }
    }

    @Override public String getName(int position) {
        RealmLocation location = getItem(position);
        return location == null ? null : location.getName();
    }

    private boolean isLoadingView(int position) {
        return loading && position == getCount() - 1;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyDataSetChanged();
    }

    public void setLocations(List<RealmLocation> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }
}
