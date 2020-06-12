package com.example.exkostadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BtmmenuTransfer extends Fragment {
    View view;
    Button lelangBerlangsung, lelangKirim, lelangSelesai,tmbBarang;

    public BtmmenuTransfer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.content_lelang, container, false);

        lelangBerlangsung = (Button) view.findViewById(R.id.lelangon);
        lelangKirim = (Button) view.findViewById(R.id.lelangsend);
        lelangSelesai = (Button) view.findViewById(R.id.lelangfin);
        tmbBarang = (Button) view.findViewById(R.id.tambahbrg);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.framelelang, new LelangBerlangsung());
        fragmentTransaction.commit();


        lelangBerlangsung.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadFragment(new LelangBerlangsung());

                TextView textView = (TextView) view.findViewById(R.id.lelangname);
                textView.setText("Berlangsung");

            }
        });
        lelangKirim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadFragment(new LelangKirim());

                TextView textView = (TextView) view.findViewById(R.id.lelangname);
                textView.setText("Kirim");

            }
        });
        lelangSelesai.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadFragment(new LelangSelesai());

                TextView textView = (TextView) view.findViewById(R.id.lelangname);
                textView.setText("Selesai");

            }
        });
        tmbBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// display a message by using a Toast
                Intent intent = new Intent(getActivity(), Tambah.class);
                startActivity(intent);
            }
        });

        return view;

    }

    private void loadFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelelang, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}