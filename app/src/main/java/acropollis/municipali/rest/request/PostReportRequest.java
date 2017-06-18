package acropollis.municipali.rest.request;

import acropollis.municipali.data.report.Report;
import lombok.Data;

@Data
public class PostReportRequest {
    private Report report;
    private byte [] reportImage;
}
