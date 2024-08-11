package com.example.springboot.repositories;

import com.example.springboot.models.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Integer> {

    @Query("SELECT c FROM ClienteModel c WHERE c.nome LIKE %:nome% ORDER BY c.nome ASC")
    List<ClienteModel> findByNome(String nome);

    @Query("SELECT c FROM ClienteModel c ORDER BY c.nome ASC")
    List<ClienteModel> findAllOrderByNomeAsc();

    @Query("SELECT c FROM ClienteModel c WHERE c.bairro = :bairro")
    List<ClienteModel> findByBairro(String bairro);

    @Query("SELECT DISTINCT c.bairro FROM ClienteModel c order by c.bairro ASC")
    List<String> findDistinctBairro();

    @Query("SELECT C FROM ClienteModel C WHERE C.nome = :nome OR C.nome = '' AND C.bairro = :bairro or C.nome = '' ORDER BY C.bairro ASC")
    List<ClienteModel> findByNomeAndBairro(@Param("nome")String nome, @Param("bairro") String bairro);

}
