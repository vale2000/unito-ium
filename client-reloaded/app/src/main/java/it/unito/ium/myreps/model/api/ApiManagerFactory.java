package it.unito.ium.myreps.model.api;

import it.unito.ium.myreps.model.Model;

public final class ApiManagerFactory {
    public static ApiManager newInstance(Model model) {
        return new ApiManagerImpl(model);
    }
}
