package com.example.springboot.repositories;

import com.example.springboot.models.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, UUID> {

    @Query("SELECT c FROM ClienteModel c WHERE c.nome LIKE %:nome% ORDER BY c.nome ASC")
    List<ClienteModel> findByNome(String nome);

    @Query("SELECT c FROM ClienteModel c ORDER BY c.nome ASC")
    List<ClienteModel> findAllOrderByNomeAsc();
}
