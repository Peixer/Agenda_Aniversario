package br.glaicon.agenda_aniversarios.Volley;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtil {

    private static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(image, 0, 0, width, height, matrix, false);
    }

    private static final int SIZE_HEIGHT = 300;
    private static final int SIZE_WIDTH = 300;

    public static Bitmap ResizedBitmap(Bitmap image) {

        if (image.getHeight() > SIZE_HEIGHT && image.getWidth() > SIZE_WIDTH)
            image = ImageUtil.getResizedBitmap(image, SIZE_HEIGHT, SIZE_WIDTH);

        return image;
    }
}
