package nz.co.chrisdrake.events.ui;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import nz.co.chrisdrake.events.EventApp;
import nz.co.chrisdrake.events.EventAppComponent;

public abstract class BaseFragment extends Fragment {

    @Override @CallSuper public void onResume() {
        super.onResume();
        if (getPresenter() != null) getPresenter().resume();
    }

    @Override @CallSuper public void onPause() {
        super.onPause();
        if (getPresenter() != null) getPresenter().pause();
    }

    @Override @CallSuper public void onDestroy() {
        if (getPresenter() != null) getPresenter().destroy();
        super.onDestroy();
    }

    @Nullable protected abstract BasePresenter getPresenter();

    protected final EventAppComponent getApplicationComponent() {
        return ((EventApp) getActivity().getApplication()).getApplicationComponent();
    }
}
