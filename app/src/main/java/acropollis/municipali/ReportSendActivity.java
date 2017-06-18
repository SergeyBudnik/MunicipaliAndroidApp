package acropollis.municipali;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import acropollis.municipali.binders.MenuBinder;
import acropollis.municipali.configuration.ProductConfiguration;
import acropollis.municipali.data.common.Language;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.rest.wrappers.omega.ReportRestWrapper;
import acropollis.municipali.service.UserService;

import static acropollis.municipali.utls.BitmapUtils.iconToBytes;
import static acropollis.municipali.utls.BitmapUtils.rotate;
import static acropollis.municipali.utls.BitmapUtils.scale;

@EActivity(R.layout.activity_report_send)
public class ReportSendActivity extends BaseActivity {
    private static final int COMMENTS_REQUEST_CODE = 1;

    @ViewById(R.id.root)
    DrawerLayout rootView;

    @ViewById(R.id.photo_layout)
    RelativeLayout photoLayoutView;
    @ViewById(R.id.photo)
    ImageView photoView;

    @ViewById(R.id.write_comment_placeholder)
    TextView writeCommentPlaceholderView;
    @ViewById(R.id.comment)
    TextView commentView;
    @ViewById(R.id.send_button)
    View sendButtonView;

    @ViewById(R.id.rotate_button)
    View rotateButtonView;

    @ViewById(R.id.spinner)
    View spinnerView;
    @ViewById(R.id.spinner_layout)
    View spinnerLayoutView;
    @ViewById(R.id.send_notification)
    View sendNotificationView;

    @Extra("photoUri")
    String photoUri;

    @Bean
    MenuBinder menuBinder;

    @Bean
    UserService userService;

    @Bean
    ReportRestWrapper reportRestWrapper;

    @AnimationRes(R.anim.spinner)
    Animation spinnerAnimation;
    @AnimationRes(R.anim.report_send_notification)
    Animation reportSendNotificationAnimation;

    private byte [] photoToSendBytes;
    private String photoComments = "";

    private Bitmap initialBitmap;

    private int angle = 0;

    private AtomicBoolean rotateIsInProgress = new AtomicBoolean(false);

    @AfterViews
    void init() {
        menuBinder.bind(rootView);

        rotateButtonView.setVisibility(View.GONE);

        if (photoUri != null) {
            try {
                initialBitmap = getInitialBitmap();

                spinnerLayoutView.setVisibility(View.VISIBLE);
                spinnerView.startAnimation(spinnerAnimation);

                sendButtonView.setBackgroundResource(R.drawable.primary_button_disabled);

                photoLayoutView.post(new Runnable() {
                    @Override
                    public void run() {
                        processUIBitmap();
                    }
                });
            } catch (IOException e) {
                finishRedirectForResult(0, 0, RESULT_CANCELED);
            }
        }

        reportSendNotificationAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                sendNotificationView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sendNotificationView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COMMENTS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                photoComments = data.getStringExtra("comment").trim();

                writeCommentPlaceholderView.setVisibility(photoComments.isEmpty() ? View.VISIBLE : View.GONE);

                commentView.setVisibility(photoComments.isEmpty() ? View.GONE : View.VISIBLE);
                commentView.setText(photoComments);
            }
        }
    }

    @Click(R.id.rotate_button)
    void onRotateClick() {
        if (!rotateIsInProgress.get()) {
            synchronized (this) {
                if (!rotateIsInProgress.get()) {
                    sendButtonView.setBackgroundResource(R.drawable.primary_button_disabled);
                    rotateButtonView.setVisibility(View.GONE);

                    rotateIsInProgress.set(true);

                    angle += 90;

                    if (angle >= 360) {
                        angle = 0;
                    }

                    spinnerLayoutView.setVisibility(View.VISIBLE);
                    spinnerView.startAnimation(spinnerAnimation);

                    processUIBitmap();
                }
            }
        }
    }

    @Click(R.id.back_button)
    void onBackClick() {
        failureRedirect();
    }

    @Click(R.id.send_button)
    void onSendButtonClick() {
        if (!rotateIsInProgress.get()) {
            synchronized (this) {
                if (!rotateIsInProgress.get()) {
                    sendButtonView.setBackgroundResource(R.drawable.primary_button_disabled);

                    spinnerLayoutView.setVisibility(View.VISIBLE);
                    spinnerView.startAnimation(spinnerAnimation);

                    sendReport();
                }
            }
        }
    }

    @Click(R.id.write_comment)
    void onCommentClick() {
        Map<String, Serializable> extras = new HashMap<>();

        extras.put("photoUri", photoUri);
        extras.put("comment", photoComments);

        redirectForResult(ReportWriteCommentActivity_.class, 0, 0, COMMENTS_REQUEST_CODE, extras);
    }

    @Background
    void sendReport() {
        reportRestWrapper.postReport(photoToSendBytes, photoComments, 0.0, 0.0, new RestListener<Void>() {
            @Override
            public void onSuccess(Void o) {
                successRedirect();
            }

            @Override
            public void onFailure() {
                failureRedirect();
            }
        });
    }

    @UiThread
    void successRedirect() {
        spinnerView.clearAnimation();
        spinnerLayoutView.setVisibility(View.INVISIBLE);

        sendNotificationView.startAnimation(reportSendNotificationAnimation);

        doSuccssRedirect();
    }

    @UiThread(delay = 600)
    void doSuccssRedirect()  {
        finishRedirectForResult(0, 0, RESULT_OK);
    }

    @UiThread
    void failureRedirect() {
        spinnerView.clearAnimation();
        spinnerLayoutView.setVisibility(View.INVISIBLE);

        finishRedirectForResult(0, 0, RESULT_CANCELED);
    }

    @Background
    void processUIBitmap() {
        Bitmap bitmap = rotate(initialBitmap, angle);

        bitmap = scale(bitmap, 800, bitmap.getHeight() * 800 / bitmap.getWidth());

        photoToSendBytes = iconToBytes(bitmap);

        bitmap = scale(
                bitmap,
                bitmap.getWidth() * photoLayoutView.getHeight() / bitmap.getHeight(),
                photoLayoutView.getHeight()
        );

        onUiBitmapProcessed(bitmap);
    }

    @UiThread
    void onUiBitmapProcessed(Bitmap bitmap) {
        spinnerView.clearAnimation();
        spinnerLayoutView.setVisibility(View.INVISIBLE);

        sendButtonView.setBackgroundResource(R.drawable.primary_button_enabled);
        rotateButtonView.setVisibility(View.VISIBLE);

        photoView.setImageBitmap(bitmap);

        rotateIsInProgress.set(false);
    }

    private Bitmap getInitialBitmap() throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                getContentResolver(),
                Uri.parse(photoUri)
        );

        int minSize = Math.min(bitmap.getWidth(), bitmap.getHeight());

        return scale(
                bitmap,
                bitmap.getWidth() * 800 / minSize,
                bitmap.getHeight() * 800 / minSize
        );
    }

    private Language getCurrentLanguage() {
        return ProductConfiguration
                .getProductConfiguration(getResources().getString(R.string.product_id))
                .getLanguage();
    }
}
