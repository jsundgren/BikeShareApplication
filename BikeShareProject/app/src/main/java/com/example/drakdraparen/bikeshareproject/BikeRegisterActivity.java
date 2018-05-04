package com.example.drakdraparen.bikeshareproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.net.URI;

/**
 *  ###BIKEREGISTERACTIVITY###
 *  This class handles the UI elements and creation of
 *  the bike object when the user wants to add a bike.
 */

public class BikeRegisterActivity extends AppCompatActivity {

    static final int REQUEST_PHOTO = 1;
    private URI uri;
    private static BikesDB sBikesDB;
    private static WeakReference<BikeMapActivity> bma;
    public static void updateActivity(BikeMapActivity activity) {
        bma = new WeakReference<BikeMapActivity>(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_register);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        sBikesDB = BikesDB.get(this);
        final EditText addBikeType = findViewById(R.id.add_bike_type);
        final EditText addBikePrice = findViewById(R.id.add_bike_price);
        ImageButton addBikePicture = findViewById(R.id.add_pic);
        addBikePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera();
            }
        });
        Button addBikeButton = findViewById(R.id.add_bike);
        addBikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sBikesDB.getPictureOfBike() != null && addBikeType.getText().length() > 0 &&
                        addBikePrice.getText().length() > 0 && Integer.parseInt(addBikePrice.getText().toString()) > 0) {
                    sBikesDB.setLocation(bma.get().getEndCoord());
                    sBikesDB.setTypeAndPriceToBike(addBikeType.getText().toString(), Integer.parseInt(addBikePrice.getText().toString()));
                    sBikesDB.bikedAdded();
                    Intent intent = new Intent(BikeRegisterActivity.this, BikeMapActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(BikeRegisterActivity.this, "Please add all the needed information of the bike!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    /* Starts the camera as a implicit intent */
    public void startCamera(){
        Intent captureImage= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(captureImage, REQUEST_PHOTO);
    }

    /* When the user have taken a pic and clicked "ok" - ends the ride */
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode != Activity.RESULT_OK) { return; }
        if (reqCode == REQUEST_PHOTO) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            sBikesDB.setPictureToBike(convertBitmapToByteArray(imageBitmap));
            finishActivity(reqCode);
        }
    }

    /* Convert the taken picture into bytes to store in the database */
    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream buffer= new ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight());
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, buffer);
        return buffer.toByteArray();
    }
}
