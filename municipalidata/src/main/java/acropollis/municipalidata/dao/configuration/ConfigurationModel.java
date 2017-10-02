package acropollis.municipalidata.dao.configuration;

import java.io.Serializable;

import lombok.Data;

@Data
public class ConfigurationModel implements Serializable {
    private String serverRootUrl;
    private String imageHostingRootUrl;
}
