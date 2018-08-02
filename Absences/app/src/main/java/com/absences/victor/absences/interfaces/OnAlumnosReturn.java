package com.absences.victor.absences.interfaces;

import com.absences.victor.absences.domains.Matricula;

import java.util.ArrayList;

public interface OnAlumnosReturn {

    void onAlumnosConsultados(ArrayList<Matricula> listaAlumnos);

    void onAlumnosError();

}
