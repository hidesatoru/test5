package com.example.test5;

import android.graphics.Paint;
import android.graphics.Path;

public class Line {
    private Path path;
    private Paint paint;
    private String str;

    Line() {
        path = new Path();
        paint = new Paint();
        str = "";
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}