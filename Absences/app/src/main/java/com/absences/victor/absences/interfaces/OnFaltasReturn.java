package com.absences.victor.absences.interfaces;

import com.absences.victor.absences.domains.Falta;

import java.util.ArrayList;

public interface OnFaltasReturn {

    void onFaltasConsultados(ArrayList<Falta> litaFaltas);

    void onFaltasError();
}
