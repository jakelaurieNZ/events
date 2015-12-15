package nz.co.chrisdrake.events.data.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmLocation extends RealmObject {
    @PrimaryKey private int id;
    private String name;
    private String summary;
    private RealmList<RealmLocation> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public RealmList<RealmLocation> getChildren() {
        return children;
    }

    public void setChildren(RealmList<RealmLocation> children) {
        this.children = children;
    }
}
