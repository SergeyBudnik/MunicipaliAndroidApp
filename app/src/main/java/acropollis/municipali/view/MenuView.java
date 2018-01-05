package acropollis.municipali.view;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.Collections;

import acropollis.municipali.activities.ArticlesListActivity_;
import acropollis.municipali.activities.BaseActivity;
import acropollis.municipali.R;
import acropollis.municipali.activities.RegistrationActivity_;
import acropollis.municipali.activities.ReportStartActivity_;
import acropollis.municipali.activities.StartActivity_;
import acropollis.municipali.data.user.User;
import acropollis.municipali.data.user.UserId;
import acropollis.municipali.service.ProductConfigurationService;
import acropollis.municipali.service.UserService;
import acropollis.municipali.utls.BitmapUtils;
import acropollis.municipalidata.dao.DaoManager;
import acropollis.municipalidata.dto.article.ArticleType;

@EViewGroup(R.layout.fragment_menu)
public class MenuView extends LinearLayout {
    public static final int PERMISSIONS_REQUEST_CODE = 1;

    public MenuView(Context context) {
        super(context);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @ViewById(R.id.user_info)
    View userInfoView;
    @ViewById(R.id.user_icon)
    ImageView userIconView;
    @ViewById(R.id.no_user_icon)
    ImageView noUserIconView;
    @ViewById(R.id.user_name)
    TextView userNameView;

    @ViewById(R.id.login)
    View loginView;
    @ViewById(R.id.logout)
    View logoutView;

    @ViewById(R.id.clear_app_data)
    View clearAppDataView;

    @Bean
    UserService userService;

    @Bean
    ProductConfigurationService productConfigurationService;

    @Bean
    DaoManager daoManager;

    @AfterViews
    void init() {
        User user = isInEditMode() ? new User() : userService.getCurrentUser();

        if (user.getUserId().getLoginType() == UserId.LoginType.NONE) {
            logoutView.setVisibility(View.GONE);
        } else {
            loginView.setVisibility(View.GONE);

            if (!isInEditMode()) {
                if (userService.getCurrentUserIcon() != null) {
                    userIconView.setImageBitmap(BitmapUtils.iconFromBytes(userService.getCurrentUserIcon()));
                    userIconView.setVisibility(VISIBLE);

                    noUserIconView.setVisibility(GONE);
                }
            }

            userNameView.setText(user.getUserDetailsInfo().getName());
        }

        clearAppDataView.setVisibility(GONE);

//        if (!productConfigurationService.getProductConfiguration().isQa()) {
//            clearAppDataView.setVisibility(GONE);
//        }
    }

    @Click(R.id.news)
    void onNewsClick() {
        ((BaseActivity) getContext()).redirect(ArticlesListActivity_.class, 0, 0, true,
                Collections.singletonMap("articlesType", ArticleType.NEWS));
    }

    @Click(R.id.events)
    void onEventsClick() {
        ((BaseActivity) getContext()).redirect(ArticlesListActivity_.class, 0, 0, true,
                Collections.singletonMap("articlesType", ArticleType.EVENT));
    }

    @Click(R.id.surveys)
    void onSurveysClick() {
        ((BaseActivity) getContext()).redirect(ArticlesListActivity_.class, 0, 0, true,
                Collections.singletonMap("articlesType", ArticleType.SURVEY));
    }

    @Click(R.id.report)
    void onReportClick() {
        BaseActivity activity = (BaseActivity) getContext();

        if (hasPermissionsToOpenReports()) {
            activity.redirect(ReportStartActivity_.class, 0, 0, true);
        } else {
            ActivityCompat.requestPermissions(
                    activity,
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

        ((BaseActivity) getContext()).redirect(RegistrationActivity_.class, 0, 0, true);
    }

    @Click(R.id.logout)
    void onLogoutClick() {
        userService.removeUser();

        ((BaseActivity) getContext()).redirect(RegistrationActivity_.class, 0, 0, true);
    }

    @Click(R.id.clear_app_data)
    void onClearAppDataClick() {
        daoManager.clearAll(productConfigurationService.getProductConfiguration());

        ((BaseActivity) getContext()).redirect(StartActivity_.class, 0, 0, true);
    }

    private boolean hasPermissionsToOpenReports() {
        BaseActivity activity = (BaseActivity) getContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return
                    activity.hasPermission(android.Manifest.permission.CAMERA) &&
                    activity.hasPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    activity.hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            return true;
        }
    }
}
