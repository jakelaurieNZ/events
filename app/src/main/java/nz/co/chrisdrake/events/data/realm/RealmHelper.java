package nz.co.chrisdrake.events.data.realm;

import java.util.List;
import nz.co.chrisdrake.events.data.api.model.Location;
import nz.co.chrisdrake.events.data.realm.model.RealmLocation;

public interface RealmHelper {
    List<RealmLocation> getPersistedLocations();

    void setPersistedLocations(List<Location> locations);
}
