package acropollis.municipali.data.country;

import java.util.Collection;
import java.util.Map;

import acropollis.municipali.data.common.Language;
import acropollis.municipali.data.city.City;
import lombok.Data;

@Data
public class Country {
    private Long id;
    private Map<Language, String> name;
    private Collection<City> cities;
}
