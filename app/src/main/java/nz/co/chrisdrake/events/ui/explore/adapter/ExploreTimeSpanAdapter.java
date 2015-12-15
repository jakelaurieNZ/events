package nz.co.chrisdrake.events.ui.explore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import nz.co.chrisdrake.events.R;
import nz.co.chrisdrake.events.ui.explore.ExploreTimeSpan;
import nz.co.chrisdrake.events.ui.widget.BaseThemedSpinnerAdapter;
import org.joda.time.LocalDate;

import static nz.co.chrisdrake.events.ui.explore.ExploreTimeSpan.DAY;
import static nz.co.chrisdrake.events.ui.explore.ExploreTimeSpan.WEEK;
import static nz.co.chrisdrake.events.ui.explore.ExploreTimeSpan.WEEKEND;

public class ExploreTimeSpanAdapter extends BaseThemedSpinnerAdapter {

    private ExploreTimeSpan[] timeSpans = new ExploreTimeSpan[] {};

    public ExploreTimeSpanAdapter(Context context) {
        super(context);
        recreateData();
    }

    public void recreateData() {
        this.timeSpans = new ExploreTimeSpan[] {
            new ExploreTimeSpan("Today", DAY),
            new ExploreTimeSpan("Tomorrow", DAY, LocalDate.now().plusDays(1)),
            new ExploreTimeSpan("Next 7 days", WEEK), //
            new ExploreTimeSpan("This weekend", WEEKEND)
        };
        notifyDataSetChanged();
    }

    @Override public boolean hasStableIds() {
        return true;
    }

    @Override public int getCount() {
        return timeSpans.length;
    }

    @Override public ExploreTimeSpan getItem(int position) {
        return timeSpans[position];
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spinner_item_explore_date, parent, false);
        }

        ((TextView) convertView).setText(getName(position));
        return convertView;
    }

    @Override public String getName(int position) {
        return getItem(position).name;
    }

    public boolean isDataInvalid() {
        LocalDate now = LocalDate.now();
        LocalDate today = timeSpans[0].interval.getStart().toLocalDate();
        return !now.equals(today);
    }
}
