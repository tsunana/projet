package fr.miage.m2.projet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private FloatingActionButton nav;
    private FloatingActionButton opencam;
    private TextView near;
    private FusedLocationProviderClient mFusedLocationClient;
    private FirebaseFirestore mDb;
    private Sprite sprite;
    private SpriteDAO spriteDao;
    private Intent i_camera;
    private Intent i_maps;
    private Intent i_display;
    private LatLngBounds mMapBoundaries;
    private MarkerOptions mCurrLocationMarker;
    private Marker mCurrLocMarker;
    private Polyline line;
    private ArrayList<Sprite> sprites = new ArrayList<>();
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;



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
        near = findViewById(R.id.near);
        //opencam.setVisibility(View.INVISIBLE);



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
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);

        Location location = locationManager.getLastKnownLocation(provider);

        mMap.setMyLocationEnabled(true);
        buildGoogleApiClient();
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
                    sprites = addSpritesOnMap();

                    mCurrLocationMarker =  new MarkerOptions()
                            .title("My position");
                    mCurrLocMarker = mMap.addMarker(mCurrLocationMarker
                            .position(myPos)
                            );
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));
                    mMap.getUiSettings().setCompassEnabled(true);



                }
            }
        });



    }

    @Override
    public void onConnected(Bundle bundle) {
        //met a jour la position toutes les 5 secondes
        mLocationRequest = new LocationRequest() ;
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {

        if (mCurrLocMarker != null) {
            mCurrLocMarker.remove();
        }
        if(line != null) {
            line.remove();
        }

//Showing Current Location Marker on Map
        LatLng myPos = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        PolylineOptions lineOptions = new PolylineOptions();
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
        //mMap.addMarker(markerOptions);
        mCurrLocMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //ajouter et mettre à jour l'itinéraire
        line = mMap.addPolyline(lineOptions
                    .add(myPos, sprites.get(0).getLatLng())
                    .width(5)
                    .color(Color.RED));
        checkLocationSprite(myPos,sprites.get(0).getLatLng());
    }

    //afficher la camera
    private void checkLocationSprite(LatLng myPos, LatLng sprite){
        //if(myPos.getPosition() == sprite.getPosition()){
         //   opencam.setVisibility(View.VISIBLE);
        double lat1 = myPos.latitude;
        double lng1 = myPos.longitude;
        double lat2 = sprite.latitude;
        double lng2 = sprite.longitude;

        Location lmyPos = new Location("myPos");
        lmyPos.setLatitude(lat1);
        lmyPos.setLongitude(lng1);

        Location lsprite = new Location("lsprite");
        lsprite.setLatitude(lat2);
        lsprite.setLongitude(lng2);

        float distance = lmyPos.distanceTo(lsprite);

        if (distance <= 50.0f) {
            opencam.setVisibility(View.VISIBLE);
            // les points sont à moins d'un mètre l'un de l'autre
        } else {
            opencam.setVisibility(View.INVISIBLE);
            // les points sont à plus d'un mètre l'un de l'autre
        }
        if (distance <= 100.0f) {
            near.setVisibility(View.VISIBLE);
            // les points sont à moins d'un mètre l'un de l'autre
        } else {
            near.setVisibility(View.INVISIBLE);
            // les points sont à plus d'un mètre l'un de l'autre
        }

    }



    private ArrayList<Sprite> addSpritesOnMap(){
        GeoPoint geoPoint1 = new GeoPoint(43.3114334,-0.3843101);
        //String img = "sprite" + sprites.get(0).getId();
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.sprite1);


        GeoPoint geoPoint2 = new GeoPoint(43.30260467529297,-0.3971642553806305);


        GeoPoint geoPoint3 = new GeoPoint(44.812719792768995,1.6812090790848637);
        Bitmap img3 = BitmapFactory.decodeResource(getResources(), R.drawable.sprite2);




        MarkerOptions aspttMo = new MarkerOptions().title("ASPTT").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        Sprite asptt = new Sprite("ASPTT", geoPoint1.getLatitude(), geoPoint1.getLongitude());
        spriteDao.addSprite(asptt);
        //asptt.setImage(img);

        Marker aspttM = mMap.addMarker(aspttMo
                .position(asptt.getLatLng()));
        asptt.setMarker(aspttM);

        MarkerOptions jejeMo = new MarkerOptions().title("jeje").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        Sprite jeje = new Sprite("jeje", geoPoint2.getLatitude(),geoPoint2.getLongitude());

        Marker jejeM = mMap.addMarker(jejeMo
                .position(jeje.getLatLng()).title("jeje").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        //Bitmap img = BitmapFactory.decodeFile("D:\\MIAGE\\M2_MIAGE\\S1\\PAM\\app\\src\\main\\res\\drawable\\salameche.png");


        MarkerOptions adeleMo = new MarkerOptions().title("jeje").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        //Sprite adele = new Sprite("adele", geoPoint3.getLatitude(),geoPoint3.getLongitude(),getBytes(img3));

        //Marker adeleM = mMap.addMarker(adeleMo.position(adele.getLatLng()).title("adele").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        //asptt.setImage(img);
        //adele.setImage(img3);
        //adele.setMarker(adeleM);

        jeje.setMarker(jejeM);
        //spriteDao.addSprite(adele);
        spriteDao.addSprite(asptt);
        spriteDao.addSprite(jeje);

        //sprites.add(asptt);
        //sprites.add(jeje);

        //sprites.add(adele);
        sprites.add(asptt);
        sprites.add(jeje);

        return sprites;
    }


    @Override
    public void onClick(View view) {
        i_camera = new Intent(this, CameraActivity.class);

        switch (view.getId()) {
            case R.id.opencam:
                    String strSprite = "sprite"+sprites.get(0).getId();

                    //i_camera.putExtra("img", outputFile.getAbsoluteFile());
                    i_camera.putExtra("img",strSprite);
                    i_camera.putParcelableArrayListExtra("key", sprites);
                    //i_camera.putExtra("sprites",sprites);
                    startActivity(i_camera);
                    //sprites.remove(0);

                //Toast.makeText(this, "img est null !!!", Toast.LENGTH_SHORT).show();



                break;
            case R.id.nav:
                startActivity(i_maps);
                break;


        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
