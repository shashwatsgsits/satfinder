package com.mapmart.satfinder;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mapmart.satfinder.R.drawable;
import com.mapmart.satfinder.R.menu;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
//import android.content.CursorLoader;
import android.content.Intent;
//import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.SearchView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor>{

	GoogleMap mGoogleMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		ActionBar actionbar = getActionBar();
		actionbar.show();
		actionbar.setIcon(null);
		actionbar.setTitle(null);
		
		SearchView searchView = new SearchView(this);
	    actionbar.setCustomView(searchView);
	    
	    actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	    searchView.setFocusable(false);
	    searchView.setIconified(false);
	    //searchView.requestFocusFromTouch();
	
	     		
		
		
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mGoogleMap = fragment.getMap();
 
        //mGoogleMap.setMyLocationEnabled(true);
//		mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false); // delete default button

        handleIntent(getIntent());
	}
	
	private void handleIntent(Intent intent){
        if(intent.getAction().equals(Intent.ACTION_SEARCH)){
            doSearch(intent.getStringExtra(SearchManager.QUERY));
        }else if(intent.getAction().equals(Intent.ACTION_VIEW)){
            getPlace(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
        }
    }
	
	@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }
	
	 @SuppressWarnings("unchecked")
	private void doSearch(String query){
	        Bundle data = new Bundle();
	        data.putString("query", query);
	        getSupportLoaderManager().restartLoader(0, data, this);
	    }
	 
	 @SuppressWarnings("unchecked")
	private void getPlace(String query){
	        Bundle data = new Bundle();
	        data.putString("query", query);
	        getSupportLoaderManager().restartLoader(1, data, this);
	    }
	 
	 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
        
		/*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	    searchView.setIconifiedByDefault(false); 
	    searchView.setIconified(false);
	    
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    */
	    return true;
	    //return super.onCreateOptionsMenu(menu);
	}
	
	@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
       
		switch(item.getItemId()){
		
		case R.id.type1:
			mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
		
		case R.id.type2:
			mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		
		case R.id.type3:
			mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;
		
		case R.id.type4:
			mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;
		
		case R.id.location:
			
			/*mGoogleMap.clear();
			mGoogleMap.setMyLocationEnabled(true);
			Location location = mGoogleMap.getMyLocation();
			Toast.makeText(getBaseContext(), ""+location, Toast.LENGTH_SHORT).show();
			if(position!=null){
				MarkerOptions markerOptions = new MarkerOptions();
				markerOptions.position(position);
				mGoogleMap.addMarker(markerOptions);
				
				CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(14.0f).build();
	            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
	            mGoogleMap.animateCamera(cameraUpdate);
	        }*/
			break;
		}
        return true;
    }
 

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		CursorLoader cLoader = null;
        if(id==0)
            cLoader = new CursorLoader(getBaseContext(), PlaceProvider.SEARCH_URI, null, null, new String[]{ args.getString("query") }, null);
        else if(id==1)
            cLoader = new CursorLoader(getBaseContext(), PlaceProvider.DETAILS_URI, null, null, new String[]{ args.getString("query") }, null);
        return cLoader;
	}

	
	 
	 private void showLocations(Cursor c){
	        MarkerOptions markerOptions = null;
	        LatLng position = null;
	        mGoogleMap.clear();
	        while(c.moveToNext()){
	            markerOptions = new MarkerOptions();
	            position = new LatLng(Double.parseDouble(c.getString(1)),Double.parseDouble(c.getString(2)));
	            markerOptions.position(position);
	            markerOptions.title(c.getString(0));
	            mGoogleMap.addMarker(markerOptions);
	        }
	        if(position!=null){
	        	CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(14.0f).build();
	            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
	            mGoogleMap.animateCamera(cameraUpdate);
	        }
	 }
	 
	

	@Override
	public void onLoadFinished(android.support.v4.content.Loader<Cursor> arg0,
			Cursor c) {
		// TODO Auto-generated method stub
		showLocations(c);
		
	}

	@Override
	public void onLoaderReset(android.support.v4.content.Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}

}
