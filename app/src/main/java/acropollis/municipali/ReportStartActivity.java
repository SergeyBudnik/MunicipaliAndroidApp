package acropollis.municipali;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import acropollis.municipali.binders.MenuBinder;

@EActivity(R.layout.activity_report_start)
public class ReportStartActivity extends BaseActivity {
    private static final String AUTHORITY = BuildConfig.AUTHORITY;

    private static final int MAKE_REPORT_FROM_CAMERA = 0;
    private static final int MAKE_REPORT_FROM_GALLERY = 1;
    private static final int MAKE_REPORT_WITHOUT_PHOTO = 2;
    private static final int SEND_REPORT = 3;

    @ViewById(R.id.root)
    DrawerLayout rootView;

    @Bean
    MenuBinder menuBinder;

    private Uri photoUri = null;

    @AfterViews
    void init() {
        menuBinder.bind(rootView);
    }

    @Click(R.id.menu_button)
    void onMenuClick() {
        rootView.openDrawer(Gravity.START);
    }

    @Click(R.id.report_from_camera)
    void onReportFromCameraClick() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();

                photoUri = FileProvider.getUriForFile(this, AUTHORITY, photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                startActivityForResult(takePictureIntent, MAKE_REPORT_FROM_CAMERA);
            } catch (IOException e) {
                /* Do nothing */
            }
        }
    }

    @Click(R.id.report_from_gallery)
    void onReportFromGalleryClick() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, MAKE_REPORT_FROM_GALLERY);
    }

    @Click(R.id.report_without_photo)
    void onReportWithoutPhoto() {
        redirectForResult(ReportSendActivity_.class, 0, 0, SEND_REPORT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MAKE_REPORT_FROM_GALLERY:
                    if (data != null) {
                        Map<String, Serializable> extras = new HashMap<>(); {
                            extras.put("photoUri", "content://media/" + data.getData().getPath());
                        }

                        redirectForResult(ReportSendActivity_.class, 0, 0, SEND_REPORT, extras);
                    }
                    break;
                case MAKE_REPORT_FROM_CAMERA:
                    Map<String, Serializable> extras = new HashMap<>(); {
                        extras.put("photoUri", photoUri.toString());
                    }

                    redirectForResult(ReportSendActivity_.class, 0, 0, SEND_REPORT, extras);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        redirect(ArticlesListActivity_.class, 0, 0, true);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Municipali_Report_" + timeStamp + "_";

        return File.createTempFile(
                imageFileName,
                ".jpg",
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        );
    }
}
