package nz.co.chrisdrake.events.ui.explore.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import nz.co.chrisdrake.events.R;
import nz.co.chrisdrake.events.data.api.model.Event;
import nz.co.chrisdrake.events.data.api.model.Image;
import nz.co.chrisdrake.events.data.api.model.ImageResource;
import nz.co.chrisdrake.events.data.api.model.Session;
import nz.co.chrisdrake.events.data.api.model.Transform;
import nz.co.chrisdrake.events.data.api.model.TransformResource;
import nz.co.chrisdrake.events.ui.widget.FooterAdapter;
import nz.co.chrisdrake.events.ui.widget.ItemClickListener;
import org.joda.time.Interval;

import static nz.co.chrisdrake.events.data.api.model.Transform.TransformSize;

public class ExploreEventAdapter extends FooterAdapter<ExploreEventAdapter.EventViewHolder>
    implements ItemClickListener {

    public interface Callbacks {
        void onEventClick(Event event);
    }

    private static final TransformSize[] PREFERRED_IMAGE_SIZES = new TransformSize[] {
        TransformSize._650x280, TransformSize._190x127, TransformSize._350x350,
        TransformSize._80X80, TransformSize._75x75, TransformSize.OTHER
    };

    private final ArrayList<Event> events = new ArrayList<>();
    private final Picasso picasso;
    private final Callbacks callbacks;

    // Interval for determining which event sessions to display.
    private Interval interval;

    public ExploreEventAdapter(Context context, Picasso picasso, Callbacks callbacks) {
        super(context, R.layout.list_item_load_more, false);
        this.picasso = picasso == null ? Picasso.with(context) : picasso;
        this.callbacks = callbacks;

        setHasStableIds(true);
    }

    @Override public EventViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View v = getInflater().inflate(R.layout.list_item_explore_event, parent, false);
        return new EventViewHolder(v, this);
    }

    @Override public void onBindItemViewHolder(EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.title.setText(event.name);
        holder.location.setText(event.locationSummary);

        // Remove line-breaks.
        String description = event.description.replaceAll("(\\r|\\n)", " ");
        holder.description.setText(description);

        bindImage(holder, event.imageResource);
        bindSession(holder, event);
    }

    private void bindImage(EventViewHolder holder, ImageResource imageResource) {
        Image image = imageResource.getPrimaryImage();

        Transform transform = null;
        if (image != null) {
            TransformResource transformResource = image.transforms;
            for (TransformSize size : PREFERRED_IMAGE_SIZES) {
                transform = transformResource.getTransformWithSize(size);
                if (transform != null) break;
            }
        }

        @DrawableRes int placeHolderResId = R.drawable.ic_placeholder;

        if (transform != null) {
            picasso.load(transform.url).placeholder(placeHolderResId).into(holder.image);
        } else {
            picasso.load(placeHolderResId).into(holder.image);
        }
    }

    private void bindSession(EventViewHolder holder, Event event) {
        List<Session> sessions = event.sessionResource.sessions;

        Session nextSession = null;
        int upcomingSessions = 0;

        for (Session session : sessions) {
            if (session.dateEnd.getTime() >= interval.getStartMillis()) {
                upcomingSessions++;

                if (nextSession == null && session.dateStart.getTime() <= interval.getEndMillis()) {
                    nextSession = session;
                }

                if (nextSession != null && upcomingSessions > 1) break;
            }
        }

        boolean hasMultipleSessions = upcomingSessions > 1;
        holder.moreSessions.setVisibility(hasMultipleSessions ? View.VISIBLE : View.GONE);

        String date = nextSession != null ? nextSession.dateTimeSummary : event.dateTimeSummary;
        holder.date.setText(date);
    }

    @Override public long getItemId(int position) {
        return getItemViewType(position) == VIEW_TYPE_FOOTER ? super.getItemId(position)
            : events.get(position).id;
    }

    @Override public int getItemCount() {
        return super.getItemCount() + events.size();
    }

    @Override public void onItemClick(View v, int position) {
        if (callbacks != null) {
            Event event = events.get(position);
            callbacks.onEventClick(event);
        }
    }

    @NonNull public ArrayList<Event> getAll() {
        return events;
    }

    public void addAll(Collection<? extends Event> events) {
        int insertPosition = getItemCount();
        this.events.addAll(events);
        notifyItemRangeInserted(insertPosition, events.size());
    }

    public void clear() {
        events.clear();
        notifyDataSetChanged();
    }

    public void setInterval(@NonNull Interval interval) {
        this.interval = interval;
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.event_title) TextView title;
        @Bind(R.id.event_description) TextView description;
        @Bind(R.id.event_location) TextView location;
        @Bind(R.id.event_date) TextView date;
        @Bind(R.id.event_more_sessions) TextView moreSessions;
        @Bind(R.id.event_image) ImageView image;

        public EventViewHolder(View itemView, final ItemClickListener itemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
