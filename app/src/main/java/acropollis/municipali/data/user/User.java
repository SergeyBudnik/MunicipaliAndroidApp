package acropollis.municipali.data.user;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable {
    private UserId userId = new UserId();
    private UserServiceInfo userServiceInfo = new UserServiceInfo();
    private UserDetailsInfo userDetailsInfo = new UserDetailsInfo();
}
