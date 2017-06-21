package com.shzy.bjj.view;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.shzy.bjj.tools.Utils;

public class AsyncImageLoaderLocal {
	private int width=0;
	private int height=0;

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public AsyncImageLoaderLocal() {
	}

	public Bitmap loadDrawable(final String imageId, final String imageUrl,
			final ImageCallback imageCallback, final Context context,
			final ImageView imageview) {
		if (Utils.checkPhotoHasSDcard(imageId)) {
			Bitmap drawable = Utils.getPhotoFromSDcard(imageId);
			if (drawable != null) {
				if (width!=0||height!=0) {
					drawable=changeImageSize(drawable, width, height);
				}
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Bitmap) message.obj, imageUrl,
						imageview);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Bitmap bitmap = loadImageFromUrl(imageUrl, 1, context);
				if (bitmap == null) {
					return;
				}
				Utils.savePhotoToSDcard(imageId, bitmap);
				if (width!=0||height!=0) {
					bitmap=changeImageSize(bitmap, width, height);
				}
				Message message = handler.obtainMessage(0, bitmap);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	public Bitmap loadImageFromUrl(String url, int inSimpleSize, Context context) {
		Bitmap d = null;
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = m.openStream();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		// BitmapFactory Options
		options.inSampleSize = 1;
		// set not load content and only load Information
		options.inJustDecodeBounds = true;
		// set load content
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		try {
			d = BitmapFactory.decodeStream(i, null, options);
		} catch (Exception e) {
			e.printStackTrace();
			d = null;
		}
		return d;
	}

	public interface ImageCallback {
		public void imageLoaded(Bitmap imageDrawable, String imageUrl,
				ImageView imageView);
	}

	public int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	 /**
	 * to change the image size ,from drawable to bitmap
	 *
	 * @param d
	 * @param scaleX
	 * @param scaleY
	 * @return
	 */
	 public static Bitmap changeImageSize(Bitmap d, int width, int height) {
	 Bitmap bmp = null;
	 float scaleX = (float) width / (float) d.getWidth();
	 float scaleY = (float) height / (float) d.getHeight();
	 Matrix m = new Matrix();
	 if (scaleX>scaleY) {
		 m.postScale(scaleY, scaleY);
	}else{
		 m.postScale(scaleX, scaleX);
	}
	
	 bmp = Bitmap
	 .createBitmap(d, 0, 0, d.getWidth(), d.getHeight(), m, true);
	 return bmp;
	 }

}