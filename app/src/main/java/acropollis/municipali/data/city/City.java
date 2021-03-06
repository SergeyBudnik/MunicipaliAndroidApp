package acropollis.municipali.data.city;

import java.io.Serializable;
import java.util.Map;

import acropollis.municipalidata.dto.common.Language;
import lombok.Data;

@Data
public class City implements Serializable {
    private long id;
    private Map<Language, String> name;
}
