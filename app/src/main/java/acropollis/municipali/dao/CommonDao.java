package acropollis.municipali.dao;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import acropollis.municipali.utls.StorageUtils;

abstract class CommonDao <T> {
    private AtomicReference<T> cache = new AtomicReference<>(null);

    protected abstract T newInstance();

    protected abstract String getFileName();

    protected T getValue() {
        return cache.get();
    }

    public void persist(Context context) {
        if (cache.get() != null) {
            try {
                StorageUtils.writeData(context, getFileName(), cache.get());
            } catch (IOException e) {
                Log.e("CommonDao", "Persist failed", e);
            }
        }
    }

    protected void readCache(Context context, String fileName, boolean force) {
        if (cache.get() == null || force) {
            try {
                cache.set((T) StorageUtils.readData(context, fileName));
            } catch (IOException e) {
                Log.e("CommonDao", "Reading failed", e);
            }
        }

        if (cache.get() == null) {
            cache.set(newInstance());
        }
    }
}