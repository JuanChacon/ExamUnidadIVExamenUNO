package edu.tecii.android.examenunidadivexamenuno;

import android.*;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener  {

    private GoogleMap mMap;

    Button btnStartStop;

    LocationManager manager;

    boolean startStop = false;

    double longitud = 0;
    double latitud = 0;

    int numPuntos = 0;

    TareaAsync tareaAs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnStartStop = (Button)findViewById(R.id.btnstartStop);

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        btnStartStop.setText(getResources().getString(R.string.opcion1));

        //no todos los dispositivos tienen geolocalizacion
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //looper tiempo, como un contador                           //tiempo minimo, distancia minima
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Criteria criteria = new Criteria();
        String proveedor = manager.getBestProvider( criteria, true); //true siempre este activo
        Location location = manager.getLastKnownLocation(proveedor);
        if (location!= null){
            onLocationChanged(location);
        }else{
            btnStartStop.setText(R.string.opcion2);
        }


        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startStop = !startStop;
                if (startStop){
                 btnStartStop.setText(R.string.opcion1);
                  tareaAs = new TareaAsync();
                    tareaAs.execute();
                }else {
                    btnStartStop.setText(R.string.opcion2);
                    tareaAs.cancel(true);


                }
            }
        });



    }

    class TareaAsync extends AsyncTask<Integer,Integer,Void>{
        public TareaAsync() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            btnStartStop.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            btnStartStop.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            if(startStop){
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // usar metodo auxiliar
                            marcasMapa();

                        }
                    });
                Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
*/
        /*
        *  // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(28.7080861, -106.10645);
        //ubicacion, posicion , titulo de la marca
        mMap.addMarker(new MarkerOptions().position(location).title("Chihuas")); //marcas
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,10));//mover automaticamente a la localizacion, como poner zoom, se adentra dentro del mapa
        UiSettings confi = mMap.getUiSettings();//mostrar caracteristicas como brujulas, paleta de herramientas, usar la app de google en la pantalla

        confi.setCompassEnabled(true); //brujula

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);// mapa normal
        * */
    }

    @Override
    public void onLocationChanged(Location location) {

        longitud = location.getLongitude();
        latitud = location.getLatitude();

    }

    public void marcasMapa(){
        numPuntos++;
        LatLng location = new LatLng(latitud, longitud);
        //ubicacion, posicion , titulo de la marca
        mMap.addMarker(new MarkerOptions().position(location).title("Punto# "+ numPuntos )); //marcas
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,10));//mover automaticamente a la localizacion, como poner zoom, se adentra dentro del mapa
        UiSettings confi = mMap.getUiSettings();//mostrar caracteristicas como brujulas, paleta de herramientas, usar la app de google en la pantalla

        confi.setCompassEnabled(true); //brujula

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}


