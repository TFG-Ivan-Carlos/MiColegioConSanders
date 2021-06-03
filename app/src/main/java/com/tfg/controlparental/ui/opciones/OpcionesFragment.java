package com.tfg.controlparental.ui.opciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.tfg.controlparental.R;


public class OpcionesFragment extends Fragment {

    private OpcionesViewModel opcionesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        opcionesViewModel =
                ViewModelProviders.of(this).get(OpcionesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_opciones, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        opcionesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}