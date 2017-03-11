package lab2.android.csulb.edu.photonotes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

public class TakePhoto extends AppCompatActivity {

    String location="";
    Button button,save;
    ImageView imageView;
    EditText editText;
    private static final int CAMERA_REQUEST = 1888;
    int flag=0;
    Bitmap finalBitmap;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        initialize();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption = editText.getText().toString();
                if(flag==1 && caption.length()!=0)
                {
                    saveImage();
                    DataGetSet dataGetSet = new DataGetSet();
                    dataGetSet.location = location;
                    dataGetSet.caption = editText.getText().toString();
                    sqLiteHelper.addtoList(dataGetSet);
                    Toast.makeText(getApplicationContext(), "Added Photo!", Toast.LENGTH_LONG).show();
                    onBackPressed();

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Take snap and add caption", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST) {

            finalBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(finalBitmap);
            button.setText("Retake");
            flag=1;
        }
    }

    private void initialize() {

        sqLiteHelper = new SQLiteHelper(getApplicationContext());

        button = (Button)findViewById(R.id.button);
        save = (Button)findViewById(R.id.button2);
        editText = (EditText)findViewById(R.id.edittext);

        imageView = (ImageView)findViewById(R.id.imageView2);

    }

    private void saveImage() {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/PhotoNotes");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            location = file.getAbsolutePath();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try{
            super.onSaveInstanceState(outState);
            if(finalBitmap!=null)
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                outState.putByteArray("image",byteArray);
                outState.putInt("flag",flag);
            }
        }
        catch (Exception e)
        {
            System.out.print(e.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        byte[] ba = savedInstanceState.getByteArray("image");
        flag = savedInstanceState.getInt("flag");
        if(ba!=null)
        {
            finalBitmap = BitmapFactory.decodeByteArray(ba, 0, ba.length);
            imageView.setImageBitmap(finalBitmap);
        }
    }
}
