package com.example.coursework.visuals;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class FaceBitmap {

    public Bitmap getBitmap(int wigth, int heigth, Mood mood) {

        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(wigth, heigth, conf); // this creates a MUTABLE bitmap
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();

        {
            int radius = 300;
            int center_x = 725; //165
            int center_y = 1000; //390
            int eye_radius = 80;
            int pupil_radius = eye_radius / 2;
            int mouth_radius = 80;

            if (mood == Mood.sad) {
                canvas.drawARGB(16, 0, 0, 0);
            } else {
                canvas.drawARGB(160, 217, 19, 60);
            }

            //Head
            if (mood == Mood.sad) {
                paint.setARGB(255, 203, 229, 153);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(center_x, center_y, radius, paint);
            } else {
                paint.setARGB(255, 255, 219, 172);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(center_x, center_y, radius, paint);
            }

            //Left eye
            RectF oval = new RectF();
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            oval.set(center_x - 60 - eye_radius * 2,
                    center_y - eye_radius,
                    center_x - 60,
                    center_y + eye_radius);
            canvas.drawArc(oval, 0, 360, true, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawArc(oval, 0, 360, true, paint);

            // Left pupil
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            oval.set(center_x - 60 - eye_radius - pupil_radius,
                    center_y - eye_radius + pupil_radius,
                    center_x - 60 - pupil_radius,
                    center_y + eye_radius - pupil_radius);
            canvas.drawArc(oval, 0, 360, true, paint);

            //Right eye
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            oval.set(center_x + 60,
                    center_y - eye_radius,
                    center_x + 60 + eye_radius * 2,
                    center_y + eye_radius);
            canvas.drawArc(oval, 0, 360, true, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawArc(oval, 0, 360, true, paint);

            // Right pupil
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            oval.set(center_x + 60 + pupil_radius,
                    center_y - eye_radius + pupil_radius,
                    center_x + 60 + eye_radius + pupil_radius,
                    center_y + eye_radius - pupil_radius);
            canvas.drawArc(oval, 0, 360, true, paint);

            //Mouth
            if (mood == Mood.sad) {
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                oval.set(center_x - mouth_radius,
                        center_y + radius - mouth_radius,
                        center_x + mouth_radius,
                        center_y + radius + mouth_radius);
                canvas.drawArc(oval, 210, 120, false, paint);
            } else {
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                oval.set(center_x - mouth_radius,
                        center_y + radius - 2 * mouth_radius,
                        center_x + mouth_radius,
                        center_y + radius - mouth_radius);
                canvas.drawArc(oval, 65, 50, true, paint);
            }

            paint.setTextSize(150);
            int step = 200;
            int space = 350;
            if (mood == Mood.sad) {
                String[] strings = new String[]{
                        "Кажется, ",
                        "Вы больны!"
                };

                int len = step * strings.length;

                for (int i = 0; i < strings.length; i++) {
                    int inner_space = ((heigth - len - radius - center_y) / 2);

                    canvas.drawText(strings[i], space,
                            (i * step + center_y + radius + inner_space),
                            paint);
                }
            } else {
                String[] strings = new String[]{
                        "С вас $100"
                };
                space = 350;
                int len = step * strings.length;

                for (int i = 0; i < strings.length; i++) {
                    int inner_space = ((heigth - len - radius - center_y) / 2);

                    canvas.drawText(strings[i], space,
                            (i * step + center_y + radius + inner_space),
                            paint);
                }
            }
        }

        return bmp;
    }


    private boolean IsInTheBeard(int center_x, int center_y, int radius, int y, int x, int oval_radius) {

        if (Math.pow((x - center_x), 2) / Math.pow(radius, 2)
                + Math.pow((y - center_y), 2) / Math.pow(oval_radius, 2) >= 1) {
            if (Math.pow((x - center_x), 2) + Math.pow((y - center_y), 2) <= Math.pow(radius, 2)) {
                return true;
            }
        }
        return false;
    }
}
