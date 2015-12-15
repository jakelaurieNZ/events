package nz.co.chrisdrake.events.data.realm;

import java.util.ArrayList;
import java.util.List;
import nz.co.chrisdrake.events.data.api.model.Location;
import nz.co.chrisdrake.events.data.api.model.LocationChildResource;
import nz.co.chrisdrake.events.data.realm.model.RealmLocation;

public class MockRealmHelper implements RealmHelper {
    private List<RealmLocation> persistedLocations = new ArrayList<>();

    @Override public List<RealmLocation> getPersistedLocations() {
        return persistedLocations;
    }

    @Override public void setPersistedLocations(List<Location> locations) {
        persistedLocations.clear();

        for (Location location : locations) {
            RealmLocation realmLocation = createRealmLocation(location);
            persistedLocations.add(realmLocation);

            LocationChildResource childrenResource = location.children;
            if (childrenResource != null) {
                List<Location> children = childrenResource.children;
                for (Location childLocation : children) {
                    persistedLocations.add(createRealmLocation(childLocation));
                }
            }
        }
    }

    private static RealmLocation createRealmLocation(Location location) {
        RealmLocation realmLocation = new RealmLocation();
        realmLocation.setId(location.id);
        realmLocation.setName(location.name);
        realmLocation.setSummary(location.summary);
        return realmLocation;
    }
}
