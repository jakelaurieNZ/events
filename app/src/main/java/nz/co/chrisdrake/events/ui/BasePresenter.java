package nz.co.chrisdrake.events.ui;

public interface BasePresenter<T> {
    void resume();

    void pause();

    void destroy();

    void setView(T view);
}
