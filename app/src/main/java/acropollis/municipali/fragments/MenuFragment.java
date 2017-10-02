package acropollis.municipali.fragments;

import android.Manifest;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Collections;

import acropollis.municipali.ArticlesListActivity_;
import acropollis.municipali.BaseActivity;
import acropollis.municipali.CountryPlatformChooseCityActivity_;
import acropollis.municipali.R;
import acropollis.municipali.RegistrationActivity_;
import acropollis.municipali.ReportStartActivity_;
import acropollis.municipali.data.user.User;
import acropollis.municipali.data.user.UserId;
import acropollis.municipali.service.BackendInfoService;
import acropollis.municipali.service.UserService;
import acropollis.municipali.utls.BitmapUtils;
import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.configuration.ProductTier;
import acropollis.municipalidata.dto.article.ArticleType;

@EFragment(R.layout.fragment_menu)
public class MenuFragment extends Fragment {
    private static final int PERMISSIONS_REQUEST_CODE = 1;

    @ViewById(R.id.user_info)
    View userInfoView;
    @ViewById(R.id.user_icon)
    ImageView userIconView;
    @ViewById(R.id.user_name)
    TextView userNameView;

    @ViewById(R.id.login)
    View loginView;
    @ViewById(R.id.logout)
    View logoutView;

    @ViewById(R.id.change_city)
    View changeCityView;

    @Bean
    UserService userService;
    @Bean
    BackendInfoService backendInfoService;

    @AfterViews
    void init() {
        User user = userService.getCurrentUser();

        if (user.getUserId().getLoginType() == UserId.LoginType.NONE) {
            logoutView.setVisibility(View.GONE);
            userInfoView.setVisibility(View.GONE);
        } else {
            loginView.setVisibility(View.GONE);

            if (userService.getCurrentUserIcon() != null) {
                userIconView.setImageBitmap(BitmapUtils.iconFromBytes(userService.getCurrentUserIcon()));
            }

            userNameView.setText(user.getUserDetailsInfo().getName());
        }

        ProductConfiguration currentProductConfiguration =
                ProductConfiguration
                        .getProductConfiguration(
                                getResources()
                                        .getString(R.string.product_id)
                        );

        if (currentProductConfiguration.getProductTier() != ProductTier.COUNTRY_PLATFORM) {
            changeCityView.setVisibility(View.GONE);
        }
    }

    @Click(R.id.news)
    void onNewsClick() {
        ((BaseActivity) getActivity()).redirect(
                ArticlesListActivity_.class, 0, 0, true,
                Collections.singletonMap("articlesType", ArticleType.NEWS)
        );
    }

    @Click(R.id.events)
    void onEventsClick() {
        ((BaseActivity) getActivity()).redirect(
                ArticlesListActivity_.class, 0, 0, true,
                Collections.singletonMap("articlesType", ArticleType.EVENT)
        );
    }

    @Click(R.id.report)
    void onReportClick() {
        BaseActivity activity = (BaseActivity) getActivity();

        if (hasPermissionsToOpenReports()) {
            activity.redirect(ReportStartActivity_.class, 0, 0, true);
        } else {
            requestPermissions(
                    new String[] {
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    PERMISSIONS_REQUEST_CODE
            );
        }
    }

    @Click(R.id.login)
    void onLoginClick() {
        userService.removeUser();

        ((BaseActivity) getActivity()).redirect(RegistrationActivity_.class, 0, 0, true);
    }

    @Click(R.id.logout)
    void onLogoutClick() {
        userService.removeUser();

        ((BaseActivity) getActivity()).redirect(RegistrationActivity_.class, 0, 0, true);
    }

    @Click(R.id.change_city)
    void onChangeCityClick() {
        userService.removeUser();
        backendInfoService.setBackendInfo(null);

        ((BaseActivity) getActivity()).redirect(CountryPlatformChooseCityActivity_.class, 0, 0, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions [], int [] grantResults) {
        BaseActivity activity = (BaseActivity) getActivity();

        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (hasPermissionsToOpenReports()) {
                    activity.redirect(ReportStartActivity_.class, 0, 0, true);
                } else {
                    ((BaseActivity) getActivity())
                            .showMessage(getResources().getString(R.string.report_no_permissions));
                }
                break;
            }
        }
    }

    private boolean hasPermissionsToOpenReports() {
        BaseActivity activity = (BaseActivity) getActivity();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return
                    activity.hasPermission(Manifest.permission.CAMERA) &&
                    activity.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    activity.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            return true;
        }
    }
}
