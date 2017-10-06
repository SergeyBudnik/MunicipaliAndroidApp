package acropollis.municipali;

import android.view.View;
import android.view.animation.Animation;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.util.Collections;

import acropollis.municipali.service.BackendInfoService;
import acropollis.municipali.service.UserService;
import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.configuration.ProductTier;
import acropollis.municipalidata.dto.article.ArticleType;

@EActivity(R.layout.activity_loading)
public class StartActivity extends BaseActivity {
    @ViewById(R.id.spinner)
    View spinnerView;

    @AnimationRes(R.anim.spinner)
    Animation spinnerAnimation;

    @Bean
    BackendInfoService backendInfoService;
    @Bean
    UserService userService;

    @AfterViews
    void init() {
        spinnerView.startAnimation(spinnerAnimation);

        ProductConfiguration currentProductConfiguration = getCurrentProductConfiguration();

        ProductTier productTier = currentProductConfiguration.getProductTier();

        if (productTier == ProductTier.DEMO) {
            //redirect(DemoAppActivity_.class, 0, 0, true);
        } else {
            if (backendInfoService.getBackendInfo() != null) {
                if (userService.getCurrentUserAuthToken() != null) {
                    redirect(
                            ArticlesListActivity_.class, 0, 0, true,
                            Collections.singletonMap("articlesType", ArticleType.NEWS)
                    );
                } else {
                    redirect(RegistrationActivity_.class, 0, 0, true);
                }
            } else {
                switch (currentProductConfiguration.getProductTier()) {
                    case COUNTRY_PLATFORM:
//                        redirect(CountryPlatformChooseCityActivity_.class, 0, 0, true);
                        break;
                    case STANDALONE:
                        redirect(StandaloneCustomerLoadingActivity_.class, 0, 0, true);
                        break;
                }
            }
        }
    }
}
