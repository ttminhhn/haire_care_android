package com.example.haircare.Fragments.MainApp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.haircare.Activities.Booking.HistoryCutActivity;
import com.example.haircare.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class HomeFragment extends Fragment {
    private View view;
    private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initview();
        setListenFragment();
        return view;
    }

    private void initview() {
        bottomNavigationView = view.findViewById(R.id.bottomNavView);
    }

    private void setListenFragment() {

        bottomNavigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_booking:
                        Intent intent = new Intent(getContext(), com.example.haircare.Activities.Booking.BookingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_history:
                        Intent intent2 = new Intent(getContext(), HistoryCutActivity.class);
                        startActivity(intent2);
                        break;
                    /*case R.id.menu_member:
                        Intent intent3 = new Intent(getContext(), MemberActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.menu_rewards:
                        Intent intent4 = new Intent(getContext(), RewardsActivity.class);
                        startActivity(intent4);
                        break;*/
                }
            }
        });

    }
}