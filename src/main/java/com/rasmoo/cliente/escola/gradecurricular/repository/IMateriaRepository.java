package com.rasmoo.cliente.escola.gradecurricular.repository;

import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMateriaRepository extends JpaRepository<MateriaEntity, Long> {
    @Query("select m from MateriaEntity m where m.horas >= :horaMinima")
    List<MateriaEntity> findByHoraMinima(@Param("horaMinima") final int horaMinima);
    @Query("select m from MateriaEntity m where m.frequencia =:frequencia")
    List<MateriaEntity> findByFrequencia(@Param("frequencia") final int frequencia);
}
