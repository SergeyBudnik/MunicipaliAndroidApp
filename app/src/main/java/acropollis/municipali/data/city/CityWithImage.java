package acropollis.municipali.data.city;

import java.util.Map;

import acropollis.municipali.data.common.Language;
import lombok.Data;

@Data
public class CityWithImage {
    private long id;
    private Map<Language, String> name;
    private byte [] icon;
}
