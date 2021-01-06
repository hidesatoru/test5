package com.example.test5;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private Canvas canvas;
    private LocationManager locationManager;    //ロケーションマネージャー
    private TextView textView;                  //速度表示用
    private int speed;                   //速度用の変数
    private ArrayList<Integer> speedlist;
    private ArrayList<String> date1;
    private String date;

    private boolean flg;                //ガイドライン判断

    private final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final int REQUEST_PERMISSION = 1000;

    private Guideline guideline;
    private LinearLayout layout;
    private Button bar;

    static final int REQUEST_OPEN_FILE = 1001;
    static final int REQUEST_CREATE_FILE = 1002;
    static final int REQUEST_DELETE_FILE = 1003;

    private MyButton start;
    private MyButton hozon;
    private MyButton clear;
    private MyButton yomu;
    private MyButton stop;
    private String hozondate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canvas = findViewById(R.id.view);
        textView = findViewById(R.id.textView);

        hozon = findViewById(R.id.hozon);
        hozon.setname("保存");
        yomu = findViewById(R.id.yomu);
        yomu.setname("読み込み");
        clear = findViewById(R.id.clear);
        clear.setname("データ削除");
        start = findViewById(R.id.start);
        start.setname("開始");
        stop = findViewById(R.id.stop);
        stop.setname("停止");

        guideline = findViewById(R.id.guideline);
        layout = findViewById(R.id.layout);
        bar = findViewById(R.id.bar);

        hozondate = getNowDatehozon() + ".csv";

        flg = true;

        speedlist = new ArrayList<Integer>();
        date1 = new ArrayList<String>();

        speed = 0;
        date = "";
        flg = true;

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int windowWidth = point.x;
        int windoHeight = point.y;

        canvas.sizeSet(windowWidth, windoHeight);

        canvas.setPath1();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        canvas.setPath2();

        checkPermission();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        super.onPause();

        speedlist = canvas.getSpeedlist();
        date1 = canvas.getDate1();
        for (int i = 0; i < speedlist.size(); i++) {
            String str = speedlist.get(i).toString();
            String str2 = date1.get(i).toString();
            try {
                FileOutputStream fileOutputStream =
                        openFileOutput("hozon.txt", Context.MODE_PRIVATE);
                fileOutputStream.write(str.getBytes());
                fileOutputStream.write(",".getBytes());
                fileOutputStream.write(str2.getBytes());
                fileOutputStream.write(",".getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //ファイルが存在しない場合の処理
            } catch (IOException e) {
                e.printStackTrace();
                //入出力エラーが発生した場合
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        canvas.dataclear2();

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int windowWidth = point.x;
        int windoHeight = point.y;

        canvas.sizeSet(windowWidth, windoHeight);

        canvas.setPath1();

        try {
            FileInputStream fileInputStream =
                    openFileInput("hozon.txt");
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
            String lineBuffer;
            while (true) {
                lineBuffer = bufferedReader.readLine();
                if (lineBuffer != null) {
                    String[] text2 = lineBuffer.split(",", 0);
                    for (int i = 0; i < text2.length; i++) {
                        if (i == 0) {
                            int sp = Integer.valueOf(text2[i]);
                            speedlist.add(sp);
                        } else if (i % 2 == 0) {
                            int sp = Integer.valueOf(text2[i]);
                            speedlist.add(sp);
                        } else {
                            date1.add(text2[i]);
                        }
                    }
                } else {
                    break;
                }
            }
            canvas.setSpeedlist(speedlist);
            canvas.setDate1(date1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        textView.setText("0 km/h");
        canvas.setPath2();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        canvas.setPath2();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();

        deleteFile("hozon.txt");

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLocationChanged(Location location) {
        if (location.hasSpeed()) {
            //速度が出ている時（km/hに変換して変数speedへ）
            speed = (int) (location.getSpeed() * 3.6f);
        } else {
            //速度が出ていない時
            speed = 0;
        }
        date = getNowDate();

        //速度を表示する
        textView.setText(speed + " km/h");
        canvas.dataset(speed, date);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getNowDate() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getNowDatehozon() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    public void stop() {
        locationManager.removeUpdates(this);
        Intent intent = new Intent(getApplication(), LocationService.class);
        stopService(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkPermission() {
        if (isGranted()) {
//            setGps();
            setEvent();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                return;
            }
        } else {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSION);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkPermission2() {
        if (isGranted()) {
            setGps();
        } else {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSION);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setGps() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);


        final Intent serviceStart = new Intent(this.getApplication(), LocationService.class);
        startForegroundService(serviceStart);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isGranted(){
        for (int i = 0; i < PERMISSIONS.length; i++){

            if (checkSelfPermission(PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(PERMISSIONS[i])) {
                    Toast.makeText(this, "アプリを実行するためには許可が必要です", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        }
        return true;
    }

    private void setEvent(){

        start.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    start.setground1();
                }

                if(event.getAction() == MotionEvent.ACTION_MOVE){

                }

                if(event.getAction() == MotionEvent.ACTION_UP){
                    start.setground2();
                    canvas.dataclear();
                    checkPermission2();
//                setGps();
//                gpsflg = true;
                    if(flg == false){
                        guideline.setGuidelinePercent(0.9f);
                        hozon.setVisibility(View.INVISIBLE);
                        yomu.setVisibility(View.INVISIBLE);
                        clear.setVisibility(View.INVISIBLE);

                        flg = true;
                        canvas.setPath2();
                    }
                    Toast.makeText(getApplicationContext(), "データ取得開始", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        hozon.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    hozon.setground1();
                }

                if(event.getAction() == MotionEvent.ACTION_MOVE){

                }

                if(event.getAction() == MotionEvent.ACTION_UP){
                    hozon.setground2();
                    stop();
                    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    intent.setType("text/csv");
                    intent.putExtra(Intent.EXTRA_TITLE, hozondate);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(Intent.createChooser(intent, "Create a file"), REQUEST_CREATE_FILE);

                    if(flg == false){
                        guideline.setGuidelinePercent(0.9f);
                        hozon.setVisibility(View.INVISIBLE);
                        yomu.setVisibility(View.INVISIBLE);
                        clear.setVisibility(View.INVISIBLE);

                        flg = true;
                        canvas.setPath2();
                    }
                }
                return true;
            }
        });

        clear.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    clear.setground1();
                }

                if(event.getAction() == MotionEvent.ACTION_MOVE){

                }

                if(event.getAction() == MotionEvent.ACTION_UP){
                    clear.setground2();
                    stop();
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("text/csv");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(Intent.createChooser(intent, "Delete a file"), REQUEST_DELETE_FILE);

                    if(flg == false){
                        guideline.setGuidelinePercent(0.9f);
                        hozon.setVisibility(View.INVISIBLE);
                        yomu.setVisibility(View.INVISIBLE);
                        clear.setVisibility(View.INVISIBLE);

                        flg = true;
                        canvas.setPath2();
                    }
                }
                return true;
            }
        });

        stop.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    stop.setground1();
                }

                if(event.getAction() == MotionEvent.ACTION_MOVE){

                }

                if(event.getAction() == MotionEvent.ACTION_UP){
                    stop.setground2();
                    stop();

                    if(flg == false){
                        guideline.setGuidelinePercent(0.9f);
                        hozon.setVisibility(View.INVISIBLE);
                        yomu.setVisibility(View.INVISIBLE);
                        clear.setVisibility(View.INVISIBLE);

                        flg = true;
                        canvas.setPath2();
                    }
                    Toast.makeText(getApplicationContext(), "データ取得停止", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        yomu.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    yomu.setground1();
                }

                if(event.getAction() == MotionEvent.ACTION_MOVE){

                }

                if(event.getAction() == MotionEvent.ACTION_UP){
                    yomu.setground2();

                    stop();

                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("text/csv");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(Intent.createChooser(intent, "Open a file"), REQUEST_OPEN_FILE);

                    if(flg == false){
                        guideline.setGuidelinePercent(0.9f);
                        hozon.setVisibility(View.INVISIBLE);
                        yomu.setVisibility(View.INVISIBLE);
                        clear.setVisibility(View.INVISIBLE);

                        flg = true;
                        canvas.setPath2();
                    }
                }
                return true;
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flg == true) {
                    guideline.setGuidelinePercent(0.4f);
                    hozon.setVisibility(View.VISIBLE);
                    yomu.setVisibility(View.VISIBLE);
                    clear.setVisibility(View.VISIBLE);

                    flg = false;
                    canvas.setPath2();
                }
                else {
                    guideline.setGuidelinePercent(0.9f);
                    hozon.setVisibility(View.INVISIBLE);
                    yomu.setVisibility(View.INVISIBLE);
                    clear.setVisibility(View.INVISIBLE);

                    flg = true;
                    canvas.setPath2();
                }
            }
        });

        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flg == true) {
                    guideline.setGuidelinePercent(0.4f);
                    hozon.setVisibility(View.VISIBLE);
                    yomu.setVisibility(View.VISIBLE);
                    clear.setVisibility(View.VISIBLE);

                    flg = false;
                    canvas.setPath2();
                }
                else {
                    guideline.setGuidelinePercent(0.9f);
                    hozon.setVisibility(View.INVISIBLE);
                    yomu.setVisibility(View.INVISIBLE);
                    clear.setVisibility(View.INVISIBLE);

                    flg = true;
                    canvas.setPath2();
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION){
            checkPermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // File load
        if (requestCode == REQUEST_OPEN_FILE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();

                if (uri != null) {
                    String str = loadStrFromUri(uri);
                    textView.setText("0 km/h");
                    canvas.setPath2();
                    Toast.makeText(this, "読み込みました", Toast.LENGTH_SHORT).show();
                }
            }
        }
        // File save
        else if (requestCode == REQUEST_CREATE_FILE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();
                speedlist = canvas.getSpeedlist();
                date1 = canvas.getDate1();
                ArrayList<String> speed1 = new ArrayList<String>();
                for(int i = 0; i < speedlist.size(); i++){
                    speed1.add(String.valueOf(speedlist.get(i)));
                }

                File file = new File(uri.getPath());
                file.delete();

                saveStrToUri(uri, speed1, date1);
                Toast.makeText(this, "保存しました", Toast.LENGTH_SHORT).show();

            }
        }
        // File delete
        else if (requestCode == REQUEST_DELETE_FILE) {
            if (resultCode == RESULT_OK && data != null) {
                ClipData clipData = data.getClipData();
                if(clipData==null){  // single selection
                    Uri uri = data.getData();
                    deleteUri(uri);
                }else {  // multiple selection
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri uri = clipData.getItemAt(i).getUri();
                        deleteUri(uri);
                    }
                }
                Toast.makeText(this, "削除しました", Toast.LENGTH_SHORT).show();
            }
        }
    }

    String loadStrFromUri(Uri uri) {
        canvas.dataclear();
        String str = "";
        Boolean loop=true;

        try {
            if (uri.getScheme().equals("content")) {
                InputStream iStream = getContentResolver().openInputStream(uri);
                if(iStream==null) loop=false;
                BufferedReader reader = new BufferedReader(new InputStreamReader(iStream, "UTF-8"));
                String lineBuffer;
                while (loop) {
                    lineBuffer = reader.readLine();
                    if (lineBuffer != null){
                        String[] text2 = lineBuffer.split(",", 0);
                        for(int i = 0; i < text2.length; i++) {
                            if(i == 0) {
                                int sp = Integer.valueOf(text2[i]);
                                speedlist.add(sp);
                            }
                            else if(i % 2 == 0){
                                int sp = Integer.valueOf(text2[i]);
                                speedlist.add(sp);
                            }
                            else {
                                date1.add(text2[i]);
                            }
                        }
                    }
                    else {
                        break;
                    }
                }
                canvas.setSpeedlist(speedlist);
                canvas.setDate1(date1);
            } else {
                Toast.makeText(this, "Unknown scheme", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Cannot read the file:" + e.toString(), Toast.LENGTH_LONG).show();
        }
        return str;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void saveStrToUri(Uri uri, ArrayList<String> str, ArrayList<String> str2) {
        try {
            if (uri.getScheme().equals("content")) {
                OutputStream oStream = getContentResolver().openOutputStream(uri);
//                FileOutputStream oStream = new FileOutputStream(String.valueOf(uri), true);
                if(oStream!=null) {
                    for(int i = 0; i < str.size(); i++) {
                        oStream.write(str.get(i).getBytes());
                        oStream.write(",".getBytes());
                        oStream.write(str2.get(i).getBytes());
                        oStream.write(",".getBytes());
                    }
                    oStream.close();
                }
            } else {
                Toast.makeText(this, "Unknown scheme", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Cannot write the file:" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void deleteUri(Uri uri) {
        try {
            DocumentsContract.deleteDocument(getContentResolver(), uri);
        } catch(FileNotFoundException e){
            Toast.makeText(this, "Cannot find the file:" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }


}