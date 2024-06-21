package com.example.springboot.controllers;

import com.example.springboot.dtos.ClienteRecordDto;
import com.example.springboot.dtos.FuncionarioRecordDto;
import com.example.springboot.models.ClienteModel;
import com.example.springboot.models.FuncionarioModel;
import com.example.springboot.repositories.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public ResponseEntity<Object> getOneClient(@PathVariable(value = "id") UUID id) {
        Optional<ClienteModel> clienteO = clienteRepository.findById(id);
        if (clienteO.isEmpty()) { //Também poderia fazer a lógica inversa utilizando o product0.isPresent()
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteO.get());
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<Object> updateClient(@PathVariable(value = "id") UUID id, @RequestBody @Valid ClienteRecordDto clienteRecordDto){
        Optional<ClienteModel> clienteO = clienteRepository.findById(id);
        if (clienteO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }

        ClienteModel clienteModel = clienteO.get();
        BeanUtils.copyProperties(clienteRecordDto, clienteModel);
        return ResponseEntity.status(HttpStatus.OK).body(clienteRepository.save(clienteModel));
    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Object> deleteClient(@PathVariable(value = "id") UUID id) {
        Optional<ClienteModel> clienteO = clienteRepository.findById(id);

        if (clienteO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }

        clienteRepository.delete(clienteO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Client deleted successfully");
    }

}
