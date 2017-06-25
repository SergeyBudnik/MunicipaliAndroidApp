package acropollis.municipali.dao;

import android.content.Context;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.Serializable;

import acropollis.municipali.data.user.User;
import acropollis.municipali.service.ProductConfigurationService;
import lombok.Data;

@Data
class UserData implements Serializable {
    private User user = new User();
    private String authToken;
    private byte [] userIcon;
}

@EBean(scope = EBean.Scope.Singleton)
public class UserDao extends CommonDao<UserData> {
    @RootContext
    Context context;

    @Bean
    ProductConfigurationService productConfigurationService;

    public User getUser() {
        readCache(context, getFileName(), false);

        return cache.getUser();
    }

    public String getAuthToken() {
        readCache(context, getFileName(), false);

        return cache.getAuthToken();
    }

    public byte [] getIcon() {
        readCache(context, getFileName(), false);

        return cache.getUserIcon();
    }

    public void setUser(User user, String authToken, byte [] icon) {
        readCache(context, getFileName(), false);

        cache.setUser(user);
        cache.setAuthToken(authToken);
        cache.setUserIcon(icon);

        persist(context);
    }

    @Override
    protected UserData newInstance() {
        return new UserData();
    }

    @Override
    protected String getFileName() {
        return UserDao.class.getCanonicalName() +
                "." +
                productConfigurationService.getProductConfiguration().getProductId();
    }
}
