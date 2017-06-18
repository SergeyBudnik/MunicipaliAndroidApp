package acropollis.municipali.data.backend;

import lombok.Data;

@Data
public class CountryPlatformFullBackendInfo {
    private long id;
    private long cityId;
    private String rootEndpoint;
}
