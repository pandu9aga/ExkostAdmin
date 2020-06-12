package com.example.exkostadmin;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuHomeFragment extends Fragment {

    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;

    BtmmenuJenis fragJenis;
    BtmmenuTransfer fragTransfer;
    BtmmenuTopup fragTopup;

    MenuItem menuItem;

    View view;

    public MenuHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.menu_fragment_home, container, false);

        viewPager = view.findViewById(R.id.pager);
        setupViewPager(viewPager);

        bottomNavigationView = view.findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.jenis_menu:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.transfer_menu:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.topup_menu:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                menuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        fragJenis = new BtmmenuJenis();
        fragTransfer = new BtmmenuTransfer();
        fragTopup = new BtmmenuTopup();

        adapter.addFragment(fragJenis);
        adapter.addFragment(fragTransfer);
        adapter.addFragment(fragTopup);
        viewPager.setAdapter(adapter);
    }

}
