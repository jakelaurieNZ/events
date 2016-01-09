package nz.co.chrisdrake.events.ui.explore;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;
import java.util.List;
import javax.inject.Inject;
import nz.co.chrisdrake.events.Config;
import nz.co.chrisdrake.events.R;
import nz.co.chrisdrake.events.data.api.model.Event;
import nz.co.chrisdrake.events.data.realm.model.RealmLocation;
import nz.co.chrisdrake.events.ui.BaseFragment;
import nz.co.chrisdrake.events.ui.BasePresenter;
import nz.co.chrisdrake.events.ui.ViewState;
import nz.co.chrisdrake.events.ui.explore.adapter.ExploreEventAdapter;
import nz.co.chrisdrake.events.ui.explore.adapter.ExploreTimeSpanAdapter;
import org.joda.time.Interval;

public class ExploreFragment extends BaseFragment
    implements ExploreView, ExploreEventAdapter.Callbacks, ExploreFilterView.Callbacks {

    public interface ExploreEventListener {
        void onEventClicked(Event event);

        void onAttributionClicked();
    }

    private static final String INSTANCE_STATE_PARAM_VIEW_STATE =
        "nz.co.chrisdrake.events.STATE_PARAM_VIEW_STATE";
    private static final String INSTANCE_STATE_PARAM_TIME_SPAN =
        "nz.co.chrisdrake.events.STATE_PARAM_TIME_SPAN";
    private static final String INSTANCE_STATE_PARAM_EVENTS =
        "nz.co.chrisdrake.events.STATE_PARAM_EVENTS";

    private static final String PREF_KEY_FEATURED = "nz.co.chrisdrake.events.PREF_KEY_FEATURED";
    private static final String PREF_KEY_FREE = "nz.co.chrisdrake.events.PREF_KEY_FREE";
    private static final String PREF_KEY_LOCATION = "nz.co.chrisdrake.events.PREF_KEY_LOCATION";

    // Min amount of items to have below the current scroll position before attempting to load more.
    private static final int LOAD_MORE_THRESHOLD = 3;

    @Inject ExplorePresenter presenter;
    @Inject Picasso picasso;
    @Inject SharedPreferences sharedPrefs;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.timespan_spinner) Spinner timeSpanSpinner;

    @Bind(R.id.list) RecyclerView recyclerView;
    @Bind(R.id.refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.error) TextView errorView;
    @Bind(R.id.empty) View emptyView;

    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.menu_explore_filter) ExploreFilterView filterView;

    private final ExploreFilterImpl filter = new ExploreFilterImpl();

    private ExploreEventListener exploreEventListener;

    private LinearLayoutManager layoutManager;

    private ExploreEventAdapter eventAdapter;

    private ExploreTimeSpanAdapter timeSpanAdapter;

    private int selectedTimeSpanPosition;

    private @ViewState int viewState;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        exploreEventListener = (ExploreEventListener) context;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // The DrawerLayout will be responsible for drawing the status bar colour.
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, v);

        toolbar.inflateMenu(R.menu.explore);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.filter) {
                    drawerLayout.openDrawer(GravityCompat.END);
                    return true;
                }
                return false;
            }
        });

        setupSpinner();
        setupRecyclerView();

        return v;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize(savedInstanceState);
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(INSTANCE_STATE_PARAM_EVENTS, eventAdapter.getAll());
        outState.putInt(INSTANCE_STATE_PARAM_VIEW_STATE, viewState);
        outState.putInt(INSTANCE_STATE_PARAM_TIME_SPAN, selectedTimeSpanPosition);
        super.onSaveInstanceState(outState);
    }

    @Override protected BasePresenter getPresenter() {
        return presenter;
    }

    private void setupSpinner() {
        timeSpanAdapter = new ExploreTimeSpanAdapter(
            new ContextThemeWrapper(getContext(), R.style.AppTheme_Widget_Spinner));

        timeSpanSpinner.setAdapter(timeSpanAdapter);
        timeSpanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != selectedTimeSpanPosition) {
                    selectedTimeSpanPosition = position;
                    presenter.attemptRefresh();
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                checkShouldLoadMore();
            }
        });

        swipeRefreshLayout.setColorSchemeColors(R.color.theme_primary, R.color.theme_accent_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.attemptRefresh();
            }
        });
    }

    private void checkShouldLoadMore() {
        int childCount = recyclerView.getChildCount();
        int totalCount = layoutManager.getItemCount();
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

        if (eventAdapter.isFooterEnabled() && (totalCount - childCount) <= (firstVisibleItem
            + LOAD_MORE_THRESHOLD)) {
            presenter.attemptLoadMore();
        }
    }

    private void initialize(@Nullable Bundle savedInstanceState) {
        getApplicationComponent().inject(this);

        eventAdapter = new ExploreEventAdapter(getContext(), picasso, this);
        recyclerView.setAdapter(eventAdapter);

        filterView.setCallbacks(this);
        filterView.setFreeOnly(sharedPrefs.getBoolean(PREF_KEY_FREE, false));
        filterView.setFeaturedOnly(sharedPrefs.getBoolean(PREF_KEY_FEATURED, false));

        presenter.setView(this);
        presenter.loadExistingLocations();

        @ViewState int viewState = savedInstanceState == null ? ViewState.DEFAULT
            : savedInstanceState.getInt(INSTANCE_STATE_PARAM_VIEW_STATE);
        setViewState(viewState);

        if (savedInstanceState != null) {
            selectedTimeSpanPosition = savedInstanceState.getInt(INSTANCE_STATE_PARAM_TIME_SPAN);
            List<Event> events =
                savedInstanceState.getParcelableArrayList(INSTANCE_STATE_PARAM_EVENTS);
            presenter.addEvents(events);
        }

        if (savedInstanceState == null || viewState == ViewState.REFRESHING) {
            presenter.attemptRefresh();
        }
    }

    @Override public void setViewState(@ViewState int viewState) {
        this.viewState = viewState;

        emptyView.setVisibility(viewState == ViewState.EMPTY ? View.VISIBLE : View.GONE);
        errorView.setVisibility(viewState == ViewState.ERROR ? View.VISIBLE : View.GONE);
        setRefreshing(viewState == ViewState.REFRESHING);
    }

    private void setRefreshing(boolean refreshing) {
        if (refreshing) {
            drawerLayout.closeDrawers();

            // SRL indicator does not appear when setRefreshing(true) is called unless it is posted.
            // http://stackoverflow.com/questions/26858692/swiperefreshlayout-setrefreshing-not-showing-indicator-initially
            swipeRefreshLayout.post(new Runnable() {
                @Override public void run() {
                    if (viewState == ViewState.REFRESHING) {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                }
            });
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override public void setLocationViewState(@ViewState int viewState) {
        filterView.setLocationViewState(viewState);
    }

    @Override public void showErrorMessage(String message) {
        if (filter.getOffset() == 0) {
            errorView.setText(message);
            setViewState(ViewState.ERROR);
        } else {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override public void enableLoadMore() {
        eventAdapter.setFooterEnabled(true);
        checkShouldLoadMore();
    }

    @Override public void disableLoadMore() {
        eventAdapter.setFooterEnabled(false);
    }

    @Override public void addEvents(List<Event> events) {
        eventAdapter.setInterval(filter.getInterval());
        eventAdapter.addAll(events);
    }

    @Override public void clearEvents() {
        recyclerView.scrollToPosition(0);
        eventAdapter.clear();
    }

    @Override public void displayLocations(List<RealmLocation> locations) {
        filterView.displayLocations(locations,
            sharedPrefs.getInt(PREF_KEY_LOCATION, Config.DEFAULT_LOCATION.id));
    }

    @Override public ExploreFilter getFilter() {
        return filter;
    }

    @Override public void onLocationSelected(int id) {
        if (sharedPrefs.getInt(PREF_KEY_LOCATION, Config.DEFAULT_LOCATION.id) != id) {
            sharedPrefs.edit().putInt(PREF_KEY_LOCATION, id).apply();
            presenter.attemptRefresh();
        }
    }

    @Override public void onFreeOnlyToggle(boolean checked) {
        onToggleChanged(PREF_KEY_FREE, checked);
    }

    @Override public void onFeaturedOnlyToggled(boolean checked) {
        onToggleChanged(PREF_KEY_FEATURED, checked);
    }

    private void onToggleChanged(String sharedPrefKey, boolean checked) {
        sharedPrefs.edit().putBoolean(sharedPrefKey, checked).apply();
        presenter.attemptRefresh();
    }

    @OnClick(R.id.filter_location_error) protected void onClickRetryLocation() {
        presenter.requestLocations();
    }

    @OnClick(R.id.attribution_text) protected void onClickAttribution() {
        exploreEventListener.onAttributionClicked();
    }

    @Override public void onEventClick(Event event) {
        exploreEventListener.onEventClicked(event);
    }

    private final class ExploreFilterImpl implements ExploreFilter {
        @Override public int getLocationId() {
            return filterView.getSelectedLocationId();
        }

        @Override public boolean isFeaturedOnly() {
            return filterView.isFeaturedOnly();
        }

        @Override public boolean isFreeOnly() {
            return filterView.isFreeOnly();
        }

        @Override public int getOffset() {
            return eventAdapter.getAll().size();
        }

        @Override public Interval getInterval() {
            if (timeSpanAdapter.isDataInvalid()) {
                timeSpanAdapter.recreateData();
            }
            return timeSpanAdapter.getItem(selectedTimeSpanPosition).interval;
        }
    }
}
