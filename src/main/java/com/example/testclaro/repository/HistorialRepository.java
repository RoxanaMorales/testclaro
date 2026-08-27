package com.example.testclaro.repository;

import com.example.testclaro.model.Estado;
import com.example.testclaro.model.Historial;
import com.example.testclaro.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistorialRepository extends JpaRepository <Historial, Long>{
    @Query("SELECT h FROM Historial h WHERE h.orden = :orden ORDER BY h.dateCreated ASC")
    List<Historial> findOrdenOrderByDateCreatedASC(@Param("orden") Orden orden);
}
