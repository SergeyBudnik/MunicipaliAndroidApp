package acropollis.municipali.data.user;

import lombok.Data;

@Data
public class User {
    private UserId userId = new UserId();
    private UserServiceInfo userServiceInfo = new UserServiceInfo();
    private UserDetailsInfo userDetailsInfo = new UserDetailsInfo();
}
