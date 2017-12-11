package acropollis.municipali.rest.wrappers.omega;

import android.util.Log;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;

import acropollis.municipali.data.report.Report;
import acropollis.municipali.rest.raw.omega.ReportRestService;
import acropollis.municipali.rest.request.PostReportRequest;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.service.ProductConfigurationService;
import acropollis.municipali.service.UserService;
import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.service.configuration.ConfigurationService;

@EBean
public class ReportRestWrapper {
    @RestService
    ReportRestService reportRestService;

    @Bean
    ProductConfigurationService productConfigurationService;
    @Bean
    ConfigurationService configurationService;
    @Bean
    UserService userService;

    @Background
    public void postReport(byte [] reportImage, String comments, double latitude, double longitude, RestListener<Void> listener) {
        try {
            listener.onStart();

            ProductConfiguration productConfiguration = productConfigurationService.getProductConfiguration();

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
                    configurationService.getServerRootUrl(productConfiguration).get(),
                    postReportRequest
            );

            listener.onSuccess(null);
        } catch (Exception e) {
            Log.e("ReportRestWrapper", "Report upload failed", e);

            listener.onFailure();
        }
    }
}
