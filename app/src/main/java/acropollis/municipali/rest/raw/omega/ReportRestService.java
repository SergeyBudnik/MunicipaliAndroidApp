package acropollis.municipali.rest.raw.omega;

import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import acropollis.municipali.rest.request.PostReportRequest;

@Rest(converters = MappingJacksonHttpMessageConverter.class)
public interface ReportRestService {
    @Post("{rootEndpoint}/report")
    void postReport(String rootEndpoint, PostReportRequest postReportRequest);
}
