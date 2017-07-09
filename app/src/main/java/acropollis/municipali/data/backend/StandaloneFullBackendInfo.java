package acropollis.municipali.data.backend;

import java.io.Serializable;

import lombok.Data;

@Data
public class StandaloneFullBackendInfo implements Serializable {
    private String id;
    private String rootEndpoint;
}
