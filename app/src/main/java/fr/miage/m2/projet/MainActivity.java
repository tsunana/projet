package fr.miage.m2.projet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, LocationListener {
    private FloatingActionButton nav;
    private FloatingActionButton opencam;
    private FusedLocationProviderClient mFusedLocationClient;
    private FirebaseFirestore mDb;
    private Sprite sprite;
    private SpriteDAO spriteDao;
    private Intent i_camera;
    private Intent i_maps;
    private LatLngBounds mMapBoundaries;
    private MarkerOptions mCurrLocationMarker;
    private ArrayList<Sprite> sprites = new ArrayList<>();
    private GoogleApi mGoogleApiClient;
    private LocationRequest locationRequest;



    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spriteDao = new SpriteDAO(getApplicationContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        if(checkGooglePlayServices()){
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        } else{
            Toast.makeText(this,"Google Play Services Not Available",Toast.LENGTH_SHORT).show();
        }
       //mDb = FirebaseFirestore.getInstance();

        //avoir la geolocalisation du device
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        opencam = findViewById(R.id.opencam);
        opencam.setVisibility(View.INVISIBLE);



    }

    private boolean checkGooglePlayServices(){
        GoogleApiAvailability gApiAv = GoogleApiAvailability.getInstance();
        int res = gApiAv.isGooglePlayServicesAvailable(this);
        if(res == ConnectionResult.SUCCESS){
            return true;

        } else if (gApiAv.isUserResolvableError(res)){
            Dialog dialog = gApiAv.getErrorDialog(this, res, 2021, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Toast.makeText(MainActivity.this, "User Canceled Dialog", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        }
        return false;
    }




    private void saveSpriteLocation() {
        if (sprite != null) {
            DocumentReference locationRef = mDb.
                    collection(getString(R.string.collection_sprite)).document();
        }
    }

    //here we are retrieving the location of our device.

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        Log.d("MainActivity", "getLastKnownLocation: called.");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();

                    GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                    Log.d("MainActivity", "onComplete: latitude" + geoPoint.getLatitude());
                    Log.d("MainActivity", "onComplete: longitude" + geoPoint.getLongitude());

                    //définir les limites de la maps
                    double bottomBoundary = location.getLatitude() - 0.00001;
                    double leftBoundary = location.getLongitude() - 0.00001;
                    double topBoundary = location.getLatitude() + 0.00001;
                    double rightBoundary = location.getLongitude() + 0.00001;
                    mMapBoundaries = new LatLngBounds(
                            new LatLng(bottomBoundary,leftBoundary),
                            new LatLng(topBoundary,rightBoundary)
                    );

                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundaries,0));

                    LatLng myPos = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                    //LatLng spriteMeca = new LatLng(44.833880, -0.566170);
                    //mMap.addMarker(new MarkerOptions().position(spriteMeca).title("Sprite 1").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    sprites = addSpritesOnMap();
                    mCurrLocationMarker =  new MarkerOptions()
                            .title("My position");
                    mMap.addMarker(new MarkerOptions()
                            .position(myPos)
                            );
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));
                    mMap.getUiSettings().setCompassEnabled(true);
                    sprites.get(0).getName();
                    /*for(int i=0; i <sprites.size();i++){
                        Polyline line = mMap.addPolyline(new PolylineOptions()
                                .add(myPos, sprites.get(i).getLatLng())
                                .width(5)
                                .color(Color.RED));
                        checkLocationSprite(mCurrLocationMarker,sprites.get(i).getMarker());
                    }*/

                }
            }
        });


    }


    @Override
    public void onLocationChanged(Location location) {
        if (mCurrLocationMarker != null) {
            //mCurrLocationMarker.remove();
        }
//Showing Current Location Marker on Map
        LatLng myPos = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(myPos);


        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations && null != providerList && providerList.size() > 0) {
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(),
                    Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude,
                        longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
                    String state = listAddresses.get(0).getAdminArea();
                    String country = listAddresses.get(0).getCountryName();
                    String subLocality = listAddresses.get(0).getSubLocality();
                    markerOptions.title("" + myPos + "," + subLocality + "," + state
                            + "," + country);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


    }

    private void checkLocationSprite(MarkerOptions myPos, MarkerOptions sprite){
        if(myPos.equals(sprite)){
            opencam.setVisibility(View.VISIBLE);

        }


    }


    public byte[] getBytes(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
    private ArrayList<Sprite> addSpritesOnMap(){
        GeoPoint geoPoint1 = new GeoPoint(43.3114334,-0.3843101);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.camera);

        GeoPoint geoPoint2 = new GeoPoint(43.30260467529297,-0.3971642553806305);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.camera);



        MarkerOptions aspttM = new MarkerOptions().title("ASPTT").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        Sprite asptt = new Sprite("ASPTT", geoPoint1.getLatitude(), geoPoint1.getLongitude(), getBytes(bitmap1));
        spriteDao.addSprite(asptt);
        asptt.setMarker(aspttM);
        mMap.addMarker(aspttM
                .position(asptt.getLatLng()));

        MarkerOptions jejeM = new MarkerOptions().title("jeje").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        Sprite jeje = new Sprite("jeje", geoPoint2.getLatitude(),geoPoint2.getLongitude(),getBytes(bitmap2));

        mMap.addMarker(jejeM
                .position(jeje.getLatLng()).title("jeje").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        jeje.setMarker(jejeM);
        spriteDao.addSprite(asptt);
        spriteDao.addSprite(jeje);
        //sprites.add(asptt);
        //sprites.add(jeje);

        sprites.add(asptt);
        sprites.add(jeje);

        return sprites;
    }


    @Override
    public void onClick(View view) {
        i_camera = new Intent(this, CameraActivity.class);
        i_maps = new Intent(this, MapsActivity.class);

        switch (view.getId()) {
            case R.id.opencam:
              startActivity(i_camera);
                break;
            case R.id.nav:
                startActivity(i_maps);
                break;


        }
    }
}

    /*
    private boolean mLocationPermissionGranted = false;
    private static final String TAG = "MainActivity";

    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.

        if (/*ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;*/
            //getChatrooms();
       /* } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(mLocationPermissionGranted){
                    //getChatrooms();
                }
                else{
                    getLocationPermission();
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav = findViewById(R.id.navigation);
    }



        @Override
    public void onClick(View view) {
        //Intent i = new Intent(Intent.ACTION_VIEW,
          //      Uri.parse("google.navigation:q=\t44.823462,\t-0.556514&mode=w"));
        Intent i = new Intent(this,MapsActivity.class);
        i.setPackage("com.google.android.apps.maps");

        if(i.resolveActivity(getPackageManager()) !=null){
            startActivity(i);

        }


    }
    */
