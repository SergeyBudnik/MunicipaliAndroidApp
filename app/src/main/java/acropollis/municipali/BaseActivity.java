package acropollis.municipali;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import acropollis.municipali.configuration.ProductConfiguration;
import acropollis.municipali.service.LanguageService;
import acropollis.municipali.service.ProductConfigurationService;
import acropollis.municipali.view.common.PopupMessageView_;

@EActivity
public abstract class BaseActivity extends FragmentActivity {
    private int currentTheme;

    @Bean
    LanguageService languageService;
    @Bean
    ProductConfigurationService productConfigurationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        currentTheme = getIntent().getIntExtra("currentTheme", R.style.RedAppTheme);

        setTheme(currentTheme);

        super.onCreate(savedInstanceState);
    }

    @AfterViews
    protected void onInit() {
        languageService.setLanguage(getCurrentProductConfiguration().getUiLanguage());
    }

    protected ProductConfiguration getCurrentProductConfiguration() {
        return productConfigurationService.getProductConfiguration();
    }

    public void setCurrentTheme(int currentTheme) {
        this.currentTheme = currentTheme;
    }

    public void showMessage(String text) {
        Toast message = Toast.makeText(this, text, Toast.LENGTH_SHORT);

        message.setView(PopupMessageView_.build(this).bind(text));

        message.show();
    }

    public void redirect(Class<? extends BaseActivity> redirectTo, int enterAnim, int exitAnim, boolean doFinish) {
        redirect(redirectTo, enterAnim, exitAnim, doFinish,
                Collections.<String, Serializable>singletonMap("currentTheme", currentTheme));
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

        i.putExtra("currentTheme", currentTheme);

        startActivity(i);
        overridePendingTransition(enterAnim, exitAnim);

        if (doFinish) {
            finish();
        }
    }

    public void redirectForResult(Class<? extends BaseActivity> redirectTo, int enterAnim, int exitAnim, int code) {
        redirectForResult(redirectTo, enterAnim, exitAnim, code,
                Collections.<String, Serializable>singletonMap("currentTheme", currentTheme));
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

        i.putExtra("currentTheme", currentTheme);

        startActivityForResult(i, code);
        overridePendingTransition(enterAnim, exitAnim);
    }

    public void finishRedirectForResult(int enterAnim, int exitAnim, int result) {
        finishRedirectForResult(enterAnim, exitAnim, result, Collections.<String, Serializable>emptyMap());
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
}
