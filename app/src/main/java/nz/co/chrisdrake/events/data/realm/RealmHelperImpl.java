package nz.co.chrisdrake.events.data.realm;

import io.realm.Realm;
import io.realm.RealmResults;
import java.util.List;
import javax.inject.Singleton;
import nz.co.chrisdrake.events.data.api.model.Location;
import nz.co.chrisdrake.events.data.api.model.LocationChildResource;
import nz.co.chrisdrake.events.data.realm.model.RealmLocation;

@Singleton public class RealmHelperImpl implements RealmHelper {
    private final Realm realm;

    public RealmHelperImpl(Realm realm) {
        this.realm = realm;
    }

    public List<RealmLocation> getPersistedLocations() {
        RealmResults<RealmLocation> results = realm.where(RealmLocation.class).findAll();
        return results.subList(0, results.size());
    }

    public void setPersistedLocations(List<Location> locations) {
        realm.beginTransaction();
        realm.clear(RealmLocation.class);
        for (Location location : locations) {
            createRealmLocation(location);
        }
        realm.commitTransaction();
    }

    private RealmLocation createRealmLocation(Location location) {
        RealmLocation realmLocation = realm.createObject(RealmLocation.class);
        realmLocation.setId(location.id);
        realmLocation.setName(location.name);
        realmLocation.setSummary(location.summary);

        LocationChildResource childrenResource = location.children;
        if (childrenResource != null) {
            List<Location> children = childrenResource.children;
            for (Location childLocation : children) {
                realmLocation.getChildren().add(createRealmLocation(childLocation));
            }
        }
        return realmLocation;
    }
}
