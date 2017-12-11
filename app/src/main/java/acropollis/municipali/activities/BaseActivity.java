package acropollis.municipali.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.youtube.player.YouTubeBaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import acropollis.municipali.R;
import acropollis.municipali.service.LanguageService;
import acropollis.municipali.service.ProductConfigurationService;
import acropollis.municipali.view.MenuView;
import acropollis.municipalibootstrap.views.MunicipaliPopupMessageView;
import acropollis.municipalibootstrap.views.MunicipaliPopupMessageView.Type;
import acropollis.municipalibootstrap.views.MunicipaliPopupMessageView_;
import acropollis.municipalidata.configuration.ProductConfiguration;
import io.fabric.sdk.android.Fabric;

@EActivity
public abstract class BaseActivity extends YouTubeBaseActivity {
    @Bean
    LanguageService languageService;
    @Bean
    protected ProductConfigurationService productConfigurationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());
    }

    @AfterViews
    protected void onInit() {
        languageService.setLanguage(getCurrentProductConfiguration().getUiLanguage());
    }

    protected ProductConfiguration getCurrentProductConfiguration() {
        return productConfigurationService.getProductConfiguration();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions [], int [] grantResults) {
        switch (requestCode) {
            case MenuView.PERMISSIONS_REQUEST_CODE: {
                if (hasPermissionsToOpenReports()) {
                    redirect(ReportStartActivity_.class, 0, 0, true);
                } else {
                    showMessage(getResources().getString(R.string.report_no_permissions), Type.ERROR);
                }
                break;
            }
        }
    }

    public void showMessage(String text, Type type) {
        Toast message = Toast.makeText(this, text, Toast.LENGTH_SHORT);

        MunicipaliPopupMessageView v = MunicipaliPopupMessageView_.build(this); {
            v.bind(text, type);
        }

        message.setView(v);
        message.show();
    }

    public void redirect(Class<? extends BaseActivity> redirectTo, int enterAnim, int exitAnim, boolean doFinish) {
        redirect(redirectTo, enterAnim, exitAnim, doFinish, Collections.emptyMap());
    }

    public void redirect(Class<? extends BaseActivity> redirectTo,
                            int enterAnim,
                            int exitAnim,
                            boolean doFinish,
                            Map<String, Serializable> extras) {

        Intent i = new Intent(this, redirectTo);

        for (String extraKey : extras.keySet()) {
            i.putExtra(extraKey, extras.get(extraKey));
        }

        startActivity(i);
        overridePendingTransition(enterAnim, exitAnim);

        if (doFinish) {
            finish();
        }
    }

    public void redirectForResult(Class<? extends BaseActivity> redirectTo, int enterAnim, int exitAnim, int code) {
        redirectForResult(redirectTo, enterAnim, exitAnim, code, Collections.emptyMap());
    }

    public void redirectForResult(
            Class<? extends BaseActivity> redirectTo,
            int enterAnim,
            int exitAnim,
            int code,
            Map<String, Serializable> extras) {

        Intent i = new Intent(this, redirectTo);

        for (String extraKey : extras.keySet()) {
            i.putExtra(extraKey, extras.get(extraKey));
        }

        startActivityForResult(i, code);
        overridePendingTransition(enterAnim, exitAnim);
    }

    public void finishRedirectForResult(int enterAnim, int exitAnim, int result) {
        finishRedirectForResult(enterAnim, exitAnim, result, Collections.emptyMap());
    }

    public void finishRedirectForResult(int enterAnim, int exitAnim, int result, Map<String, Serializable> extras) {
        Intent i = new Intent();

        for (String extraKey : extras.keySet()) {
            i.putExtra(extraKey, extras.get(extraKey));
        }

        setResult(result, i);

        finish();
        overridePendingTransition(enterAnim, exitAnim);
    }

    public boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private boolean hasPermissionsToOpenReports() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return
                    hasPermission(android.Manifest.permission.CAMERA) &&
                    hasPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            return true;
        }
    }
}
