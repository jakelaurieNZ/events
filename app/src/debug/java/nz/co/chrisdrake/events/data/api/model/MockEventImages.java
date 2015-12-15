package nz.co.chrisdrake.events.data.api.model;

import java.util.Collections;

public final class MockEventImages {
    static final ImageResource IO = new ImageResource(Collections.singletonList( //
        new Image(true, new TransformResource(Collections.singletonList(
            new Transform("https://events.google.com/io2015/images/io15-color.png", 800, 600,
                Transform.TransformSize.OTHER))))));

    private MockEventImages() {
        throw new AssertionError("Non-instantiable.");
    }
}
