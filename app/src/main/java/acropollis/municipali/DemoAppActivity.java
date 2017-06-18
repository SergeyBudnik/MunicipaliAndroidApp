package acropollis.municipali;

import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.data.common.Language;
import acropollis.municipali.service.LanguageService;

@EActivity(R.layout.activity_demo_app)
public class DemoAppActivity extends BaseActivity {
    @Bean
    LanguageService languageService;

    @ViewById(R.id.demo_lang_english)
    TextView demoLangEnglishView;
    @ViewById(R.id.demo_lang_hebrew)
    TextView demoLangHebrewView;
    @ViewById(R.id.demo_lang_macedonian)
    TextView demoLangMacedonianView;

    @ViewById(R.id.demo_theme_red)
    TextView demoThemeRedView;
    @ViewById(R.id.demo_theme_blue)
    TextView demoThemeBlueView;

    @AfterViews
    void init() {
        languageService.setLanguage(Language.ENGLISH);
    }

    @Click(R.id.demo_lang_english)
    void onLangEnglish() {
        languageService.setLanguage(Language.ENGLISH);

        demoLangEnglishView.setTextColor(getResources().getColor(R.color.red));
        demoLangHebrewView.setTextColor(getResources().getColor(R.color.black));
        demoLangMacedonianView.setTextColor(getResources().getColor(R.color.black));
    }

    @Click(R.id.demo_lang_hebrew)
    void onLangHebrew() {
        languageService.setLanguage(Language.HEBREW);

        demoLangEnglishView.setTextColor(getResources().getColor(R.color.black));
        demoLangHebrewView.setTextColor(getResources().getColor(R.color.red));
        demoLangMacedonianView.setTextColor(getResources().getColor(R.color.black));
    }

    @Click(R.id.demo_lang_macedonian)
    void onLangMacedoian() {
        languageService.setLanguage(Language.MACEDONIAN);

        demoLangEnglishView.setTextColor(getResources().getColor(R.color.black));
        demoLangHebrewView.setTextColor(getResources().getColor(R.color.black));
        demoLangMacedonianView.setTextColor(getResources().getColor(R.color.red));
    }

    @Click(R.id.demo_theme_red)
    void onThemeRed() {
        setCurrentTheme(R.style.RedAppTheme);

        demoThemeRedView.setTextColor(getResources().getColor(R.color.red));
        demoThemeBlueView.setTextColor(getResources().getColor(R.color.black));
    }

    @Click(R.id.demo_theme_blue)
    void onThemeBlue() {
        setCurrentTheme(R.style.BlueAppTheme);

        demoThemeRedView.setTextColor(getResources().getColor(R.color.black));
        demoThemeBlueView.setTextColor(getResources().getColor(R.color.red));
    }

    @Click(R.id.demo_ok)
    void onOkClick() {
        redirect(DemoAppServerActivity_.class, 0, 0, true);
    }
}
