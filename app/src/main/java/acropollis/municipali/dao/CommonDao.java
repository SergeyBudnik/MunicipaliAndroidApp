package acropollis.municipali.dao;

import android.content.Context;

import java.io.IOException;

import acropollis.municipali.utls.StorageUtils;

abstract class CommonDao <T> {
    protected T cache;

    protected abstract T newInstance();

    protected abstract String getFileName();

    public void persist(Context context) {
        try {
            StorageUtils.writeData(context, getFileName(), cache);
        } catch (IOException e) {
            /* ToDo: ex handling */
            /* Do nothing */
        }
    }

    protected void readCache(Context context, String fileName, boolean force) {
        if (cache == null || force) {
            try {
                cache = StorageUtils.readData(context, fileName);
            } catch (IOException e) {
                /* ToDo: ex handling */
                /* Do nothing */
            }
        }

        if (cache == null) {
            cache = newInstance();
        }
    }
}