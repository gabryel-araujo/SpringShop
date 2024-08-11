package com.example.springboot.controllers;

import com.example.springboot.dtos.FuncionarioRecordDto;
import com.example.springboot.dtos.FuncionarioSumaryDto;
import com.example.springboot.models.FuncionarioModel;
import com.example.springboot.repositories.FuncionarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class FuncionarioController {

    @Autowired
    FuncionarioRepository funcionarioRepository;
    //Permite a inserção dos métodos JPA

    @PostMapping("/funcionarios")
    public ResponseEntity<FuncionarioModel> addFunc(@RequestBody @Valid FuncionarioModel funcionarioRecordDto) {
        var funcionarioModel = new FuncionarioModel(); //Usar o var evita ter que colocar ProductModel productModel = new ProductModel();
        BeanUtils.copyProperties(funcionarioRecordDto, funcionarioModel); //Recebe o que vai ser convertido e o tipo que vai ser convertido. Recebe o DTO e converter em model
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioRepository.save(funcionarioModel));//Como o método é de inserção(Criação) no banco, utilizo o método created (201) do HttpStatus
    }

    @GetMapping("/funcionarios")
    public ResponseEntity<List<FuncionarioModel>> getAllFuncs() {
        return ResponseEntity.status(HttpStatus.OK).body(funcionarioRepository.findAll());
    }

    @GetMapping("/funcionarios/{id}")
    public ResponseEntity<Object> getOneFunc(@PathVariable(value = "id") int id) {
        Optional<FuncionarioModel> funcionarioO = funcionarioRepository.findById(id);
        if (funcionarioO.isEmpty()) { //Também poderia fazer a lógica inversa utilizando o product0.isPresent()
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(funcionarioO.get());
    }

    @PutMapping("/funcionarios/{id}")
    public ResponseEntity<Object> updateFunc(@PathVariable(value = "id") int id, @RequestBody @Valid FuncionarioRecordDto funcionarioRecordDto){
        Optional<FuncionarioModel> funcionarioO = funcionarioRepository.findById(id);
        if (funcionarioO.isEmpty()) { //Também poderia fazer a lógica inversa utilizando o product0.isPresent()
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

//        ProductModel productModel = new ProductModel(); -> Isso não pode ser feito pois já existe um int cadastrado para esse produto
//        Fazer um novo ProductModel, geraria um novo int para esse produto.
        FuncionarioModel funcionarioModel = funcionarioO.get();
        BeanUtils.copyProperties(funcionarioRecordDto, funcionarioModel);
        return ResponseEntity.status(HttpStatus.OK).body(funcionarioRepository.save(funcionarioModel));
    }

    @DeleteMapping("/funcionarios/{id}")
    public ResponseEntity<Object> deleteFunc(@PathVariable(value = "id") int id) {
        Optional<FuncionarioModel> funcionarioO = funcionarioRepository.findById(id);

        if (funcionarioO.isEmpty()) { //Também poderia fazer a lógica inversa utilizando o product0.isPresent()
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        funcionarioRepository.delete(funcionarioO.get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

    @GetMapping("funcionarios/funcionario/{email}")
    public ResponseEntity<Object> getFuncByEmail(@PathVariable(value = "email") String email){
        Optional<FuncionarioModel> funcionarioO = funcionarioRepository.findByEmail(email);

        if(funcionarioO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(funcionarioO.get());
    }

    @GetMapping("funcionarios/dados")
    public ResponseEntity<Object> getFuncByNameAndRole(){
        List<FuncionarioSumaryDto> funcionarioO = funcionarioRepository.findByNameAndRole();

        if(funcionarioO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(funcionarioO);
    }

    @GetMapping("funcionarios/visitas")
    public ResponseEntity<Object> getFuncOrderByVisitas(){
        List<FuncionarioModel> funcionarioO = funcionarioRepository.findFuncionariosOrderByVisitas();

        if(funcionarioO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(funcionarioO);
    }

}
