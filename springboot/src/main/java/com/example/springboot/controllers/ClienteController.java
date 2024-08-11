package com.example.springboot.controllers;

import com.example.springboot.dtos.ClienteRecordDto;
import com.example.springboot.dtos.FuncionarioRecordDto;
import com.example.springboot.models.ClienteModel;
import com.example.springboot.models.FuncionarioModel;
import com.example.springboot.repositories.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ClienteController {
    @Autowired
    ClienteRepository clienteRepository;

    @PostMapping("/clientes")
    public ResponseEntity<ClienteModel> addCliente(@RequestBody @Valid ClienteModel clienteRecordDto){
        ClienteModel clienteModel = new ClienteModel();
        BeanUtils.copyProperties(clienteRecordDto, clienteModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteRepository.save(clienteModel));
    }

    @GetMapping("/clientes")
    public ResponseEntity<List<ClienteModel>> getAllClients(){
        return ResponseEntity.status(HttpStatus.OK).body(clienteRepository.findAll());
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<Object> getOneClient(@PathVariable(value = "id") int id) {
        Optional<ClienteModel> clienteO = clienteRepository.findById(id);
        if (clienteO.isEmpty()) { //Também poderia fazer a lógica inversa utilizando o product0.isPresent()
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteO.get());
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<Object> updateClient(@PathVariable(value = "id") int id, @RequestBody @Valid ClienteRecordDto clienteRecordDto){
        Optional<ClienteModel> clienteO = clienteRepository.findById(id);
        if (clienteO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }

        ClienteModel clienteModel = clienteO.get();
        BeanUtils.copyProperties(clienteRecordDto, clienteModel);
        return ResponseEntity.status(HttpStatus.OK).body(clienteRepository.save(clienteModel));
    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Object> deleteClient(@PathVariable(value = "id") int id) {
        Optional<ClienteModel> clienteO = clienteRepository.findById(id);

        if (clienteO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }

        clienteRepository.delete(clienteO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Client deleted successfully");
    }

    @GetMapping("/clientes/nome/{nome}")
    public ResponseEntity<Object> getManyClientsByName(@PathVariable(value = "nome") String nome) {
        List<ClienteModel> clienteModel = clienteRepository.findByNome(nome);
        if (clienteModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteModel);
    }

    @GetMapping("/clientes/ordenados")
    public ResponseEntity<Object> getManyClientsOrderByNomeAsc() {
        List<ClienteModel> clienteModel = clienteRepository.findAllOrderByNomeAsc();
        if (clienteModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteModel);
    }

    @GetMapping("/clientes/bairro/{bairro}")
    public ResponseEntity<Object> getManyClientsByBairro(@PathVariable(value = "bairro") String bairro) {
        List<ClienteModel> clienteModel = clienteRepository.findByBairro(bairro);
        if (clienteModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteModel);
    }

    @GetMapping("/clientes/bairro/distinct")
    public ResponseEntity<Object> getDistinctBairro() {
        List<String> bairro = clienteRepository.findDistinctBairro();
        if (bairro == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(bairro);
    }

    @GetMapping("/clientes")
    public ResponseEntity<Object> getManyClientsByNomeAndBairro(@PathVariable(value = "nome") String nome, @PathVariable(value = "bairro") String bairro) {
        List<ClienteModel> clienteModel = clienteRepository.findByNomeAndBairro(nome, bairro);
        if (clienteModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteModel);
    }

}
