package it.unito.ium.myreps.model.services.api;

import it.unito.ium.myreps.model.Model;

public final class ApiManagerFactory {
    public static ApiManager newInstance(Model model) {
        return new ApiManagerImpl(model);
    }
}
