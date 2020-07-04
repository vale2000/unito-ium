package it.unito.ium.myreps.logic.api;

import it.unito.ium.myreps.logic.Model;

public final class ApiManagerFactory {
    public static ApiManager newInstance(Model model) {
        return new ApiManagerImpl(model);
    }
}
