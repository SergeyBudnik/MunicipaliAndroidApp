package acropollis.municipalidata.dao.common;

import android.content.Context;

import com.annimon.stream.Optional;

import java.io.IOException;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.utils.StorageUtils;

public abstract class CommonSingletonImageDao {
    public Optional<byte []> getIcon(ProductConfiguration configuration) {
        try {
            return Optional.ofNullable(StorageUtils.readData(getContext(), getFileName(configuration)));
        } catch (IOException e) {
            logError("Icon collect failed");

            return Optional.empty();
        }
    }

    public void setIcon(ProductConfiguration configuration, byte [] iconBytes) {
        try {
            StorageUtils.writeData(getContext(), getFileName(configuration), iconBytes);
        } catch (IOException e) {
            logError("Icon save failed");
        }
    }

    public void removeIcon(ProductConfiguration configuration) {
        try {
            StorageUtils.clearData(getContext(), getFileName(configuration));
        } catch (IOException e) {
            logError("Icon removal failed");
        }
    }

    protected abstract Context getContext();
    protected abstract String getFileName(ProductConfiguration configuration);
    protected abstract void logError(String message);
}
