package nz.co.chrisdrake.events.ui.explore;

import android.support.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import nz.co.chrisdrake.events.Config;
import nz.co.chrisdrake.events.data.DefaultSubscriber;
import nz.co.chrisdrake.events.data.api.DateQuery;
import nz.co.chrisdrake.events.data.api.EventFinderService;
import nz.co.chrisdrake.events.data.api.LocationQuery;
import nz.co.chrisdrake.events.data.api.Order;
import nz.co.chrisdrake.events.data.api.model.Event;
import nz.co.chrisdrake.events.data.api.model.EventResource;
import nz.co.chrisdrake.events.data.api.model.Location;
import nz.co.chrisdrake.events.data.api.model.LocationResource;
import nz.co.chrisdrake.events.data.realm.RealmHelper;
import nz.co.chrisdrake.events.data.realm.model.RealmLocation;
import nz.co.chrisdrake.events.ui.BasePresenter;
import nz.co.chrisdrake.events.ui.ViewState;
import org.joda.time.Interval;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class ExplorePresenter implements BasePresenter<ExploreView> {

    private static final LocationQuery LOCATION_QUERY =
        new LocationQuery.Builder().location(Location.NEW_ZEALAND.id).build();

    private final RealmHelper realmHelper;
    private final EventFinderService eventFinderService;

    private boolean loadMoreInProgress = false;
    private boolean shouldReset = false;

    private Subscription eventSubscription = Subscriptions.empty();
    private Subscription locationSubscription = Subscriptions.empty();

    private ExploreView view;

    @Inject
    public ExplorePresenter(EventFinderService eventFinderService, RealmHelper realmHelper) {
        this.eventFinderService = eventFinderService;
        this.realmHelper = realmHelper;
    }

    @Override public void resume() {
        // do nothing.
    }

    @Override public void pause() {
        // do nothing.
    }

    @Override public void destroy() {
        eventSubscription.unsubscribe();
        locationSubscription.unsubscribe();
    }

    @Override public void setView(ExploreView view) {
        this.view = view;
    }

    public void attemptRefresh() {
        shouldReset = true;
        view.disableLoadMore();
        requestEvents();
    }

    public void attemptLoadMore() {
        if (!loadMoreInProgress) {
            requestEvents();
        }
    }

    public void requestEvents() {
        eventSubscription.unsubscribe();

        loadMoreInProgress = true;

        ExploreFilter filter = view.getFilter();

        if (filter.getOffset() == 0 || shouldReset) {
            view.setViewState(ViewState.REFRESHING);
        }

        LocationQuery locationQuery =
            new LocationQuery.Builder().location(filter.getLocationId()).build();

        Interval interval = filter.getInterval();

        Observable<EventResource> request = eventFinderService.events( //
            Order.POPULARITY, //
            locationQuery, //
            new DateQuery(interval.getStart()), //
            new DateQuery(interval.getEnd()), //
            filter.isFeaturedOnly() ? 1 : 0, //
            filter.isFreeOnly() ? 1 : 0, //
            shouldReset ? 0 : filter.getOffset());

        this.eventSubscription = request.compose(this.<EventResource>applySchedulers())
            .subscribe(new DefaultSubscriber<EventResource>() {
                @Override public void onNextOrError(@Nullable EventResource eventResource,
                    @Nullable Throwable e) {
                    onEventResponse(eventResource, e);
                }
            });
    }

    private void onEventResponse(@Nullable EventResource eventResource, @Nullable Throwable e) {
        loadMoreInProgress = false;

        if (shouldReset) view.clearEvents();

        shouldReset = false;

        if (e != null) {
            view.showErrorMessage(e.getLocalizedMessage());
        } else if (eventResource != null) {
            addEvents(eventResource.events);
        }
    }

    public void addEvents(List<Event> events) {
        if (events.size() > 0) {
            view.addEvents(events);
            view.enableLoadMore();
        } else {
            view.disableLoadMore();
        }

        view.setViewState(view.getFilter().getOffset() == 0 ? ViewState.EMPTY : ViewState.DEFAULT);
    }

    public void loadExistingLocations() {
        if (realmHelper.getPersistedLocations().size() == 0) {
            realmHelper.setPersistedLocations(Collections.singletonList(Config.DEFAULT_LOCATION));
        }

        List<RealmLocation> persistedLocations = realmHelper.getPersistedLocations();
        view.displayLocations(persistedLocations);

        if (persistedLocations.size() <= 1) {
            requestLocations();
        }
    }

    public void requestLocations() {
        locationSubscription.unsubscribe();

        view.setLocationViewState(ViewState.REFRESHING);

        Observable<LocationResource> request = eventFinderService.locations(LOCATION_QUERY, 1, 2);

        locationSubscription = request.compose(this.<LocationResource>applySchedulers())
            .subscribe(new DefaultSubscriber<LocationResource>() {
                @Override public void onNextOrError(@Nullable LocationResource locationResource,
                    @Nullable Throwable e) {
                    onLocationResponse(locationResource, e);
                }
            });
    }

    private void onLocationResponse(@Nullable LocationResource locationResource,
        @Nullable Throwable e) {
        if (e != null) {
            view.setLocationViewState(ViewState.ERROR);
        } else if (locationResource != null) {
            realmHelper.setPersistedLocations(locationResource.locations);
            view.setLocationViewState(ViewState.DEFAULT);
            view.displayLocations(realmHelper.getPersistedLocations());
        }
    }

    @SuppressWarnings("unchecked") <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
