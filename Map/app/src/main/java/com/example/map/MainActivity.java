package com.example.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.maplibre.android.MapLibre;
import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.camera.CameraUpdateFactory;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.location.LocationComponentActivationOptions;
import org.maplibre.android.location.modes.CameraMode;
import org.maplibre.android.location.modes.RenderMode;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.MapView;
import org.maplibre.android.maps.Style;
import org.maplibre.android.plugins.annotation.SymbolManager;
import org.maplibre.android.plugins.annotation.SymbolOptions;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private MapLibreMap mapLibreMap;
    private SymbolManager symbolManager;
    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            centerMapOnUserLocation();
                        } else {
                            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }

            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapLibre.getInstance(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapView = findViewById(R.id.mapView);
        String styleJson = loadStyleFromAssets();
        mapView.getMapAsync(map -> {
            mapLibreMap = map;
            map.setStyle(new Style.Builder().fromJson(styleJson), style ->  {
                style.addImage("marker",
                        ResourcesCompat.getDrawable(getResources(), R.drawable.ic_location, getTheme()));
                style.addImage("shopping-cart",
                        ResourcesCompat.getDrawable(getResources(), R.drawable.ic_shopping_cart, getTheme()));

                symbolManager = new SymbolManager(mapView, mapLibreMap, style);
                symbolManager.setIconAllowOverlap(true);
                symbolManager.setIconIgnorePlacement(true);

                addShoppingCarts();
            });
            map.setCameraPosition(new CameraPosition.Builder()
                    .target(new LatLng(38.522928, -8.891219))
                    .zoom(15.0)
                    .build());
            map.addOnMapClickListener(point -> {
                symbolManager.create(new SymbolOptions()
                        .withLatLng(point)
                        .withIconImage("marker"));
                return true;
            });
        });
        ImageButton centerButton = findViewById(R.id.centerLocationButton);
        centerButton.setOnClickListener(v -> askForLocationWithPermission());
    }

    private void addShoppingCarts() {
        LatLng[] locations = new LatLng[] {
                new LatLng(38.53760, -8.87806),
                new LatLng(38.53760, -8.87816),
                new LatLng(38.53745, -8.87816),
                new LatLng(38.53745, -8.87826)
        };
        for (LatLng location : locations) {
            symbolManager.create(new SymbolOptions()
                    .withLatLng(location)
                    .withIconImage("shopping-cart"));
        }
    }

    private void askForLocationWithPermission() {
        String finePermission =
                Manifest.permission.ACCESS_FINE_LOCATION;
        String coarsePermission =
                Manifest.permission.ACCESS_COARSE_LOCATION;

        if (ContextCompat.checkSelfPermission(this, finePermission) == PackageManager.PERMISSION_GRANTED) {
            centerMapOnUserLocation();
        } else if (ContextCompat.checkSelfPermission(this, coarsePermission) == PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.dialog_approximate_location)
                    .setCancelable(false)
                    .setPositiveButton(R.string.dialog_ok, (dialog, id) -> centerMapOnUserLocation())
                    .setNegativeButton(R.string.dialog_settings, (dialog, id) -> openAppSettings())
                    .create()
                    .show();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, finePermission)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, coarsePermission)) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.dialog_location_permission)
                    .setCancelable(false)
                    .setPositiveButton(R.string.dialog_ok, (dialog, id) ->
                            permissionLauncher.launch(finePermission))
                    .create()
                    .show();
        } else {
            permissionLauncher.launch(finePermission);
        }
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    private void enableLocationComponent() {
        LocationComponentActivationOptions build =
                LocationComponentActivationOptions.builder(this,
                        mapLibreMap.getStyle()).build();

        mapLibreMap.getLocationComponent().activateLocationComponent(build);

        mapLibreMap.getLocationComponent().setLocationComponentEnabled(true);

        mapLibreMap.getLocationComponent().setCameraMode(CameraMode.TRACKING);

        mapLibreMap.getLocationComponent().setRenderMode(RenderMode.COMPASS);

    }

    private void centerMapOnUserLocation() {
        if (!mapLibreMap.getLocationComponent().isLocationComponentActivated()) {
            enableLocationComponent();
        }
        Location location =
                mapLibreMap.getLocationComponent().getLastKnownLocation();
        if (location != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(),
                            location.getLongitude()))
                    .zoom(18.0)
                    .build();
            mapLibreMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private String loadStyleFromAssets() {
        String styleJson = "";
        try {
            InputStream inputStream = getAssets().open("style.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            styleJson = new String(buffer);
        } catch (IOException e) {
            Log.e("MapLibre", "Error loading style file", e);
        }
        return styleJson;
    }
}