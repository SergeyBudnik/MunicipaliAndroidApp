package acropollis.municipali;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.bootstrap.view.MunicipaliLoadingView;
import acropollis.municipali.configuration.ProductConfiguration;
import acropollis.municipali.data.backend.BackendInfo;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.rest.wrappers.alpha.StandaloneProductRestWrapper;
import acropollis.municipali.service.BackendInfoService;

@EActivity(R.layout.activity_loading)
public class StandaloneCustomerLoadingActivity extends BaseActivity {
    @ViewById(R.id.loading)
    MunicipaliLoadingView loadingView;

    @Bean
    BackendInfoService backendInfoService;

    @Bean
    StandaloneProductRestWrapper standaloneProductRestWrapper;

    @Extra("demoProductId")
    String demoProductId;

    @AfterViews
    public void init() {
        loadingView.bind(new MunicipaliLoadingView.Loader() {
            @Override
            public void onStart() {
                getBackendInfo();
            }

            @Override
            public MunicipaliLoadingView.LoadingFailureAction onFailure() {
                return MunicipaliLoadingView.LoadingFailureAction.STAY;
            }
        });

        loadingView.startLoading();
    }

    private void getBackendInfo() {
        final ProductConfiguration currentProductConfiguration = ProductConfiguration
                .getProductConfiguration(
                        demoProductId == null ? getResources().getString(R.string.product_id) : demoProductId
                );

        standaloneProductRestWrapper.getBackendInfoWithHighQualityBranding(
                currentProductConfiguration.getProductId(),
                new RestListener<BackendInfo>() {
                    @Override
                    public void onSuccess(BackendInfo backendInfo) {
                        backendInfoService.setBackendInfo(backendInfo);

                        redirect(RegistrationActivity_.class, 0, 0, true);
                    }

                    @Override
                    public void onFailure() {
                        loadingView.onLoadingFailed();
                    }
                }
        );
    }
}
