package acropollis.municipali.data.backend;

import java.io.Serializable;

import lombok.Data;

@Data
public class BackendInfo implements Serializable {
    private String rootEndpoint;
    private String imageHostingRootEndpoint;
    private byte [] background;
    private byte [] icon;
}
