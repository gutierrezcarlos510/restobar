package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Avatar;

import java.util.List;

public interface AvatarS {

	List<Avatar> listar();

    String getValuePorSexo(boolean sexo);
}