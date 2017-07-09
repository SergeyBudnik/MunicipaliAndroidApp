package acropollis.municipali.data.report;

import java.io.Serializable;

import lombok.Data;

@Data
public class Report implements Serializable {
    private Long id;
    private String userId;
    private double latitude, longitude;
    private String comment;
}
