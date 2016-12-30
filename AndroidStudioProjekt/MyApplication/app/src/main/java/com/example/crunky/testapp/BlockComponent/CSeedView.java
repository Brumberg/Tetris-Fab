package com.example.crunky.testapp.BlockComponent;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.content.Context;
import android.widget.ImageView;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.example.crunky.testapp.R;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * Created by oli on 27.12.2016.
 */

public class CSeedView extends ImageView {
    private Rect rectangle;
    private Paint paint;
    private Bitmap m_Seedbox;

    public CSeedView(Context context) {
        super(context);
    }

    private static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Bitmap bitmap;
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.vi_seed_box_5x4);
        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public CSeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_Seedbox = getBitmapFromVectorDrawable(context, R.drawable.vi_seed_box_5x4);

        int x = 50;
        int y = 50;
        int sideLength = 200;

        // create a rectangle that we'll draw later
        rectangle = new Rect(x, y, sideLength, sideLength);

        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(Color.GRAY);
    }

    public CSeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onDraw(Canvas canvas) {
        /*super.onDraw(canvas);
        Drawable d = getResources().getDrawable(R.drawable.vi_seed_box_5x4);
        int left = 0;
        int top = 0;
        int right =150;
        int bottom = 150;
        canvas.drawColor(Color.BLUE);
        canvas.drawRect(rectangle, paint);
*/
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        System.out.println("Painting content");
        Paint paint  = new Paint(Paint.LINEAR_TEXT_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize(12.0F);
        System.out.println("Drawing text");
        canvas.drawText("Hello World in custom view", 100, 100, paint);

    }
}
