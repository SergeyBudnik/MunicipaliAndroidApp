package acropollis.municipali.bootstrap.data;

import lombok.Data;

@Data
public class MunicipaliRowData {
    public interface TransparencyInfo {
        boolean isTransparent();
    }

    public static final TransparencyInfo NO_TRANSPARENCY = new TransparencyInfo() {
        @Override
        public boolean isTransparent() {
            return false;
        }
    };

    public interface CounterInfo {
        int getCounter();
    }

    public static final CounterInfo NO_COUNTER = new CounterInfo() {
        @Override
        public int getCounter() {
            return 0;
        }
    };

    private long id;
    private String text;
    private MunicipaliLoadableIconData loadableIconData;
    private TransparencyInfo transparencyInfo;
    private CounterInfo counterInfo;

    public MunicipaliRowData(
            long id, String text,
            MunicipaliLoadableIconData loadableIconData,
            TransparencyInfo transparencyInfo,
            CounterInfo counterInfo
    ) {
        this.id = id;
        this.text = text;
        this.loadableIconData = loadableIconData;
        this.transparencyInfo = transparencyInfo;
        this.counterInfo = counterInfo;
    }
}
