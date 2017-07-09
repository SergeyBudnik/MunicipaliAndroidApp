package acropollis.municipali.data.backend;

import java.io.Serializable;

import lombok.Data;

@Data
public class CountryPlatformFullBackendInfo implements Serializable {
    private long id;
    private long cityId;
    private String rootEndpoint;
}
