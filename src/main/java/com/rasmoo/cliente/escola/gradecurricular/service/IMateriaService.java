package com.rasmoo.cliente.escola.gradecurricular.service;

import com.rasmoo.cliente.escola.gradecurricular.dto.MateriaDto;
import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;

import java.util.List;

public interface IMateriaService {

    Boolean atualizar(final MateriaDto materia);

    Boolean excluir(final Long id);

    /*
     * LISTAR todas matérias.
     */
    List<MateriaDto> listar();

    /*
     * CONSULTA uma matéria a partir do ID.
     */
    MateriaDto consultar(final Long id);

    /*
     * CADASTRAR uma matéria.
     */
    Boolean cadastrar(final MateriaDto materia);

    List<MateriaDto> listarPorHorarioMinimo(int horaMinima);

    List<MateriaDto> listarPorFrequencia(int frequencia);
}
