package com.bogosla.binsta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyView extends View {
    private Paint paint;
    private Path path = new Path();
    private List<String> colors =  Arrays.asList("#FFD700", "#0D6EFD", "#DC3545", "#332729", "#198754");
    private List<Point> points = new ArrayList<>();
    float x = 0, y = 0;

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8f);
        paint.setColor(Color.parseColor(colors.get(0)));

    }

    private int getColor() {
        int i = (int)(Math.random()*colors.size());
        return Color.parseColor(colors.get(i));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Point p: points) {
            paint.setColor(p.color);
            canvas.drawCircle(p.mx, p.my, 48, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        Point p = new Point();
        p.mx = x;
        p.my = y;
        p.color = getColor();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            points.add(p);
            postInvalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    private class Point {
        float mx, my;
        int color;
    }
}
