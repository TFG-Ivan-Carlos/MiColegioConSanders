package com.tfg.controlparental.ui.alumno;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlumnoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AlumnoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Que alumno quiere consultar");
    }

    public LiveData<String> getText() {
        return mText;
    }

}