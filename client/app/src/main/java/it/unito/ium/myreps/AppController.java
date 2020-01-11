package it.unito.ium.myreps;

import android.app.Application;

import it.unito.ium.myreps.model.Model;
import it.unito.ium.myreps.model.ModelFactory;

public final class AppController extends Application implements Controller {
    private Model appModel;

    @Override
    public void onCreate() {
        super.onCreate();
        this.appModel = ModelFactory.instantiate(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // ...
    }
}
