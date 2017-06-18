package acropollis.municipali.service;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import acropollis.municipali.dao.BackendInfoDao;
import acropollis.municipali.data.backend.BackendInfo;

@EBean
public class BackendInfoService {
    @Bean
    BackendInfoDao backendInfoDao;

    public BackendInfo getBackendInfo() {
        return backendInfoDao.getBackendInfo();
    }

    public void setBackendInfo(BackendInfo backendInfo) {
        backendInfoDao.setBackendInfo(backendInfo);
    }
}
