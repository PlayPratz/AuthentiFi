package com.pratz.authentifi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.pratz.authentifi.Assets.AssetsFragment;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class MainActivity extends AppCompatActivity {

	FragmentManager fragmentManager;

	private void askForPermission(String permission, Integer requestCode) {
		if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

				//This is called if user has denied the permission before
				//In this case I am just asking the permission again
				ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

			}
			else {

				ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
			}
		} /*else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }*/
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		askForPermission(Manifest.permission.CAMERA, CAMERA);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		//setSupportActionBar(toolbar);

		final Integer fragmentContainer = R.id.fragment_container;
		fragmentManager = getSupportFragmentManager();
		final Fragment Assets = new AssetsFragment();
		final Fragment Profile = new MyProfileFragment();


		BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
		bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

				Fragment fragment = null;

				switch (menuItem.getItemId()) {
					case R.id.navigation_assets:
						fragment = Assets;
						break;

					case R.id.navigation_scan:
						openCam();
						return false;

					case R.id.navigation_profile:
						fragment = Profile;
						break;

				}

				fragmentManager.beginTransaction()
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
						.replace(fragmentContainer, fragment)
						.commit();
				return true;
			}
		});

		//set screen at boot
		fragmentManager.beginTransaction().add(fragmentContainer, Assets).commit();
	}


	void pushlogs(Fragment fragment) {

		Log.d("yolo added", Boolean.toString(fragment.isAdded()));
		Log.d("yolo stsvd", Boolean.toString(fragment.isStateSaved()));
		Log.d("yolo detac", Boolean.toString(fragment.isDetached()));
		Log.d("yolo hidde", Boolean.toString(fragment.isHidden()));
		Log.d("yolo visib", Boolean.toString(fragment.isVisible()));
		Log.d("yolo resum", Boolean.toString(fragment.isResumed()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void openCam() {
		Intent intent = new Intent(this, Viewfind.class);
		startActivity(intent);
	}




}


// Code that I used temporarily until I did it the right way:

/*
class SwipelessViewPager extends ViewPager {

	private boolean isPagingEnabled;

	public SwipelessViewPager(Context context) {
		super(context);
		this.isPagingEnabled = true;
	}

	public SwipelessViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.isPagingEnabled = true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.isPagingEnabled && super.onTouchEvent(event);
	}

	//for samsung phones to prevent tab switching keys to show on keyboard
	@Override
	public boolean executeKeyEvent(KeyEvent event) {
		return isPagingEnabled && super.executeKeyEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return this.isPagingEnabled && super.onInterceptTouchEvent(event);
	}

	public void setPagingEnabled(boolean enabled) {
		this.isPagingEnabled = enabled;
	}
}


class BottomNavigationAdapter extends FragmentPagerAdapter {

	private final List<Fragment> fragmentList = new ArrayList<>();

	BottomNavigationAdapter(FragmentManager manager) {
		super(manager);
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

	public void addFragment(Fragment fragment){
		fragmentList.add(fragment);
	}

}*/


/** TODO:
Android App:

 Enviroment 1(Customer):

 Login(username, password)

 Signup(email,password,name,photo(*),phone)

 MyAssets(List of assets, when you click on the assets it takes you to ProductPage)

 ProductPage(Brand name, model no,image,desc,status & Manufacturer(location,timestamp) & ++PreviousOwner(name,timestamp))
 (Button for SELL -> which goes to SellActivity with the code)
 (Button for Report Stolen -> and response)

 Scanner (Can scan NFC code, once scanned goes to ProductPage
 (Brand name, model no,image,desc,status & Manufacturer(location,timestamp))

 MyProfile(Details, and listview of transaction history)
		buyer    (Scanner for scanning sellers QR Code -> Scanner for scanning NFC code -> Confirm)


 Enviroment 2(Retailer):

 Login(username, password)

 Scanner(scans the NFC code, then the same process of second hand)
*/
