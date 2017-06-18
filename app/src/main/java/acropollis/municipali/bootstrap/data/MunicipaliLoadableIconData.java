package acropollis.municipali.bootstrap.data;

import lombok.Data;

@Data
public class MunicipaliLoadableIconData {
    public interface IconLoadingListener {
        void onSuccess(byte [] icon);
        void onFailure();
    }

    public interface IconFromCacheLoader {
        byte [] load();
    }

    public interface IconFromNetworkLoader {
        void load(IconLoadingListener listener);
    }

    private long id;
    private IconFromCacheLoader iconFromCacheLoader;
    private IconFromNetworkLoader iconFromNetworkLoader;

    public MunicipaliLoadableIconData(long id, IconFromCacheLoader iconFromCacheLoader, IconFromNetworkLoader iconFromNetworkLoader) {
        this.id = id;
        this.iconFromCacheLoader = iconFromCacheLoader;
        this.iconFromNetworkLoader = iconFromNetworkLoader;
    }
}
