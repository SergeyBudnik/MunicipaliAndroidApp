package acropollis.municipali.dao;

import android.content.Context;

import java.io.IOException;

import acropollis.municipali.utls.StorageUtils;

abstract class CommonIconDao<T> {
    public byte [] getIcon(T id) {
        try {
            return StorageUtils.readData(getContext(), getFileName(id));
        } catch (IOException e) {
            logError("Icon collect failed");

            return null;
        }
    }

    public void addIcon(T id, byte [] iconBytes) {
        try {
            StorageUtils.writeData(getContext(), getFileName(id), iconBytes);
        } catch (IOException e) {
            logError("Icon save failed");
        }
    }

    public void removeIcon(T id) {
        try {
            StorageUtils.clearData(getContext(), getFileName(id));
        } catch (IOException e) {
            logError("Icon removal failed");
        }
    }

    protected abstract Context getContext();
    protected abstract String getFileName(T id);
    protected abstract void logError(String message);
}
