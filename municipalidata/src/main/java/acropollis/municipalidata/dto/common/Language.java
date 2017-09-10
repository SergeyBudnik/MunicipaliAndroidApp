package acropollis.municipalidata.dto.common;

import java.util.Locale;

import lombok.Getter;

public enum Language {
    ENGLISH(new Locale("en", "us")),
    RUSSIAN(new Locale("ru", "ru")),
    MACEDONIAN(new Locale("mk")),
    ARABIC(null),
    HEBREW(new Locale("he"));

    @Getter private Locale locale;

    Language(Locale locale) {
        this.locale = locale;
    }
}
