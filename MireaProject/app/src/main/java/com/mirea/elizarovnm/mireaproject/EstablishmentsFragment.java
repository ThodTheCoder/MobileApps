package com.mirea.elizarovnm.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.geometry.Point;
import com.yandex.runtime.image.ImageProvider;

import java.util.Arrays;
import java.util.List;

public class EstablishmentsFragment extends Fragment {
    private MapView mapView;
    private RecyclerView recyclerView;
    private List<Establishment> establishments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_establishments, container, false);

        mapView = view.findViewById(R.id.mapview);
        recyclerView = view.findViewById(R.id.recycler_view);

        MapKitFactory.initialize(getContext());
        mapView.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        loadEstablishments();
        addMarkers();

        return view;
    }

    private void loadEstablishments() {
        // Загрузите данные заведений, например, из локального массива или базы данных
        establishments = Arrays.asList(
                new Establishment("Стромынка 20", "Институт кибербезопасности", new Point(55.794259, 37.701448)),
                new Establishment("ул. Большая Пироговская, д. 22", "Учебный корпус на Большой Пироговской", new Point(55.728738, 37.567653)),
                new Establishment("проспект Вернадского, д. 78", "Институт информационных технологий", new Point(55.669986, 37.480409))
        );
    }

    private void addMarkers() {
        Map map = mapView.getMap();
        for (Establishment establishment : establishments) {
            PlacemarkMapObject marker = map.getMapObjects().addPlacemark(establishment.getLocation());
            marker.setUserData(establishment);
            marker.addTapListener((mapObject, point) -> {
                Establishment data = (Establishment) mapObject.getUserData();
                Toast.makeText(getContext(), data.getName() + ": " + data.getDescription(), Toast.LENGTH_SHORT).show();
                return true;
            });
            marker.setIcon(ImageProvider.fromResource(getContext(), com.yandex.maps.mobile.R.drawable.search_layer_pin_selected_default));
        }
    }

    @Override
    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }
}