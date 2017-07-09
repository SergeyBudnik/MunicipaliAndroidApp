package acropollis.municipali.data.user;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserId implements Serializable {
    public enum LoginType {
        FACEBOOK, GOOGLE_PLUS, NONE
    }

    private LoginType loginType;
    private String userId;
}