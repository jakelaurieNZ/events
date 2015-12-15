package nz.co.chrisdrake.events.data;

import android.support.annotation.Nullable;
import rx.Subscriber;

public abstract class DefaultSubscriber<T> extends Subscriber<T> {
    @Override public void onCompleted() {
        // do nothing.
    }

    @Override public void onError(Throwable e) {
        onNextOrError(null, e);
    }

    @Override public void onNext(T o) {
        onNextOrError(o, null);
    }

    public abstract void onNextOrError(@Nullable T o, @Nullable Throwable e);
}
