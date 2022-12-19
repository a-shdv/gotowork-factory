package com.example.coursework.visuals;

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
import com.example.coursework.activities.EnterActivity;
import com.example.coursework.activities.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FaceQuestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_quest);

        setBitmapAsBackground(Mood.sad);

        View view = findViewById(R.id.erase);
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ((EraseView) view).setParent(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(FaceQuestActivity.this, EnterActivity.class));
                finish();
            }
        }, 15 * 1000);
    }

    void updateThePicture() {
        setBitmapAsBackground(Mood.happy);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(FaceQuestActivity.this, EnterActivity.class));
                finish();
            }
        }, 15000);
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
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
        }
        catch (Exception ex) {
        }
    }

    private Bitmap LoadBitmap() {
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            File file = new File(path, "bitmap.jpg");
            if (file.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                return myBitmap;
            }
        }
        catch (Exception ex) {

        }
        return null;
    }
}
