package it.unito.ium.myreps.model.storage;

import android.content.Context;

public final class KVStorageFactory {
    public static KVStorage newInstance(Context context) {
        return new KVStorageImpl(context);
    }
}
