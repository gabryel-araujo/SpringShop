package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;
    //Permite a inserção dos métodos JPA

    @PostMapping("/products")
    public ResponseEntity<ProductModel> addProduct(@RequestBody @Valid ProductModel productRecordDto) {
        var productModel = new ProductModel(); //Usar o var evita ter que colocar ProductModel productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel); //Recebe o que vai ser convertido e o tipo que vai ser convertido. Recebe o DTO e converter em model
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));//Como o método é de inserção(Criação) no banco, utilizo o método created (201) do HttpStatus
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> product0 = productRepository.findById(id);
        if (product0.isEmpty()) { //Também poderia fazer a lógica inversa utilizando o product0.isPresent()
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(product0.get());
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id, @RequestBody @Valid ProductRecordDto productRecordDto){
        Optional<ProductModel> product0 = productRepository.findById(id);
        if (product0.isEmpty()) { //Também poderia fazer a lógica inversa utilizando o product0.isPresent()
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

//        ProductModel productModel = new ProductModel(); -> Isso não pode ser feito pois já existe um UUID cadastrado para esse produto
//        Fazer um novo ProductModel, geraria um novo UUID para esse produto.
        ProductModel productModel = product0.get();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> product0 = productRepository.findById(id);

        if (product0.isEmpty()) { //Também poderia fazer a lógica inversa utilizando o product0.isPresent()
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        productRepository.delete(product0.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
    }

    //Daqui para baixo serão métodos que eu criei para testar o funcionamento da api

    @GetMapping("/products/total")
    public ResponseEntity<BigDecimal> getTotalProducts() {
        BigDecimal sum = productRepository.getSum();
        return ResponseEntity.status(HttpStatus.OK).body(sum);
    }

}
