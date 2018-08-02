package com.absences.victor.absences.interfaces;

import com.absences.victor.absences.domains.Alumno;

public interface OnAlumnoReturn {

    void onAlumnosConsultados(Alumno alumno);

    void onAlumnosError();
}
