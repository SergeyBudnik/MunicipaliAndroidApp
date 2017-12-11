package acropollis.municipali.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.Html;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import acropollis.municipali.R;
import acropollis.municipali.activities.StartActivity_;

public class MunicipaliGcmListenerService extends FirebaseMessagingService {
    private static final int NOTIFICATION_ID = 435345;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(
                remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody()
        );
    }

    private void showNotification(String title, String text) {
        Intent notificationIntent = new Intent(getBaseContext(), StartActivity_.class); {
            notificationIntent
                    .setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
                    );
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getBaseContext(),
                1,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.launcher_icon)
                        .setContentTitle(title)
                        .setContentText(Html.fromHtml(text))
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();

        NotificationManager notificationManager = (NotificationManager) getBaseContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
