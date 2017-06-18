package acropollis.municipali.rest.raw.omega;

import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import acropollis.municipali.data.user.User;
import acropollis.municipali.rest.response.RegistrationResponse;

@Rest(converters = MappingJacksonHttpMessageConverter.class)
public interface UserRestService {
    @Post("{rootEndpoint}/user")
    RegistrationResponse register(String rootEndpoint, User user);
}
