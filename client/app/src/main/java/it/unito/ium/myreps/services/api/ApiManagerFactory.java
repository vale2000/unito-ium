package it.unito.ium.myreps.services.api;

import it.unito.ium.myreps.Model;

public final class ApiManagerFactory {
    public static ApiManager instantiate(Model model) {
        return new ApiManagerImpl(model);
    }
}
