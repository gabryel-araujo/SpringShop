package com.example.springboot.repositories;


import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
    // Essa classe apenas herda de um repositorio jpa os métodos, mas pra isso é necessário passar o tipo do retorno e o tipo do ID

    @Query("SELECT SUM(p.value) FROM ProductModel p")
    BigDecimal getSum();

}
