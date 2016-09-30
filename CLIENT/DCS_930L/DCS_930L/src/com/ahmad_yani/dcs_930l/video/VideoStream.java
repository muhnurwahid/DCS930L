package com.ahmad_yani.dcs_930l.video;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

@SuppressLint("SimpleDateFormat") public class VideoStream extends View {

	Bitmap bitmap;
	String camName;
	Date date;
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat formatter;

	public VideoStream(Context context) {
		super(context);
		formatter = new SimpleDateFormat("d/M/y h:mm:ss a");
		formatter.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		if (bitmap != null) {
			date = new Date();
			cal.setTimeInMillis(date.getTime());
			Rect dest = new Rect(0, 0, getWidth(), getHeight());
			Paint paint = new Paint();
			paint.setFilterBitmap(true);
			paint.setColor(Color.WHITE);
			paint.setStrokeWidth(12);
			canvas.drawBitmap(bitmap, null, dest, paint);
			canvas.drawText(camName, 10, 20, paint);
			canvas.drawText(formatter.format(cal.getTime()), 10, 40, paint);
		}
		invalidate();
	}

	public void update(Bitmap bitmap, String camName) {
		this.bitmap = bitmap;
		this.camName = camName;
	}
}
