package lab2.android.csulb.edu.photonotes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewList extends AppCompatActivity {

    ListView lv;
    SQLiteHelper sqLiteHelper;
    ArrayList<DataGetSet> getSetData;
  //  private static final int PER_REQUEST_CODE1 = 100;
    //private static final int PER_REQUEST_CODE2 = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_view_list);
            Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(tb);


            try{

                if(ActivityCompat.checkSelfPermission(ViewList.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ViewList.this, new String[]{Manifest.permission.CAMERA}, 200);
                }
                else if(ActivityCompat.checkSelfPermission(ViewList.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ViewList.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                }
            }
            catch (Exception e)
            {
                System.out.print(e.toString());
            }

            lv = (ListView) findViewById(R.id.lv);
            sqLiteHelper = new SQLiteHelper(getApplicationContext());


            getSetData = new ArrayList<DataGetSet>();

            showlistdisplay();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(ViewList.this, ViewPhoto.class);
                    i.putExtra("id",getSetData.get(position).id);
                    startActivity(i);
                }
            });

            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    sqLiteHelper.deletePhoto(getSetData.get(position).id);
                    showlistdisplay();
                    return false;
                }
            });
        }
        catch (Exception e)
        {
            System.out.print(e.toString());
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent i = new Intent(ViewList.this, TakePhoto.class);
                startActivityForResult(i, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showlistdisplay();
    }

    private void showlistdisplay()
    {
        getSetData = sqLiteHelper.getListDB();

        ArrayList<String> captionslist = new ArrayList<String>();

        if(getSetData.size()==0)
        {
            lv.setAdapter(null);
            Toast.makeText(getApplicationContext(), "Empty List", Toast.LENGTH_LONG);
        }
        else
        {
            for(int i=0;i<getSetData.size();i++)
                captionslist.add(getSetData.get(i).caption);
            ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,captionslist);
            lv.setAdapter(adp);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Your permission
                ActivityCompat.requestPermissions(ViewList.this, new String[]{Manifest.permission.CAMERA}, 200);
            }
            else
            {
                Toast.makeText(this, "Please provide appropriate permissions to avoid any unexpected behavior.", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == 200){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Done
            }
            else {
                Toast.makeText(this, "Please provide appropriate permissions to avoid any unexpected behavior.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(R.id.action_settings==item.getItemId())
        {
            Intent i = new Intent(Intent.ACTION_DELETE);
            i.setData(Uri.parse("package:lab2.android.csulb.edu.photonotes"));
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
