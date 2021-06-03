package com.tfg.controlparental.ui.opciones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OpcionesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OpcionesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Opciones");
    }

    public LiveData<String> getText() {
        return mText;
    }
}