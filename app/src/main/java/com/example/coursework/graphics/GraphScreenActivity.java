package com.example.coursework.graphics;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.coursework.R;
import com.example.coursework.activities.LoginActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class GraphScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_screen);

        setBitmapAsBackground(Mood.sad);

        View view = findViewById(R.id.erase);
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ((Eraser) view).setParent(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(GraphScreenActivity.this, LoginActivity.class));
                finish();
            }
        }, 1 * 1000);
    }

    void updateThePicture() {
        setBitmapAsBackground(Mood.happy);

/*        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(GraphScreenActivity.this, LoginActivity.class));
                finish();
            }
        }, 1000);*/
    }

    private void setBitmapAsBackground(Mood mood) {
        ConstraintLayout layout = findViewById(R.id.erase_background);
        FaceBitmap faceBitmap = new FaceBitmap();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Bitmap bitmap = faceBitmap.getBitmap(width, height - 170, mood);
        SaveBitmap(bitmap);

        ImageView imageView = findViewById(R.id.erase_imageview);
        imageView.setImageBitmap(bitmap);
    }

    private void SaveBitmap(Bitmap bitmap) {
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            OutputStream fOut = null;
            File file = new File(path, "bitmap.jpg");
            fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception ex) {
        }
    }
}
