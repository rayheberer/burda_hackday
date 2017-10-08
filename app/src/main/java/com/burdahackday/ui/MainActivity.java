package com.burdahackday.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.burdahackday.api.ApiService;
import com.burdahackday.api.RetroClient;
import com.burdahackday.api.response.Result;
import com.burdahackday.model.Post;
import com.burdahackday.model.PostsDatabaseHelper;
import com.burdahackday.model.User;
import com.burdahackday.permission.PermissionsActivity;
import com.burdahackday.permission.PermissionsChecker;
import com.burdahackday.utils.DateTimeUtils;
import com.burdahackday.utils.ImageFilePath;
import com.burdahackday.utils.InternetConnection;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    /**
     * Permission List
     */
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

    /**
     * Context Variables
     */
    Context mContext;

    /**
     * Views
     */
    View parentView;
    ImageView imageView;
    TextView textView;

    /**
     * Image path to send
     */
    String imagePath;

    /**
     *
     */
    PermissionsChecker checker;

    /**
     *
     */
    Toolbar toolbar;
    ProgressDialog progressDialog;

    private static int SPLASH_TIME_OUT = 3000;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        /**
         * Parent View
         */
        parentView = findViewById(R.id.parentView);

        /**
         * Permission Checker Initialized
         */
        checker = new PermissionsChecker(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getString(R.string.string_title_upload_progressbar_));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(imagePath)) {

                    /**
                     * Uploading AsyncTask
                     */
                    if (InternetConnection.checkConnection(mContext)) {
                        /******************Retrofit***************/
                       // uploadImage();
                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time =&gt; "+c.getTime());

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());

                        User sampleUser = new User();
                        sampleUser.mName = "Steph";
                        sampleUser.mEmail = "sbakshi36@gmail.com";

                        Post samplePost = new Post();
                        samplePost.user = sampleUser;
                        samplePost.mImageUrl = imagePath;
                        samplePost.mDate = DateTimeUtils.parseDateTime(formattedDate, "yyyy-MM-dd HH:mm", "hh:mm a, dd-MMM-yyyy") ;

                        // Get singleton instance of database
                        PostsDatabaseHelper databaseHelper = PostsDatabaseHelper.getInstance(mContext);

                        // Add sample post to the database
                        databaseHelper.addPost(samplePost);
                        progressDialog.show();

                        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                            @Override
                            public void run() {
                                // This method will be executed once the timer is over
                                // Start your app main activity

                                Calendar c = Calendar.getInstance();
                                c.add(Calendar.YEAR ,1);
                                System.out.println("Current time =&gt; "+c.getTime());

                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String formattedDate = df.format(c.getTime());

                                User sampleUser = new User();
                                sampleUser.mName = "Steph";
                                sampleUser.mEmail = "sbakshi36@gmail.com";

                                Post samplePost = new Post();
                                samplePost.user = sampleUser;
                                samplePost.mImageUrl = "https://scontent.fmuc2-1.fna.fbcdn.net/v/t34.0-12/22330637_10212792218793112_1682871404_n.png?oh=ad56ed71aafddc9d88ed697338f43fa8&oe=59DB7B2D";
                                samplePost.mDate = DateTimeUtils.parseDateTime(formattedDate, "yyyy-MM-dd HH:mm", "hh:mm a, dd-MMM-yyyy") ;

                                // Get singleton instance of database
                                PostsDatabaseHelper databaseHelper = PostsDatabaseHelper.getInstance(mContext);

                                // Add sample post to the database
                                databaseHelper.addPost(samplePost);
                                Intent next = new Intent(MainActivity.this,TimeLineActivity.class);
                                startActivity(next);

                                // close this activity
                                finish();
                            }
                        }, SPLASH_TIME_OUT);


                    } else {
                        Snackbar.make(parentView, R.string.string_internet_connection_warning, Snackbar.LENGTH_INDEFINITE).show();
                    }
                } else {
                    Snackbar.make(parentView, R.string.string_message_to_attach_file, Snackbar.LENGTH_INDEFINITE).show();
                }
            }
        });
    }

    /**
     * Upload Image Client Code
     */
    private void uploadImage() {

        /**
         * Progressbar to Display if you need
         */


        progressDialog.show();

        //Create Upload Server Client
        ApiService service = RetroClient.getApiService();

        //File creating from selected URL
        File file = new File(imagePath);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        Call<Result> resultCall = service.uploadImage(body);

        // finally, execute the request
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                progressDialog.dismiss();

                // Response Success or Fail
                if (response.isSuccessful()) {
                    if (response.body().getResult().equals("success"))
                        Snackbar.make(parentView, R.string.string_upload_success, Snackbar.LENGTH_LONG).show();
                    else
                        Snackbar.make(parentView, R.string.string_upload_fail, Snackbar.LENGTH_LONG).show();

                } else {
                    Snackbar.make(parentView, R.string.string_upload_fail, Snackbar.LENGTH_LONG).show();
                }

                /**
                 * Update Views
                 */
                imagePath = "";
                /*textView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);*/
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    /**
     * Showing Image Picker
     */
    public void showImagePopup() {
        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
        } else {
            // File System.
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_PICK);

            // Chooser of file system options.
            final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.string_choose_image));
            startActivityForResult(chooserIntent, 1010);
        }
    }

    /***
     * OnResult of Image Picked
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1010) {
            if (data == null) {
                Snackbar.make(parentView, R.string.string_unable_to_pick_image, Snackbar.LENGTH_INDEFINITE).show();
                return;
            }
            Uri uri = data.getData();


            imagePath = ImageFilePath.getPath(MainActivity.this, data.getData());
            Picasso.with(mContext).load(new File(imagePath))
                    .fit().centerCrop().into(imageView);;
            textView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            Snackbar.make(parentView, R.string.string_reselect, Snackbar.LENGTH_LONG).show();
//                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());

            Log.i("tag", "onActivityResult: file path : " + imagePath);
           /* Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(columnIndex);

                Picasso.with(mContext).load(new File(imagePath))
                        .into(imageView);

                Snackbar.make(parentView, R.string.string_reselect, Snackbar.LENGTH_LONG).show();
                cursor.close();

                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                Snackbar.make(parentView, R.string.string_unable_to_load_image, Snackbar.LENGTH_LONG).show();
            }*/
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_camera) {
            showImagePopup();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }
}
