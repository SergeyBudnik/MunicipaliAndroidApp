package acropollis.municipali.data.country;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import acropollis.municipalidata.dto.common.Language;
import acropollis.municipali.data.city.City;
import lombok.Data;

@Data
public class Country implements Serializable {
    private Long id;
    private Map<Language, String> name;
    private Collection<City> cities;
}
