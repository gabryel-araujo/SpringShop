package com.example.springboot.repositories;


import com.example.springboot.models.FuncionarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FuncionarioRepository extends JpaRepository<FuncionarioModel, UUID> {
    // Essa classe apenas herda de um repositorio jpa os métodos, mas pra isso é necessário passar o tipo do retorno e o tipo do ID

//    @Query("SELECT SUM(p.value) FROM FuncionarioModel p")
//    BigDecimal getSum();

}
