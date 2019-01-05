package com.pratz.authentifi.SellActivity;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.pratz.authentifi.ConnectionManager;
import com.pratz.authentifi.MainActivity;
import com.pratz.authentifi.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyStep2 extends Fragment {

	SurfaceView viewfind;
	TextView textView;

	public BuyStep2() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_buy_step1, container, false);

		viewfind = view.findViewById(R.id.viewfinder);
		textView = view.findViewById(R.id.nexttocontinue);
		createCameraSource();

		return view;
	}

	private void createCameraSource() {

		final BarcodeDetector barcodeDetector = new BarcodeDetector
				.Builder(getContext())
				//	.setBarcodeFormats()
				.build();
		final CameraSource cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
				.setAutoFocusEnabled(true)
				.setRequestedFps(15.0f)
				.setRequestedPreviewSize(1024, 1024)
				.build();

		viewfind.getHolder().addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
				Log.i("Kaldon-2", code);
				RequestQueue requestQueue = Volley.newRequestQueue(getContext());
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("QRCode", BuyActivity.qrcode);
					jsonObject.put("code", code);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				barcodeDetector.release();

				String URL = MainActivity.address+"/getProductDetails";
				ConnectionManager.sendData(jsonObject.toString(), requestQueue, URL, new ConnectionManager.VolleyCallback() {
					@Override
					public void onSuccessResponse(String result) {
						viewfind.setVisibility(View.INVISIBLE);
						textView.setVisibility(View.VISIBLE);

					}

					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});



			}
		});
	}

}
