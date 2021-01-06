package com.example.test5;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

public class MyButton extends androidx.appcompat.widget.AppCompatButton {

    private Context context;

    private boolean btn_Frg;

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.btn_Frg = true;
        this.setTextColor(Color.rgb(150, 150, 150));

        setBackground(getResources().getDrawable(R.drawable.pet1));

    }

    public boolean getFrg(){
        return this.btn_Frg;
    }

    public void setname(String str){
        this.setText(str);
    }

    public void setground1(){
        this.setBackground(getResources().getDrawable(R.drawable.pet2));
    }

    public void setground2(){
        this.setBackground(getResources().getDrawable(R.drawable.pet1));
    }

}