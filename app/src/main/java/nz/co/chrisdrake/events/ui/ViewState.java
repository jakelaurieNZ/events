package nz.co.chrisdrake.events.ui;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({ ViewState.DEFAULT, ViewState.REFRESHING, ViewState.EMPTY, ViewState.ERROR })
public @interface ViewState {
    int DEFAULT = 0;
    int REFRESHING = 1;
    int EMPTY = 2;
    int ERROR = 3;
}