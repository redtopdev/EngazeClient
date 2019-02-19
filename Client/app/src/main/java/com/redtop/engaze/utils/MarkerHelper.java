package com.redtop.engaze.utils;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.redtop.engaze.BaseActivity;
import com.redtop.engaze.R;
import com.redtop.engaze.entity.UsersLocationDetail;

public class MarkerHelper {

	private final static String TAG = AppUtility.class.getName();
	
	public static Marker drawDestinationMarker(LatLng latLng , GoogleMap map, final Context context){
		// Define the size you want from dimensions file
		int shapeSize = context.getResources().getDimensionPixelSize(R.dimen.map_dot_marker_size);
		Drawable shapeDrawable = null;
		try{
			int markerSize = (int) context.getResources().getDimension(R.dimen.marker_size);		
			Bitmap bitmap = Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(context.getContentResolver(),Uri.parse("android.resource://com.redtop.engaze/drawable/ic_destination")),markerSize,markerSize, true).copy(Bitmap.Config.ARGB_8888, true);
			shapeDrawable= new BitmapDrawable(context.getResources(), bitmap );
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return map.addMarker(generateMarker(latLng, shapeDrawable, context, shapeSize, shapeSize, .37f, .67f,""));
	}
	
	
	public static Marker drawParticipantMarker(LatLng latLng, UsersLocationDetail userLocationDetail, GoogleMap map, Context context){
		latLng = new LatLng(latLng.latitude, latLng.longitude);
		// Define the size you want from dimensions file
		int shapeSize = context.getResources().getDimensionPixelSize(R.dimen.map_dot_marker_size);
		Drawable shapeDrawable = null;
		Bitmap bitmap1;
		Bitmap b = null;

		try {

			int circleSize = (int) context.getResources().getDimension(R.dimen.marker_circle_size);
			int markerSize = (int) context.getResources().getDimension(R.dimen.marker_size);

			bitmap1 = AppUtility.getCroppedBitmap( Bitmap.createScaledBitmap(userLocationDetail.getContactOrGroup().getImageBitmap(context), circleSize, circleSize, true)) ;

			Bitmap bitmap2 = Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(context.getContentResolver(),Uri.parse("android.resource://com.redtop.engaze/drawable/marker")),markerSize,markerSize, true).copy(Bitmap.Config.ARGB_8888, true);
			Paint paint = new Paint();
			//ColorFilter filter = new PorterDuffColorFilter(context.getResources().getColor(R.color.accent), android.graphics.PorterDuff.Mode.SRC_ATOP );

			//paint.setColorFilter(filter);

			Canvas canvas = new Canvas(bitmap2);
			canvas.drawBitmap(bitmap2, 0, 0, paint);

			b = AppUtility.overlayBitmapToCenter(bitmap2, bitmap1);
			shapeDrawable= new BitmapDrawable(context.getResources(), b );

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		

		return map.addMarker(generateMarker(latLng, shapeDrawable, context, shapeSize, shapeSize, .5f, .8f, userLocationDetail.getUserName()));
	}
	
	
	public static Marker drawTimeDistanceMarker(LatLng latLng, UsersLocationDetail ud, GoogleMap map, Context context){
		latLng = new LatLng(latLng.latitude, latLng.longitude);
		View etaMarker = ((BaseActivity)context).getLayoutInflater().inflate(R.layout.custom_snippet_eta, null);

		DisplayMetrics dm = new DisplayMetrics();
		((BaseActivity)context).getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		etaMarker.setLayoutParams(params);				
		TextView info= (TextView) etaMarker.findViewById(R.id.txt_info);
		info.setText(ud.getDistance() + ", " + ud.getEta().replace("hour", "hr"));	
		IconGenerator iconGen = new IconGenerator(context);
		iconGen.setColor(context.getResources().getColor(R.color.primary));
		iconGen.setContentView(etaMarker);
		// Create the bitmap
		Bitmap bitmap = iconGen.makeIcon();

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(latLng)
		.anchor(.5f, 2.0f)
		//.title(userLocationDetail.getUserName())
		.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
		return map.addMarker(markerOptions);

		}
	
