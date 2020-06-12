package com.example.exkostadmin;

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
    Button transBelum, transKirim, transTerima, transGagal;

    public BtmmenuTransfer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.content_transfer, container, false);

        transBelum = (Button) view.findViewById(R.id.transBelum);
        transKirim = (Button) view.findViewById(R.id.transKirim);
        transTerima = (Button) view.findViewById(R.id.transTerima);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.framelelang, new TransBelum());
        fragmentTransaction.commit();


        transBelum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadFragment(new TransBelum());

                TextView textView = (TextView) view.findViewById(R.id.statTrans);
                textView.setText("Kirim");

            }
        });
        transKirim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadFragment(new TransKirim());

                TextView textView = (TextView) view.findViewById(R.id.statTrans);
                textView.setText("Tunggu");

            }
        });
        transTerima.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadFragment(new TransTerima());

                TextView textView = (TextView) view.findViewById(R.id.statTrans);
                textView.setText("Sukses");

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