package nz.co.chrisdrake.events;

import nz.co.chrisdrake.events.data.DebugDataModule;

public class DebugEventApp extends EventApp {

    @Override protected void createComponent() {
        createComponent(false);
    }

    private void createComponent(boolean mockMode) {
        this.appComponent = DaggerEventAppComponent.builder()
            .eventAppModule(new EventAppModule(this))
            .debugDataModule(new DebugDataModule(mockMode))
            .build();
    }

    public void setMockMode(boolean mockMode) {
        createComponent(mockMode);
    }
}
