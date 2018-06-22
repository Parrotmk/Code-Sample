package com.whaddyalove.filter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.soundcloud.android.crop.Crop;
import com.whaddyalove.R;
import com.whaddyalove.activities.AddTextImageActivity;
import com.whaddyalove.common.UrlConstants;
import com.whaddyalove.common.WhaddyaLoveConstants;
import com.whaddyalove.utils.ButtonPlus;
import com.whaddyalove.utils.DialogUtil;
import com.whaddyalove.utils.ImageLoadingUtils;
import com.whaddyalove.utils.SessionUtil;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageFilterActivity extends AppCompatActivity implements ThumbnailCallback {
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private Activity activity;
    private RecyclerView thumbListView;
    private ImageView placeHolderImageView;
    private Bitmap decodedByte, filterBitMap;
    private EditText caption, description;
    private AQuery aQuery;


    private static final int CAMERA_PIC_REQUEST = 1;
    private String fileName = "whaddyalove.jpg";
    private String mediaFileName;
    private String encodedImageString;
    private ImageLoadingUtils imageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filter);
        aQuery = new AQuery(this);
        imageUtil = new ImageLoadingUtils(this);
      /*  String bitmap = getIntent().getStringExtra("bitMap");
        byte[] decodedString = Base64.decode(bitmap, Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);*/
        activity = this;
        initUIWidgets();
    }

    private void initUIWidgets() {
        thumbListView = (RecyclerView) findViewById(R.id.thumbnails);
        placeHolderImageView = (ImageView) findViewById(R.id.place_holder_imageview);
//        placeHolderImageView.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 650, 650, false));
//        filterBitMap = Bitmap.createScaledBitmap(decodedByte, 650, 650, false);

        caption = (EditText) findViewById(R.id.edt_caption);
        description = (EditText) findViewById(R.id.edt_description);


        findViewById(R.id.btn_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!caption.getText().toString().trim().equalsIgnoreCase("")) {
                    encodeImageToString(filterBitMap, "Upload");
                } else {
                    caption.setError("Please enter cation");
                }
            }
        });
        findViewById(R.id.imgCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraHardware(ImageFilterActivity.this)) {
                    int permissionStatus = ContextCompat.checkSelfPermission(ImageFilterActivity.this,
                            Manifest.permission.CAMERA);
                    if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ImageFilterActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                WhaddyaLoveConstants.CAMERA_PERMISSION_REQUEST);
                    } else {
                        permissionStatus = ContextCompat.checkSelfPermission(ImageFilterActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ImageFilterActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    WhaddyaLoveConstants.WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST);
                        } else {
                            openCamera();
                        }
                    }
                }
            }
        });
        findViewById(R.id.imgGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });
    }


    private void openCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment.getExternalStorageDirectory(), fileName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WhaddyaLoveConstants.CAMERA_PERMISSION_REQUEST:
            case WhaddyaLoveConstants.WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (requestCode == WhaddyaLoveConstants.CAMERA_PERMISSION_REQUEST) {
                        int permissionStatus = ContextCompat.checkSelfPermission(ImageFilterActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ImageFilterActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    WhaddyaLoveConstants.WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST);
                        } else {
                            openCamera();
                        }
                    } else {
                        openCamera();
                    }
                } else {
                    if (requestCode == WhaddyaLoveConstants.CAMERA_PERMISSION_REQUEST) {
                        Toast.makeText(ImageFilterActivity.this, "Permission to access Camera is required to take a photo using Whaddya Love App.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ImageFilterActivity.this, "Permission to access photos, media and files is required to take a photo using Whaddya Love App.", Toast.LENGTH_SHORT).show();
                    }
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_PIC_REQUEST) {
                File f = new File(android.os.Environment.getExternalStorageDirectory(), fileName);
                Uri selectedImage = Uri.fromFile(f);
                Uri destination = Uri.fromFile(new File(getCacheDir(), "vd"));
                Crop.of(selectedImage, destination).asSquare().start(this);
            } else if (requestCode == 2) {
                Uri destination = Uri.fromFile(new File(getCacheDir(), "vd"));
                Crop.of(data.getData(), destination).asSquare().start(this);
            } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
                Uri selectedImage = Crop.getOutput(data);
                if (selectedImage.getAuthority() != null) {
                    String fileNameSegments[] = selectedImage.toString().split("/");
                    mediaFileName = fileNameSegments[fileNameSegments.length - 1];
                    if (mediaFileName.indexOf(".") == -1) {
                        mediaFileName = mediaFileName + sdf.format(date).toString() + ".jpg";
                    }
                    ParcelFileDescriptor parcelFileDescriptor = null;
                    try {
                        parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImage, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        bitmap = imageUtil.decodeBitmapFromFileDescriptor(fileDescriptor);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (parcelFileDescriptor != null)
                                parcelFileDescriptor.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
//                    userImage.setImageBitmap(bitmap);
                    encodeImageToString(bitmap, "camera");
                }
            } else if (requestCode == 2412) {
                findViewById(R.id.btn_post).setEnabled(true);
                findViewById(R.id.btn_post).setBackgroundResource(R.drawable.button_circle_shape);
                ((ButtonPlus) findViewById(R.id.btn_post)).setTextColor(getResources().getColor(R.color.white));
                String path = data.getStringExtra("imagePath");
                byte[] decodedString = Base64.decode(path, Base64.DEFAULT);
                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                placeHolderImageView.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 650, 650, false));
                filterBitMap = Bitmap.createScaledBitmap(decodedByte, 650, 650, false);
                initHorizontalList();
            }

        } else {
            Toast.makeText(ImageFilterActivity.this, "No Image Selected !!", Toast.LENGTH_SHORT).show();
        }

    }


    public void encodeImageToString(final Bitmap bitmap, final String upload) {
        encodedImageString = "";
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                encodedImageString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                if (upload.equalsIgnoreCase("Upload")) {
                    uploadNewPost(encodedImageString, caption.getText().toString(), description.getText().toString());
                } else {
                    Intent i = new Intent(ImageFilterActivity.this, AddTextImageActivity.class);
                    i.putExtra("bitMap", encodedImageString);
                    startActivityForResult(i, 2412);
                }
//                captureImageDialog.show();
            }
        }.execute(null, null, null);
    }

    private void initHorizontalList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        thumbListView.setLayoutManager(layoutManager);
        thumbListView.setHasFixedSize(true);
        bindDataToAdapter();
    }

    private void bindDataToAdapter() {
        final Context context = this.getApplication();
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                Bitmap thumbImage = Bitmap.createScaledBitmap(decodedByte, 640, 640, false);
                ThumbnailItem t1 = new ThumbnailItem();
                ThumbnailItem t2 = new ThumbnailItem();
                ThumbnailItem t3 = new ThumbnailItem();
                ThumbnailItem t4 = new ThumbnailItem();
                ThumbnailItem t5 = new ThumbnailItem();
                ThumbnailItem t6 = new ThumbnailItem();

                t1.image = thumbImage;
                t2.image = thumbImage;
                t3.image = thumbImage;
                t4.image = thumbImage;
                t5.image = thumbImage;
                t6.image = thumbImage;
                ThumbnailsManager.clearThumbs();
                ThumbnailsManager.addThumb(t1); // Original Image

                t2.filter = SampleFilters.getStarLitFilter();
                ThumbnailsManager.addThumb(t2);

                t3.filter = SampleFilters.getBlueMessFilter();
                ThumbnailsManager.addThumb(t3);

                t4.filter = SampleFilters.getAweStruckVibeFilter();
                ThumbnailsManager.addThumb(t4);

                t5.filter = SampleFilters.getLimeStutterFilter();
                ThumbnailsManager.addThumb(t5);

                t6.filter = SampleFilters.getNightWhisperFilter();
                ThumbnailsManager.addThumb(t6);

                List<ThumbnailItem> thumbs = ThumbnailsManager.processThumbs(context);

                ThumbnailsAdapter adapter = new ThumbnailsAdapter(thumbs, (ThumbnailCallback) activity);
                thumbListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }

    @Override
    public void onThumbnailClick(Filter filter) {
        placeHolderImageView.setImageBitmap(filter.processFilter(Bitmap.createScaledBitmap(decodedByte, 650, 650, false)));
        filterBitMap = filter.processFilter(Bitmap.createScaledBitmap(decodedByte, 650, 650, false));
    }



    private void uploadNewPost(String imagePath, String caption, String description) {
        if (DialogUtil.checkInternetConnection(ImageFilterActivity.this)) {
            String url = UrlConstants.BASE_URL + UrlConstants.ADD_POST;
            ProgressDialog progress = new ProgressDialog(ImageFilterActivity.this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            Map params = new HashMap();
            params.put("user_id", SessionUtil.getUserId(ImageFilterActivity.this));
            params.put("caption", caption);
            params.put("description", description);
            params.put("post_image", imagePath);
            aQuery.progress(progress).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    if (json != null) {
                        try {
                            if (json.getInt("responseCode") == 1) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                finish();
                            } else {
                                Toast.makeText(ImageFilterActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(ImageFilterActivity.this, "Couldn't connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            DialogUtil.showNoConnectionDialog(ImageFilterActivity.this);
        }
    }
}
