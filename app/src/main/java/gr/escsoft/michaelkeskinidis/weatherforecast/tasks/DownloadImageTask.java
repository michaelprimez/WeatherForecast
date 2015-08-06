package gr.escsoft.michaelkeskinidis.weatherforecast.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by Michael on 8/5/2015.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private static final String IMG_URL = "http://openweathermap.org/img/w/";
    WeakReference<ImageView> bmImage;

    public DownloadImageTask(ImageView bmImage) {
        if (bmImage != null) {
            this.bmImage = new WeakReference<ImageView>(bmImage);
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String urldisplay = IMG_URL + params[0];
        Bitmap mIcon = null;
        InputStream in = null;
        try {
            in = new java.net.URL(urldisplay).openStream();
            mIcon = BitmapFactory.decodeStream(in);
//            mIcon = getResizedBitmap(mIcon, 128, 128);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mIcon;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (bmImage != null) {
            bmImage.get().setImageBitmap(result);
        }
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }
}