package com.arcore.ruler;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    //첫실행체크
    public SharedPreferences prefs;

    public void checkFirstRun(){
        boolean isFirstRun = prefs.getBoolean("isFirstRun",true);
        if(isFirstRun)
        {
            Intent newIntent = new Intent(MainActivity.this, ManualActivity.class);
            startActivity(newIntent);

            prefs.edit().putBoolean("isFirstRun",false).apply();
            //처음만 true 그다음부터는 false 바꾸는 동작
        }
    }

    public static final int IMAGE_GALLERY_REQUEST = 20;
    Button btn_measure;
    Button btn_locate;
    Button btn_gallery;
    Button btn_board;
    TextView txt_iden;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //첫실행
        prefs = getSharedPreferences("Pref", MODE_PRIVATE);
        this.checkFirstRun();

        //로그인된 사용자 정보 받아오는 코드
        Intent sessintent = getIntent();
        String userID = sessintent.getExtras().getString("userID");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File dir_main = new File(Environment.getExternalStorageDirectory() + File.separator + "Ruler");
        dir_main.mkdirs();
        File dir_images = new File(Environment.getExternalStorageDirectory() + File.separator + "Ruler/images");
        dir_images.mkdirs();
        File dir_obj = new File(Environment.getExternalStorageDirectory() + File.separator + "Ruler/obj");
        dir_obj.mkdirs();



        txt_iden = (TextView)findViewById(R.id.identext);
        btn_measure = (Button)findViewById(R.id.btn_measure);
        btn_locate = (Button)findViewById(R.id.btn_locate);
        btn_gallery = (Button)findViewById(R.id.btn_gallery);
        btn_board = (Button)findViewById(R.id.btn_board);

        txt_iden.setText(userID + "님 어서오세요!");

        btn_measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MeasureActivity.class);
                startActivity(intent);
            }
        });

        btn_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocateActivity.class);
                startActivity(intent);
            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File[] imageFiles;
                imageFiles = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Ruler/images").listFiles();
                if(imageFiles.length<1){
                    Toast.makeText(getApplicationContext(),"저장된 사진이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
                    startActivity(intent);
                }
            }
        });
        btn_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater mainInflater = getMenuInflater();
        mainInflater.inflate(R.menu.main_option, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.a:
                Toast.makeText(getApplicationContext(), "강세준/김재석/노원종", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.b:
                Toast.makeText(getApplicationContext(), "Ruler v1.0", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.c:
                Toast.makeText(getApplicationContext(), "메뉴얼로 이동합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ManualActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    private void requestCameraPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 0);
        }
    }

    private void requestMemoryPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestCameraPermission();
        requestMemoryPermission();
    }

    /*public void onGalleryClicked(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();

        Uri data = Uri.parse(pictureDirectoryPath);

        photoPickerIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }*/
}
