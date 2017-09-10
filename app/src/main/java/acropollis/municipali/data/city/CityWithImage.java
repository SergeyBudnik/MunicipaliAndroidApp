package acropollis.municipali.data.city;

import java.io.Serializable;
import java.util.Map;

import acropollis.municipalidata.dto.common.Language;
import lombok.Data;

@Data
public class CityWithImage implements Serializable {
    private long id;
    private Map<Language, String> name;
    private byte [] icon;
}
