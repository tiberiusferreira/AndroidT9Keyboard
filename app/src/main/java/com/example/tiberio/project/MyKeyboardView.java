package com.example.tiberio.project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by tiberio on 15/11/2015.
 */
public class MyKeyboardView extends KeyboardView {

    Context context;
    Paint paint = new Paint();
    public MyKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context ;

    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        paint.setColor(Color.RED);

        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for(Keyboard.Key key: keys) {
            if(0==key.label.toString().compareTo(".,?")){
                canvas.drawText("1", key.x + (float) 0.8*(key.width), key.y + (float) 0.4*(key.height), paint);
            }else
            if(0==key.label.toString().compareTo("ABC")){
                canvas.drawText("2", key.x + (float) 0.8*(key.width), key.y + (float) 0.4*(key.height), paint);
            }else
            if(0==key.label.toString().compareTo("DEF")){
                canvas.drawText("3", key.x + (float) 0.8*(key.width), key.y + (float) 0.4*(key.height), paint);
            }else
            if(0==key.label.toString().compareTo("GHI")){
                canvas.drawText("4", key.x + (float) 0.8*(key.width), key.y + (float) 0.4*(key.height), paint);
            }else
            if(0==key.label.toString().compareTo("JKL")){
                canvas.drawText("5", key.x + (float) 0.8*(key.width), key.y + (float) 0.4*(key.height), paint);
            }else
            if(0==key.label.toString().compareTo("MNO")){
                canvas.drawText("6", key.x + (float) 0.8*(key.width), key.y + (float) 0.4*(key.height), paint);
            }else
            if(0==key.label.toString().compareTo("PQRS")){
                canvas.drawText("7", key.x + (float) 0.8*(key.width), key.y + (float) 0.35*(key.height), paint);
            }else
            if(0==key.label.toString().compareTo("TUV")){
                canvas.drawText("8", key.x + (float) 0.8*(key.width), key.y + (float) 0.4*(key.height), paint);
            }else
            if(0==key.label.toString().compareTo("WXYZ")){
                canvas.drawText("9", key.x + (float) 0.8*(key.width), key.y + (float) 0.35*(key.height), paint);
                }

        }
    }
}
