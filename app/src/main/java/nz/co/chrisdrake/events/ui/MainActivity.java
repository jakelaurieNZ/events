package nz.co.chrisdrake.events.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import nz.co.chrisdrake.events.R;
import nz.co.chrisdrake.events.ui.explore.ExploreFragment;

public class MainActivity extends AppCompatActivity {
    public static final int FRAGMENT_CONTAINER_ID = R.id.container;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            addFragment(new ExploreFragment());
        }
    }

    private void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
            .add(FRAGMENT_CONTAINER_ID, fragment)
            .commit();
    }
}
