package com.absences.victor.absences.interfaces;

import com.absences.victor.absences.domains.Horario;

import java.util.ArrayList;

public interface OnHorarioReturn {

    void onHorarioConsultados(ArrayList<Horario> listaHorario);

    void onHorarioError();
}
