package acropollis.municipalidata.dao.common;

import android.content.Context;

import com.annimon.stream.Optional;

import java.io.IOException;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.utils.StorageUtils;

public abstract class CommonImageDao<T> {
    public Optional<byte []> getIcon(ProductConfiguration configuration, T id) {
        try {
            return Optional.ofNullable(StorageUtils.readData(getContext(), getFileName(configuration, id)));
        } catch (IOException e) {
            logError("Icon collect failed");

            return Optional.empty();
        }
    }

    public void addIcon(ProductConfiguration configuration, T id, byte [] iconBytes) {
        try {
            StorageUtils.writeData(getContext(), getFileName(configuration, id), iconBytes);
        } catch (IOException e) {
            logError("Icon save failed");
        }
    }

    public void removeIcon(ProductConfiguration configuration, T id) {
        try {
            StorageUtils.clearData(getContext(), getFileName(configuration, id));
        } catch (IOException e) {
            logError("Icon removal failed");
        }
    }

    protected abstract Context getContext();
    protected abstract String getFileName(ProductConfiguration configuration, T id);
    protected abstract void logError(String message);
}
