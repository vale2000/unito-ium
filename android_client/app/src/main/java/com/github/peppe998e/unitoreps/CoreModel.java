package com.github.peppe998e.unitoreps;

import com.github.peppe998e.unitoreps.modules.authcache.AuthCache;
import com.github.peppe998e.unitoreps.modules.apiservice.ApiService;

/**
 * Interface of CoreModel in order to hide methods and grow modularity
 *
 */
public interface CoreModel {
    AuthCache getAuthCache();
    ApiService getNetwork();
}
