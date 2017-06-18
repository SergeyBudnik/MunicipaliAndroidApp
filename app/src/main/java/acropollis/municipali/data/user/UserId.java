package acropollis.municipali.data.user;

import lombok.Data;

@Data
public class UserId {
    public enum LoginType {
        FACEBOOK, GOOGLE_PLUS, NONE
    }

    private LoginType loginType;
    private String userId;
}