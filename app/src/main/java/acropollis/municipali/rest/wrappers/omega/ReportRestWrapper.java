package acropollis.municipali.rest.wrappers.omega;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;

import acropollis.municipali.data.report.Report;
import acropollis.municipali.rest.raw.omega.ReportRestService;
import acropollis.municipali.rest.request.PostReportRequest;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.service.BackendInfoService;
import acropollis.municipali.service.UserService;

@EBean
public class ReportRestWrapper {
    @RestService
    ReportRestService reportRestService;

    @Bean
    BackendInfoService backendInfoService;
    @Bean
    UserService userService;

    @Background
    public void postReport(byte [] reportImage, String comments, double latitude, double longitude, RestListener<Void> listener) {
        try {
            listener.onStart();

            Report report = new Report(); {
                report.setUserId(userService.getCurrentUserAuthToken());
                report.setLatitude(latitude);
                report.setLongitude(longitude);
                report.setComment(comments);
            }

            PostReportRequest postReportRequest = new PostReportRequest(); {
                postReportRequest.setReport(report);
                postReportRequest.setReportImage(reportImage);
            }

            reportRestService.postReport(
                    backendInfoService.getBackendInfo().getRootEndpoint(),
                    postReportRequest
            );

            listener.onSuccess(null);
        } catch (Exception e) {
            listener.onFailure();
        }
    }
}
