package acropollis.municipali.data.user;

import lombok.Data;

@Data
public class UserDetailsInfo {
    public enum Gender {
        MALE, FEMALE, UNKNOWN
    }

    private String name;
    private Gender gender;
    private String email;
    private Long dateOfBirth;
}
