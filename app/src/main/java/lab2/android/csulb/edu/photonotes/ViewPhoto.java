package lab2.android.csulb.edu.photonotes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewPhoto extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView)findViewById(R.id.textView);

        Intent i = getIntent();
        String id = i.getStringExtra("id");

        SQLiteHelper sqLiteHelper = new SQLiteHelper(getApplicationContext());
        DataGetSet dgs = new DataGetSet();

        dgs = sqLiteHelper.getPhoto(id);

        Bitmap myBitmap = BitmapFactory.decodeFile(dgs.location);

        imageView.setImageBitmap(myBitmap);

        textView.setText(dgs.caption);

        dgs = sqLiteHelper.getPhoto(id);



    }
}
