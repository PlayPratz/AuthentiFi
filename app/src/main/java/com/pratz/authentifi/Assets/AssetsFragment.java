package com.pratz.authentifi.Assets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pratz.authentifi.ConnectionManager;
import com.pratz.authentifi.ProductPage;
import com.pratz.authentifi.R;
import com.pratz.authentifi.User.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Thread.sleep;
import java.util.List;
import java.util.ArrayList;

public class AssetsFragment extends Fragment {

	View view=null;
	List<Asset> assetList = new ArrayList<Asset>();
	RecyclerView.Adapter mAdapter;


	String textAddress = "192.168.43.24";

	public AssetsFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(view==null)
			Log.d("yolo hi", "view is null create");
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);


		/*String shit[] = {"Java", "Should", "Die", "Because", "OOP", "Sucks", "So", "Hard", "That", "I", "Would", "Rather",
				"Use", "C", "Or", "Python", "Java", "Should", "Die", "Because", "OOP", "Sucks", "So", "Hard", "That", "I",
				"Would", "Rather", "Use", "C", "Or", "Python"};*/

		Asset asset = new Asset("somecode", "Nike", "Cloudfoam");


		//ListView listView = (ListView) view.findViewById(R.id.assetrecycler);
		//ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.listtext, shit);
		//listView.setAdapter(arrayAdapter);





		View view = getView();

		//RecyclerView

		RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.assetrecycler);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);

		// use a linear layout manager
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);

		// specify an adapter (see also next example)
		//RecyclerView.Adapter mAdapter = new MyAdapter(shit);

		//Lets hope clicking works
		mAdapter = new AssetAdapter(assetList);

		mRecyclerView.setAdapter(mAdapter);


		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
				((LinearLayoutManager) mLayoutManager).getOrientation());
		mRecyclerView.addItemDecoration(dividerItemDecoration);


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {


		View view = inflater.inflate(R.layout.fragment_assets, container, false);


		Log.d("yolo hi", "Asset got createView()");

		RequestQueue requestQueue = Volley.newRequestQueue(getContext());

		String URL = "http://"+textAddress+":8080/myAssets";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("email", "pratz@playmax.in");
		}
		catch (Exception e)
		{}
		String requestBody = jsonObject.toString();


		ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback() {
			@Override
			public void onSuccessResponse(String result) {
				try {
					JSONArray jsonArray = new JSONArray(result);
					jsonArray.remove(0);
					JSONObject tempObject;
					assetList.clear();
					for(int i=0; i<jsonArray.length(); i++) {

						tempObject = jsonArray.getJSONObject(i);
						assetList.add(i, new Asset(tempObject.getString("code"),
								tempObject.getString("model"),tempObject.getString("model")));

					}
					mAdapter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});


		return view;
	}


	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
	}


	//Crap I added
	/*
	class AssetAdapter extends com.pratz.authentifi.Assets.AssetAdapter {

		private String mDataset[];

		public AssetAdapter(String[] myDataset) {
			super(myDataset);
			mDataset = myDataset;
		}

		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return super.onCreateViewHolder(parent, viewType);
		}

		@Override
		public void onBindViewHolder(MyViewHolder holder, final int position) {
			super.onBindViewHolder(holder, position);
			holder.mCode.setText(mDataset[position]);

			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), ProductPage.class);
					Bundle bundle = new Bundle();
					bundle.putString("code", mDataset[position]);
					bundle.putBoolean("isOwner", true);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
		}
	}*/
}


