package com.authentication.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ADMIN on 21-Jun-17.
 */

public class ImageUtils {

    public static File saveImage(String path,String fileName,byte[] data) {
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdir();
        }

        File file = new File(path + File.separator + fileName + ".bmp");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file;

    }

    public static Bitmap CreateBitmap(int imgWidth, int imgHeight, byte[] imgBytes) {
        int[] pixels = new int[imgWidth * imgHeight];
        for (int i = 0; i < imgWidth * imgHeight; i++) {
            pixels[i] = imgBytes[i];
        }

        Bitmap emptyBmp = Bitmap.createBitmap(pixels, imgWidth, imgHeight, Bitmap.Config.RGB_565);

        int width, height;
        height = emptyBmp.getHeight();
        width = emptyBmp.getWidth();

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(result);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(emptyBmp, 0, 0, paint);

        return result;
    }
}
