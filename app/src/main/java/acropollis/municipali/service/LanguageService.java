package acropollis.municipali.service;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import acropollis.municipalidata.dto.common.Language;

@EBean(scope = EBean.Scope.Singleton)
public class LanguageService {
    @RootContext
    Context context;

    public void setLanguage(Language language) {
        Resources resources = context.getResources();

        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();

        configuration.locale = language.getLocale();

        context.getResources().updateConfiguration(configuration, displayMetrics);
    }
}