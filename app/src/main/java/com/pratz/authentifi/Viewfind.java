package com.pratz.authentifi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Array;

public class Viewfind extends AppCompatActivity {

	SurfaceView viewfind;
	ImageButton closeButton, flashButton;
	CameraManager cameraManager;

	@Override
	protected void onResume() {
		super.onResume();
		overridePendingTransition(R.anim.slide_in_down, R.anim.stay_in_position);
	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(0, R.anim.slide_out_down);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);


		setContentView(R.layout.activity_viewfind);

		viewfind = (SurfaceView) findViewById(R.id.viewfinder);
		closeButton = (ImageButton) findViewById(R.id.close);

		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		createCameraSource();

	}

	private void createCameraSource() {

		final BarcodeDetector barcodeDetector = new BarcodeDetector
						.Builder(getApplicationContext())
					//	.setBarcodeFormats()
						.build();
		final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
				.setAutoFocusEnabled(true)
				.setRequestedFps(15.0f)
				.setRequestedPreviewSize(1024, 1024)
				.build();

		viewfind.getHolder().addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				if (ActivityCompat.checkSelfPermission(Viewfind.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
					// TODO: Consider calling
					//    ActivityCompat#requestPermissions
					// here to request the missing permissions, and then overriding
					//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
					//                                          int[] grantResults)
					// to handle the case where the user grants the permission. See the documentation
					// for ActivityCompat#requestPermissions for more details.
					return;
				}
				try {
					cameraSource.start(viewfind.getHolder());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				cameraSource.stop();
			}
		});

		barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
			@Override
			public void release() {

			}

			@Override
			public void receiveDetections(Detector.Detections<Barcode> detections) {

				final SparseArray<Barcode> barcodes = detections.getDetectedItems();
				String code = barcodes.valueAt(0).displayValue;

				Log.d("oolala1", code);
				Intent intent = new Intent(Viewfind.this, ProductPage.class);
				Bundle bundle = new Bundle();
				bundle.putString("code", code);

				if(false)
					bundle.putBoolean("isOwner", true);
				else
					bundle.putBoolean("isOwner", false);

				intent.putExtras(bundle);
				startActivity(intent);
				barcodeDetector.release();
				finish();
			}
		});
	}
}
