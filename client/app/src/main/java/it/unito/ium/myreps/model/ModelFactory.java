package it.unito.ium.myreps.model;

import it.unito.ium.myreps.Controller;

public final class ModelFactory {
    public static Model instantiate(Controller appController) {
        return new ModelImpl(appController);
    }
}
