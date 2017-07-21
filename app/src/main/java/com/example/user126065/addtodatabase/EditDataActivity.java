package com.example.user126065.addtodatabase;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by user126065 on 7/20/17.
 */

public class EditDataActivity  extends AppCompatActivity
{
    private static final String TAG = "EditDataActivity";
    private Button btnSave,btnDelete,edit_choose_button;
    private EditText editable_item;
    ImageView edit_image;
    DatabaseHelper mDatabaseHelper;
    final int REQUEST_CODE_GALLERY = 999;
    private String selectedName;
    byte[]  selectedImage;
    private int selectedID;
    private static final String IMAGE_URI_KEY = "IMAGE_URI";
    private static final String BITMAP_WIDTH = "BITMAP_WIDTH";
    private static final String BITMAP_HEIGHT = "BITMAP_HEIGHT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        edit_choose_button = (Button) findViewById(R.id.edit_choose_button);
        editable_item = (EditText) findViewById(R.id.editable_item);
        edit_image= (ImageView) findViewById(R.id.edit_imageView);
        mDatabaseHelper = new DatabaseHelper(this);
        Intent receivedIntent = getIntent();
        //now get the itemID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value
        selectedName = receivedIntent.getStringExtra("name");
        Cursor data  = mDatabaseHelper.getItemImage(selectedID);
        byte[] myimage= null;
        while (data.moveToNext())
        {
            myimage = data.getBlob(2);


        }
        Bundle bundle = this.getIntent().getExtras();


        Bitmap bitmap = BitmapFactory.decodeByteArray(myimage, 0, myimage.length);



        editable_item.setText(selectedName);
        edit_image.setImageBitmap(bitmap);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String item = editable_item.getText().toString();
                byte[] NewEntry7 = imageViewToByte(edit_image);
                if(!item.equals(""))
                {
                    mDatabaseHelper.updateName(item,selectedID,selectedName,"name");
                    mDatabaseHelper.updateImage(NewEntry7,selectedID,"newimage");

                }
                else
                {
                    toastMessage("You must enter a name");
                }




                toastMessage("DATA SAVED");
                Intent intent = new Intent(EditDataActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseHelper.deleteName(selectedID,selectedName);
                editable_item.setText("");
                toastMessage("removed from database");
                Intent intent = new Intent(EditDataActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        edit_choose_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        EditDataActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                edit_image.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
    public byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
