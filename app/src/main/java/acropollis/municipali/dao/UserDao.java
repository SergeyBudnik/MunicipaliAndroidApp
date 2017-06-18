package acropollis.municipali.dao;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.Serializable;

import acropollis.municipali.data.user.User;
import lombok.Data;

@Data
class UserData implements Serializable {
    private User user;
    private String authToken;
    private byte [] userIcon;
}

@EBean(scope = EBean.Scope.Singleton)
public class UserDao extends CommonDao<UserData> {
    private static final String FILE_NAME = UserDao.class.getCanonicalName();

    @RootContext
    Context context;

    public User getUser() {
        readCache(context, FILE_NAME, false);

        return cache.getUser();
    }

    public String getAuthToken() {
        readCache(context, FILE_NAME, false);

        return cache.getAuthToken();
    }

    public byte [] getIcon() {
        readCache(context, FILE_NAME, false);

        return cache.getUserIcon();
    }

    public void setUser(User user, String authToken, byte [] icon) {
        readCache(context, FILE_NAME, false);

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
        return FILE_NAME;
    }
}
