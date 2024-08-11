package com.example.springboot.repositories;


import com.example.springboot.dtos.FuncionarioSumaryDto;
import com.example.springboot.models.FuncionarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FuncionarioRepository extends JpaRepository<FuncionarioModel, Integer> {
    // Essa classe apenas herda de um repositorio jpa os métodos, mas pra isso é necessário passar o tipo do retorno e o tipo do ID

//    @Query("SELECT SUM(p.value) FROM FuncionarioModel p")
//    BigDecimal getSum();

    @Query("SELECT f FROM FuncionarioModel f where f.email = :email")
    Optional<FuncionarioModel> findByEmail(String email);

    @Query("SELECT new com.example.springboot.dtos.FuncionarioSumaryDto(f.nome,f.cargo,f.visitas) FROM FuncionarioModel f ORDER BY f.nome ASC")
    List<FuncionarioSumaryDto> findByNameAndRole();

//    Preciso otimizar essa consulta para trazer apenas nome, e visitas.
//    Trazer todos os dados, inclusive a senha trará vulnerabilidade.
    @Query(value = "SELECT * FROM  funcionario f ORDER BY f.visitas DESC", nativeQuery = true)
    List<FuncionarioModel> findFuncionariosOrderByVisitas();

}
