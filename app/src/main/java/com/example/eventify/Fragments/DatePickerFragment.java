package com.example.eventify.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.eventify.activities.AgregarActivity;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Obtener la fecha actual para mostrarla por defecto en el DatePickerDialog
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Crear una nueva instancia de DatePickerDialog y devolverla
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Obtener la actividad que contiene este fragmento
        AgregarActivity activity = (AgregarActivity) getActivity();
        if (activity != null) {
            // Llamar al método onDateSet de la actividad
            activity.onDateSet(view, year, month, dayOfMonth);
        }
    }
}
