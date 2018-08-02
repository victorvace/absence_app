package com.absences.victor.absences.interfaces;

import com.absences.victor.absences.domains.Usuario;

public interface OnLoginReturn {

    void onLoginConsultados(Usuario usuario);

    void onLoginError();
}
