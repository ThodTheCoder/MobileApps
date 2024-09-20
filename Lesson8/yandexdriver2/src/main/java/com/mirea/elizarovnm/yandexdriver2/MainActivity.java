package com.mirea.elizarovnm.yandexdriver2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.DrivingSummarySession;
import com.yandex.mapkit.directions.driving.Event;
import com.yandex.mapkit.directions.driving.Summary;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mirea.elizarovnm.yandexdriver2.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity
        implements DrivingSession.DrivingRouteListener, UserLocationObjectListener {

    private final int REQUEST_CODE_PERMISSION = 100;
    private ActivityMainBinding binding;
    private final Point ROUTE_START_LOCATION = new Point(55.797303, 37.717415);
    private final Point ROUTE_END_LOCATION = new Point(55.794229, 37.700772);
    private final Point SCREEN_CENTER = new Point(
            (ROUTE_START_LOCATION.getLatitude() + ROUTE_END_LOCATION.getLatitude()) / 2,
            (ROUTE_START_LOCATION.getLongitude() + ROUTE_END_LOCATION.getLongitude()) /
                    2);
    private MapView mapView;
    private DrivingRouter drivingRouter;
    private UserLocationLayer userLocationLayer;
    private MapObjectCollection mapObjectCollection;
    private DrivingSession drivingSession;
    private int[] colors = {0xFFFF0000, 0xFF00FF00, 0x00FFBBBB, 0xFF0000FF};
    private MapKit mapKit;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        MapKitFactory.initialize(this);
        DirectionsFactory.initialize(this);
        mapView = binding.mapView;
        setContentView(binding.getRoot());
        mapView.getMap().move(new CameraPosition(SCREEN_CENTER, 10, 0, 0));
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        mapObjectCollection = mapView.getMap().getMapObjects().addCollection();

        PlacemarkMapObject marker = mapView.getMap().getMapObjects().addPlacemark(
                new Point(55.794259, 37.701448)
        );
        marker.setIcon(ImageProvider.fromResource(this, com.yandex.maps.mobile.R.drawable.search_layer_pin_selected_default));
        marker.addTapListener(new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point
                    point) {
                Toast.makeText(getApplication(), "МИРЭА (Стромынка, 20)",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        requestPermissions();

    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    private void submitRequest(Point startPoint, Point endPoint) {
        DrivingOptions drivingOptions = new DrivingOptions();
        VehicleOptions vehicleOptions = new VehicleOptions();
        drivingOptions.setRoutesCount(4);

        ArrayList<RequestPoint> requestPoints = new ArrayList<>();
        requestPoints.add(new RequestPoint(startPoint, RequestPointType.WAYPOINT, null));
        requestPoints.add(new RequestPoint(endPoint, RequestPointType.WAYPOINT, null));

        progressDialog = ProgressDialog.show(this, "Поиск маршрута", "Пожалуйста, подождите...", true);

        drivingSession = drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions, MainActivity.this);
    }

    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
        int color;
        for (int i = 0; i < list.size(); i++) {
            color = colors[i];
            mapObjectCollection.addPolyline(list.get(i).getGeometry()).setStrokeColor(color);
        }
        PlacemarkMapObject marker = mapView.getMap().getMapObjects().addPlacemark(
                new Point(55.794259, 37.701448)
        );
        marker.setIcon(ImageProvider.fromResource(this, com.yandex.maps.mobile.R.drawable.search_layer_pin_selected_default));
        marker.addTapListener(new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point
                    point) {
                Toast.makeText(getApplication(), "проспект Вернадского, 78",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        progressDialog.dismiss();
    }

    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        String errorMessage = "unknown_error";
        if (error instanceof RemoteError) {
            errorMessage = "remote error";
        } else if (error instanceof NetworkError) {
            errorMessage = "network error";
        }
        progressDialog.dismiss();
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);
        } else {
            loadUserLocationLayer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadUserLocationLayer();
            } else {
                Toast.makeText(this, "Разрешение на определение местоположения не предоставлено",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadUserLocationLayer() {
        MapKit mapKit = MapKitFactory.getInstance();
        mapKit.resetLocationManagerToDefault();
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener((UserLocationObjectListener) this);

    }

    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {

        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                this, android.R.drawable.arrow_up_float));
        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();
        pinIcon.setIcon("pin",
                ImageProvider.fromResource(this, R.drawable.ic_launcher_foreground),
                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(1f)
                        .setScale(0.5f)
        );
        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);
        mapKit = MapKitFactory.getInstance();

        mapKit.createLocationManager().requestSingleUpdate(new LocationListener() {
            @Override
            public void onLocationUpdated(@NonNull Location location) {
                Log.d("onLocationUpdated", "TUT");
                submitRequest(location.getPosition(), new Point(55.794259, 37.701448));
            }

            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {

            }
        });
    }


    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {

    }
}