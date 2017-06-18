package acropollis.municipali.push;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MunicipaliInstanceIDListenerService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        try {
            String token = FirebaseInstanceId.getInstance().getToken();

            Intent tokenObtainIntent = new Intent(MunicipaliGcmMessages.TOKEN_OBTAINED_SUCCESS);

            tokenObtainIntent.putExtra("token", token);

            sendBroadcast(tokenObtainIntent);
        } catch (Exception e) {
            sendBroadcast(new Intent(MunicipaliGcmMessages.TOKEN_OBTAINED_FAILURE));
        }
    }
}
