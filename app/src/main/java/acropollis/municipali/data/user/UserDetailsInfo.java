package acropollis.municipali.data.user;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserDetailsInfo implements Serializable {
    public enum Gender {
        MALE, FEMALE, UNKNOWN
    }

    private String name;
    private Gender gender;
    private String email;
    private Long dateOfBirth;
}
