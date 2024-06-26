package com.example.springboot.repositories;

import com.example.springboot.models.AtendimentoModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AtendimentoRepository extends JpaRepository<AtendimentoModel, UUID> {

    @Query("SELECT a FROM AtendimentoModel a WHERE a.cliente.id = :id_cliente AND a.data_atendimento like :data")
    List<AtendimentoModel> findByIdCliente(UUID id_cliente, String data);

    @Modifying
    @Transactional
    @Query("UPDATE AtendimentoModel a SET a.link_relatorio = :link WHERE a.link_relatorio = 'null' AND a.id = :id")
    int updateLinkRelatorio(@Param("link") String link, @Param("id") UUID id);
}
