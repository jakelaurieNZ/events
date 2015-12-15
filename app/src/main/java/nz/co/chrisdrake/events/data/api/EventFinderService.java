package nz.co.chrisdrake.events.data.api;

import android.support.annotation.IntRange;
import nz.co.chrisdrake.events.data.api.model.EventResource;
import nz.co.chrisdrake.events.data.api.model.LocationResource;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/** @see "http://www.eventfinda.co.nz/api/v2/overview" */
public interface EventFinderService {
    @GET("events.json") //
    Observable<EventResource> events( //
        @Query("order") Order order, //
        @Query("location") LocationQuery location, //
        @Query("start_date") DateQuery startDate, //
        @Query("end_date") DateQuery endDate, //
        @Query("featured") @IntRange(from = 0, to = 1) int featured, //
        @Query("free") @IntRange(from = 0, to = 1) int free, //
        @Query("offset") int offset);

    @GET("locations.json") //
    Observable<LocationResource> locations( //
        @Query("location") LocationQuery location, //
        @Query("rows") int rows, //
        @Query("levels") int levels);
}
