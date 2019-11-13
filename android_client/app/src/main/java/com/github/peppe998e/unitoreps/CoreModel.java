package com.github.peppe998e.unitoreps;

import com.github.peppe998e.unitoreps.modules.AuthCache.AuthCache;

/**
 * Interface of CoreModel in order to hide methods and grow modularity
 *
 */
public interface CoreModel {
    AuthCache getAuthCache();
}
