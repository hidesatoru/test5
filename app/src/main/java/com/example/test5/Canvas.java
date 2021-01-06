package com.example.test5;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Canvas extends View {
    private int posX;
    private int posY;
    private Paint paint;        //グラフメイン用
    private Path path;
    private Paint paint2;       //グラフ数値用
    private Path path2;
    private Paint paint3;       //グラフ補助線用
    private Path path3;
    private Paint paint4;
    private Path path4;         //時刻表示用
    private ArrayList<Line> list;
    private Line line;
    private ArrayList<Line> list2;      //時刻表示用
    private Line line2;                 //時刻表示用
    private float takasa1memori;            //高さ１目盛り分
    private float yoko1memori;           //横１目盛り追加分
    private float f3;           //速度値仮
    private int windowWidth;    //画面幅
    private int windowHeight;   //画面高さ
    private float takasa;
    private float yoko;
    private ArrayList<Integer> speedlist;
    private ArrayList<String> date1;
    private boolean flg;
    private boolean flg2;
    private String date;
    private int counthojo;      //ループ処理補助
    private Line line3;             //補助線表示用
    private ArrayList<Line> list3;  //補助線表示用
    private Path path5;             //補助線表示用
    private Paint paint5;           //補助線表示用
    private Line line4;             //補助単位表示用
    private ArrayList<Line> list4;  //補助単位表示用
    private Path path6;             //補助単位表示用
    private Paint paint6;           //補助単位表示用
//    private Line line5;             //メイン線表示用
//    private ArrayList<Line> list5;  //メイン線表示用


    public Canvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        posX = 0;
        posY = 0;

        windowWidth = 0;
        windowHeight = 0;
        takasa = 0;
        yoko = 0;
        counthojo = 0;

        date = "";

        flg = false;
        flg2 = true;

        speedlist = new ArrayList<Integer>();
        date1 = new ArrayList<String>();

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setStrokeJoin(Paint.Join.ROUND);  //線の端の丸め
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setTextSize(50);

        path = new Path();

        list = new ArrayList<Line>();
        line = new Line();


        paint2 = new Paint();
        paint2.setColor(Color.YELLOW);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(3);
        paint2.setStrokeJoin(Paint.Join.ROUND);  //線の端の丸め
        paint2.setStrokeCap(Paint.Cap.ROUND);

        path2 = new Path();
//        list5 = new ArrayList<Line>();
//        line5 = new Line();

        paint3 = new Paint();
        paint3.setColor(Color.rgb(150, 150, 150));
//        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeWidth(3);
//        paint3.setStrokeJoin(Paint.Join.ROUND);  //線の端の丸め
//        paint3.setStrokeCap(Paint.Cap.ROUND);
        paint3.setTextSize(40);

        paint4 = new Paint();
        paint4.setColor(Color.rgb(150, 150, 150));
        paint4.setStyle(Paint.Style.STROKE);
        paint4.setStrokeWidth(3);
        paint4.setStrokeJoin(Paint.Join.ROUND);  //線の端の丸め
        paint4.setStrokeCap(Paint.Cap.ROUND);
        paint4.setTextSize(500);

        path3 = new Path();

        path4 = new Path();

        list2 = new ArrayList<Line>();
        line2 = new Line();

        list3 = new ArrayList<Line>();
        line3 = new Line();
        path5 = new Path();

        paint5 = new Paint();
        paint5.setColor(Color.rgb(150, 150, 150));
        paint5.setStyle(Paint.Style.STROKE);
        paint5.setStrokeWidth(3);
        paint5.setStrokeJoin(Paint.Join.ROUND);  //線の端の丸め
        paint5.setStrokeCap(Paint.Cap.ROUND);
        paint5.setTextSize(40);

        list4 = new ArrayList<Line>();
        line4 = new Line();
        path6 = new Path();

    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
//        for(int i = 0; i < list.size(); i++){
//            canvas.drawPath(list.get(i).getPath(), list.get(i).getPaint());
//        }
        canvas.drawPath(path,paint);

//        takasa = (float) (windowHeight * 0.6);
//        takasa1memori = (takasa - 50) / 15 / 10;

//        canvas.drawPath(path5, paint5);

        for(int i = 0; i < list3.size(); i++) {
            canvas.drawPath(list3.get(i).getPath(), list3.get(i).getPaint());
        }

        for(int i = 0; i < list4.size(); i++){
            canvas.drawTextOnPath(list4.get(i).getStr(), list4.get(i).getPath(), 0, 0, list4.get(i).getPaint());
        }

        canvas.drawPath(path2, paint2);
//        for(int i = 0; i < list5.size(); i++){
//            canvas.drawPath(list5.get(i).getPath(), list5.get(i).getPaint());
//        }
        path2.reset();

//        for(int i = 0; i < list2.size(); i++){
//            canvas.drawPath(list2.get(i).getPath(), list2.get(i).getPaint());
//        }
        canvas.drawPath(path3,paint4);
        for(int i = 0; i < list2.size(); i++){
            canvas.drawTextOnPath(list2.get(i).getStr(), list2.get(i).getPath(), 0, 0, list2.get(i).getPaint());
        }
//        canvas.drawTextOnPath(date,path4,0,0,paint3);
        path3.reset();
    }

    public void setPath1(){
        takasa = (float) (windowHeight * 0.6);

        path.moveTo(100, 50);
        path.lineTo(100, takasa);  //座標0
        path.lineTo(windowWidth - 50, takasa);    //X軸MAX

//        path5.moveTo(100, 150);
//        path5.lineTo(windowWidth - 50, 150);

//        line.setPath(path);
//        line.setPaint(paint);
//        list.add(line);
//
//        line = new Line();
//        path = new Path();



        takasa1memori = (takasa - 50) / 15 / 10;

        for (int i = 150; i >= 0; i -= 10) {
            String str;
            str = String.valueOf(i);
            if(i == 150) {
                paint5.setStrokeWidth(5);
                paint5.setColor(Color.WHITE);
                paint3.setStrokeWidth(5);
                paint3.setColor(Color.WHITE);

//                canvas.drawText(str, 10, 50, paint3);
                path6.moveTo(10, 55);
                path6.lineTo(80, 55);
                path5.moveTo(80, 50);
                path5.lineTo(windowWidth - 50, 50);

                line3.setPath(path5);
                line3.setPaint(paint5);
                line3.setStr(str);
                list3.add(line3);
                line4.setPath(path6);
                line4.setPaint(paint3);
                line4.setStr(str);
                list4.add(line4);

//                paint5.setStrokeWidth(3);
//                paint5.setColor(Color.rgb(150, 150, 150));

                path5 = new Path();
                line3 = new Line();
                paint5 = new Paint();
                paint5.setColor(Color.rgb(150, 150, 150));
                paint5.setStyle(Paint.Style.STROKE);
                paint5.setStrokeWidth(3);
                paint5.setStrokeJoin(Paint.Join.ROUND);  //線の端の丸め
                paint5.setStrokeCap(Paint.Cap.ROUND);
                paint5.setTextSize(40);

                path6 = new Path();
                line4 = new Line();
                paint3 = new Paint();
                paint3.setColor(Color.rgb(150, 150, 150));
                paint3.setStrokeWidth(3);
                paint3.setTextSize(40);
            }
            else if(i != 0){
                if(i % 50 == 0){
                    paint5.setStrokeWidth(5);
                    paint5.setColor(Color.WHITE);
                    paint3.setStrokeWidth(5);
                    paint3.setColor(Color.WHITE);
                }

//                canvas.drawText(str, 10, takasa - takasa1memori * i, paint3);
//                canvas.drawLine(80, takasa - takasa1memori * i, windowWidth - 50, takasa - takasa1memori * i, paint3);

                path6.moveTo(10, takasa - takasa1memori * i + 5);
                path6.lineTo(80, takasa - takasa1memori * i + 5);
                path5.moveTo(80, takasa - takasa1memori * i);
                path5.lineTo(windowWidth - 50, takasa - takasa1memori * i);

                line3.setPath(path5);
                line3.setPaint(paint5);
                line3.setStr(str);
                list3.add(line3);
                line4.setPath(path6);
                line4.setPaint(paint3);
                line4.setStr(str);
                list4.add(line4);

                path5 = new Path();
                line3 = new Line();
                paint5 = new Paint();
                paint5.setColor(Color.rgb(150, 150, 150));
                paint5.setStyle(Paint.Style.STROKE);
                paint5.setStrokeWidth(3);
                paint5.setStrokeJoin(Paint.Join.ROUND);  //線の端の丸め
                paint5.setStrokeCap(Paint.Cap.ROUND);
                paint5.setTextSize(40);

                path6 = new Path();
                line4 = new Line();
                paint3 = new Paint();
                paint3.setColor(Color.rgb(150, 150, 150));
                paint3.setStrokeWidth(3);
                paint3.setTextSize(40);
            }
            else {
//                canvas.drawText(str, 10, takasa, paint3);

                path6.moveTo(10, takasa + 5);
                path6.lineTo(80, takasa + 5);
//                path5.moveTo(80, takasa);
//                path5.lineTo(windowWidth - 50, takasa);
//
//                line3.setPath(path5);
//                line3.setPaint(paint5);
//                line3.setStr(str);
//                list3.add(line3);
                line4.setPath(path6);
                line4.setPaint(paint3);
                line4.setStr(str);
                list4.add(line4);
//
//                path5 = new Path();
//                line3 = new Line();
//                paint5 = new Paint();
//                paint5.setColor(Color.rgb(150, 150, 150));
//                paint5.setStyle(Paint.Style.STROKE);
//                paint5.setStrokeWidth(3);
//                paint5.setStrokeJoin(Paint.Join.ROUND);  //線の端の丸め
//                paint5.setStrokeCap(Paint.Cap.ROUND);
//                paint5.setTextSize(40);
//
                path6 = new Path();
                line4 = new Line();
                paint3 = new Paint();
                paint3.setColor(Color.rgb(150, 150, 150));
                paint3.setStrokeWidth(3);
                paint3.setTextSize(40);
            }
        }

        flg = true;

        invalidate();
    }
    public void setPath2(){

        takasa = (float) (windowHeight * 0.6);
        takasa1memori = (takasa - 50) / 15 / 10;

        if(speedlist.size() < 11) {
            yoko = (windowWidth - 150) / 10;
            yoko1memori = yoko;
        }
        else {
            yoko = (windowWidth - 150) / (speedlist.size() - 1);
            yoko1memori = yoko;
        }

        for (int i = 0; i < speedlist.size(); i++) {

            if (i == 0) {
                path2.moveTo(100f, takasa - (speedlist.get(i) * takasa1memori));
//                line5.setPath(path2);
//                line5.setPaint(paint2);
//                list5.add(line5);
//                path3.moveTo(yoko + 100f, 50);
//                path3.lineTo(yoko + 100f, takasa);

            } else {
                path2.lineTo(yoko + 100f, takasa - (speedlist.get(i) * takasa1memori));
//                line5.setPath(path2);
//                line5.setPaint(paint2);
//                list5.add(line5);
            }
            if(i != 0){
                yoko += yoko1memori;
            }
        }


        takasa = (float) (windowHeight * 0.6);
        takasa1memori = (takasa - 50) / 15 / 10;

        if(speedlist.size() < 11) {
            yoko = (windowWidth - 150) / 10;
            yoko1memori = yoko;
        }
        else {
            yoko = (windowWidth - 150) / (speedlist.size() - 1);
            yoko1memori = yoko;
        }
        for(int i = 1; i < speedlist.size(); i++){
            if (i == 1) {
                path3.reset();
                path4.reset();
                list2 = new ArrayList<Line>();
//                        flg2 = true;
            }
            if (i % 2 == 0 && i != 1) {
                if (i / 10 < 1) {
                    path3.moveTo(yoko + 100f, 50);
                    path3.lineTo(yoko + 100f, takasa);
                    path4.moveTo(yoko + 20f, takasa + 35);
                    path4.lineTo(yoko + 180f, takasa + 35);
                    line2.setPath(path4);
                    line2.setPaint(paint3);
//                    if(flg2){
//                        date = getNowDate().toString();
//                        flg2 = false;
//                    }
                    line2.setStr(date1.get(i));
                    list2.add(line2);
                    path4 = new Path();
                    line2 = new Line();
                } else if (i % 10 == 0 && i / 30 < 1) {
                    if (i == 10) {
                        path3.reset();
                        path4.reset();
                        list2 = new ArrayList<Line>();
//                        flg2 = true;
                    }
                    path3.moveTo(yoko + 100f, 50);
                    path3.lineTo(yoko + 100f, takasa);
                    path4.moveTo(yoko + 20f, takasa + 35);
                    path4.lineTo(yoko + 180f, takasa + 35);
                    line2.setPath(path4);
                    line2.setPaint(paint3);
//                    date = date1.get(i);
//                    if(flg2){
//                        date = getNowDate();
//                        flg2 = false;
//                    }
                    line2.setStr(date1.get(i));
                    list2.add(line2);
                    path4 = new Path();
                    line2 = new Line();
                } else if (i % 30 == 0 && i / 60 < 1) {
                    if (i == 30) {
                        path3.reset();
                        path4.reset();
                        list2 = new ArrayList<Line>();
//                        flg2 = true;
                    }
                    path3.moveTo(yoko + 100f, 50);
                    path3.lineTo(yoko + 100f, takasa);
                    path4.moveTo(yoko + 20f, takasa + 35);
                    path4.lineTo(yoko + 180f, takasa + 35);
                    line2.setPath(path4);
                    line2.setPaint(paint3);
//                    date = date1.get(i);
//                    if(flg2){
//                        date = getNowDate();
//                        flg2 = false;
//                    }
                    line2.setStr(date1.get(i));
                    list2.add(line2);
                    path4 = new Path();
                    line2 = new Line();
                } else if (i % 60 == 0 && i / 300 < 1) {
                    if (i == 60) {
                        path3.reset();
                        path4.reset();
                        list2 = new ArrayList<Line>();
//                        flg2 = true;
                    }
                    path3.moveTo(yoko + 100f, 50);
                    path3.lineTo(yoko + 100f, takasa);
                    path4.moveTo(yoko + 20f, takasa + 35);
                    path4.lineTo(yoko + 180f, takasa + 35);
                    line2.setPath(path4);
                    line2.setPaint(paint3);
//                    date = date1.get(i);
//                    if(flg2){
//                        date = getNowDate();
//                        flg2 = false;
//                    }
                    line2.setStr(date1.get(i));
                    list2.add(line2);
                    path4 = new Path();
                    line2 = new Line();
                } else if (i % 300 == 0) {
                    if (i == 300) {
                        path3.reset();
                        path4.reset();
                        list2 = new ArrayList<Line>();
//                        flg2 = true;
                    }
                    path3.moveTo(yoko + 100f, 50);
                    path3.lineTo(yoko + 100f, takasa);
                    path4.moveTo(yoko + 20f, takasa + 35);
                    path4.lineTo(yoko + 180f, takasa + 35);
                    line2.setPath(path4);
                    line2.setPaint(paint3);
//                    date = date1.get(i);
//                    if(flg2){
//                        date = getNowDate();
//                        flg2 = false;
//                    }
                    line2.setStr(date1.get(i));
                    list2.add(line2);
                    path4 = new Path();
                    line2 = new Line();
                }

            }

            if(i != 0){
                yoko += yoko1memori;
            }
            invalidate();

        }

//        invalidate();

    }

    public void sizeSet(int width, int height) {

        windowWidth = width;
        windowHeight = height;
    }

    public void dataset(int data, String date){
        speedlist.add(data);
        date1.add(date);
        setPath2();
    }

    public ArrayList<Integer> getSpeedlist(){
        return speedlist;
    }

    public ArrayList<String> getDate1(){
        return date1;
    }

    public void setSpeedlist(ArrayList<Integer> sp){
        speedlist = sp;
    }

    public void setDate1(ArrayList<String> setdate){
        date1 = setdate;
        setPath2();
    }

    public void dataclear(){
        speedlist.clear();
        date1.clear();
        path2.reset();
//        line5 = new Line();
//        list5 = new ArrayList<Line>();
        path3.reset();
        path4.reset();
        line2 = new Line();
        list2 = new ArrayList<Line>();
    }
    public void dataclear2(){
        path.reset();
        path5.reset();
        path6.reset();
        line3 = new Line();
        list3 = new ArrayList<Line>();
        line4 = new Line();
        list4 = new ArrayList<Line>();
    }

    public String getDate() {
        return date;
    }

}
