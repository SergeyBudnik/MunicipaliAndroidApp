package acropollis.municipali.service;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import acropollis.municipali.dao.UserDao;
import acropollis.municipali.data.user.User;

@EBean
public class UserService {
    @Bean
    UserDao userDao;

    public User getCurrentUser() {
        return userDao.getUser();
    }

    public String getCurrentUserAuthToken() {
        return userDao.getAuthToken();
    }

    public byte [] getCurrentUserIcon() {
        return userDao.getIcon();
    }

    public void setCurrentUser(User user, String authToken, byte [] icon) {
        userDao.setUser(user, authToken, icon);
    }

    public void removeUser() {
        userDao.setUser(null, null, null);
    }
}
