package com.absences.victor.absences.interfaces;

import com.absences.victor.absences.domains.Modulo;

import java.util.ArrayList;

public interface OnCiclosReturn {

    void onCiclosConsultados(ArrayList<Modulo> listaCiclos);

    void onCiclosError();

}
