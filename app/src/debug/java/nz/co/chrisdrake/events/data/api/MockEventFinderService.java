package nz.co.chrisdrake.events.data.api;

import android.support.annotation.IntRange;
import nz.co.chrisdrake.events.data.api.model.EventResource;
import nz.co.chrisdrake.events.data.api.model.LocationResource;
import nz.co.chrisdrake.events.data.api.model.MockEventResponse;
import nz.co.chrisdrake.events.data.api.model.MockLocationResponse;
import retrofit.http.Query;
import rx.Observable;

public class MockEventFinderService implements EventFinderService {

    @Override public Observable<EventResource> events( //
        @Query("order") Order order, //
        @Query("location") LocationQuery location, //
        @Query("start_date") DateQuery startDate, //
        @Query("end_date") DateQuery endDate, //
        @Query("featured") @IntRange(from = 0, to = 1) int featured, //
        @Query("free") @IntRange(from = 0, to = 1) int free, //
        @Query("offset") int offset) {
        return Observable.just(offset == 0 ? MockEventResponse.GOOGLE_IO : MockEventResponse.EMPTY);
    }

    @Override public Observable<LocationResource> locations( //
        @Query("location") LocationQuery location, //
        @Query("rows") int rows, //
        @Query("levels") int levels) {
        return Observable.just(MockLocationResponse.UNITED_STATES);
    }
}