	public static Marker drawPinMarker(GoogleMap map, LatLng latLng, Context context){
		try {

			// Define the size you want from dimensions file
			int shapeSize = context.getResources().getDimensionPixelSize(R.dimen.map_dot_marker_size);
			Bitmap bitmap1;
			Bitmap b = null;

			//Bitmap centerIcon = BitmapFactory.decodeResource(mContext.getResources(),markerCenterImageResId);
			int circleSize = (int) context.getResources().getDimension(R.dimen.pin_circle_size);
			int markerSize = (int) context.getResources().getDimension(R.dimen.pin_size);
			Bitmap centerIcon = AppUtility.generateCircleBitmapForText(context, context.getResources().getColor(R.color.primary), 40, "");								
			bitmap1 = AppUtility.getCroppedBitmap( Bitmap.createScaledBitmap(centerIcon, circleSize, circleSize, true)) ;
			Bitmap bitmap2 = Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(context.getContentResolver(),Uri.parse("android.resource://com.redtop.engaze/drawable/ic_pin")),markerSize,markerSize, true).copy(Bitmap.Config.ARGB_8888, true);
			Paint paint = new Paint();
			ColorFilter filter = new PorterDuffColorFilter(context.getResources().getColor(R.color.icon), android.graphics.PorterDuff.Mode.SRC_ATOP );

			paint.setColorFilter(filter);

			Canvas canvas = new Canvas(bitmap2);
			canvas.drawBitmap(bitmap2, 0, 0, paint);

			b = AppUtility.overlayBitmapToCenterOfPin(bitmap2, bitmap1);
			Drawable shapeDrawable= new BitmapDrawable(context.getResources(), b );
			
			return map.addMarker(generateMarker(latLng, shapeDrawable, context, shapeSize, shapeSize, .5f, 1.1f, null));


		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
	
	public static Drawable CreateLocatonPinDrawable(Context context){
		Drawable shapeDrawable = null;
		try {
			Bitmap bitmap1;
			Bitmap b = null;
			//Bitmap centerIcon = BitmapFactory.decodeResource(mContext.getResources(),markerCenterImageResId);
			int circleSize = (int) context.getResources().getDimension(R.dimen.pin_circle_size);
			int markerSize = (int) context.getResources().getDimension(R.dimen.pin_size);
			Bitmap centerIcon = AppUtility.generateCircleBitmapForText(context, context.getResources().getColor(R.color.primary), 40, "");								
			bitmap1 = AppUtility.getCroppedBitmap( Bitmap.createScaledBitmap(centerIcon, circleSize, circleSize, true)) ;
			Bitmap bitmap2 = Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(context.getContentResolver(),Uri.parse("android.resource://com.redtop.engaze/drawable/ic_pin")),markerSize,markerSize, true).copy(Bitmap.Config.ARGB_8888, true);
			Paint paint = new Paint();
			ColorFilter filter = new PorterDuffColorFilter(context.getResources().getColor(R.color.icon), android.graphics.PorterDuff.Mode.SRC_ATOP );

			paint.setColorFilter(filter);

			Canvas canvas = new Canvas(bitmap2);
			canvas.drawBitmap(bitmap2, 0, 0, paint);

			b = AppUtility.overlayBitmapToCenterOfPin(bitmap2, bitmap1);
			shapeDrawable= new BitmapDrawable(context.getResources(), b );

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return shapeDrawable;
	}
	
	private static MarkerOptions generateMarker(LatLng latLng, Drawable shapeDrawable, Context context, int width, int height, float u, float v, String title){
		IconGenerator iconGen = new IconGenerator(context);
		iconGen.setBackground(shapeDrawable);

		// Create a view container to set the size
		View view = new View(context);
		view.setLayoutParams(new ViewGroup.LayoutParams(width, height));
		iconGen.setContentView(view);
		

		// Create the bitmap
		Bitmap bitmap = iconGen.makeIcon();

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(latLng)
		.anchor(u,v)		
		.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
		
		if(title!=null){
			markerOptions.title(title);
		}

		return markerOptions;
	}	
}
