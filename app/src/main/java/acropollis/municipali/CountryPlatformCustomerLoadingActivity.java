package acropollis.municipali;

import android.view.View;
import android.view.animation.Animation;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import acropollis.municipali.configuration.ProductConfiguration;
import acropollis.municipali.data.backend.BackendInfo;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.rest.wrappers.alpha.CountryPlatformProductRestWrapper;
import acropollis.municipali.service.BackendInfoService;

@EActivity(R.layout.activity_loading)
public class CountryPlatformCustomerLoadingActivity extends BaseActivity {
    @ViewById(R.id.spinner)
    View spinnerView;

    @AnimationRes(R.anim.spinner)
    Animation spinnerAnimation;

    @Bean
    BackendInfoService backendInfoService;

    @Bean
    CountryPlatformProductRestWrapper countryPlatformProductRestWrapper;

    @Extra("cityId")
    Long cityId;

    @AfterViews
    void init() {
        spinnerView.startAnimation(spinnerAnimation);

        getBackendInfo();
    }

    @UiThread
    void onSuccessLoading() {
        redirect(RegistrationActivity_.class, 0, 0, true);
    }

    @UiThread
    void onFailureLoading() {
        finish();
    }

    private void getBackendInfo() {
        final ProductConfiguration currentProductConfiguration = ProductConfiguration
                .getProductConfiguration(getResources().getString(R.string.product_id));

        countryPlatformProductRestWrapper.getBackendInfoWithHighQualityBranding(
                currentProductConfiguration.getProductId(),
                cityId,
                new RestListener<BackendInfo>() {
                    @Override
                    public void onSuccess(BackendInfo backendInfo) {
                        backendInfoService.setBackendInfo(backendInfo);

                        onSuccessLoading();
                    }

                    @Override
                    public void onFailure() {
                        onFailureLoading();
                    }
                }
        );
    }
}
