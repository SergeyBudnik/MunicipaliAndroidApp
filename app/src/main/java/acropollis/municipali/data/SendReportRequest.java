package acropollis.municipali.data;

import acropollis.municipalidata.dto.common.Language;
import lombok.Data;

@Data
public class SendReportRequest {
    private String userAuthToken;
    private String reportText;
    private Language reportLanguage;
    private byte [] image;
}