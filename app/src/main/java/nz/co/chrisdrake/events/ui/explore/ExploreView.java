package nz.co.chrisdrake.events.ui.explore;

import java.util.List;
import nz.co.chrisdrake.events.data.api.model.Event;
import nz.co.chrisdrake.events.data.realm.model.RealmLocation;
import nz.co.chrisdrake.events.ui.ViewState;

public interface ExploreView {
    void setViewState(@ViewState int viewState);

    void showErrorMessage(String message);

    void enableLoadMore();

    void disableLoadMore();

    void addEvents(List<Event> events);

    void clearEvents();

    void setLocationViewState(@ViewState int viewState);

    void displayLocations(List<RealmLocation> locations);

    ExploreFilter getFilter();
}