package com.example.springboot.controllers;

import com.example.springboot.dtos.AtendimentoRecordDto;
import com.example.springboot.dtos.ClienteRecordDto;
import com.example.springboot.models.AtendimentoModel;
import com.example.springboot.models.ClienteModel;
import com.example.springboot.models.FuncionarioModel;
import com.example.springboot.repositories.AtendimentoRepository;
import com.example.springboot.repositories.ClienteRepository;
import com.example.springboot.repositories.FuncionarioRepository;
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
public class AtendimentoController {
    @Autowired
    AtendimentoRepository atendimentoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping("/atendimentos")
    public ResponseEntity<AtendimentoModel> addService(@RequestBody @Valid AtendimentoRecordDto atendimentoRecordDto){
        //Buscando o funcionario
        Optional<FuncionarioModel> funcionarioOptional = funcionarioRepository.findById(UUID.fromString(atendimentoRecordDto.id_funcionario()));

        if (funcionarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        //Buscando o cliente
        Optional<ClienteModel> clienteOptional = clienteRepository.findById(UUID.fromString(atendimentoRecordDto.id_cliente()));

        if (clienteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        AtendimentoModel atendimentoModel = new AtendimentoModel();
        BeanUtils.copyProperties(atendimentoRecordDto, atendimentoModel);
        atendimentoModel.setFuncionario(funcionarioOptional.get());
        atendimentoModel.setCliente(clienteOptional.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(atendimentoRepository.save(atendimentoModel));
    }

    @GetMapping("/atendimentos")
    public ResponseEntity<List<AtendimentoModel>> getAllService(){
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoRepository.findAll());
    }

    @GetMapping("/atendimentos/{id}")
    public ResponseEntity<Object> getOneService(@PathVariable(value = "id") UUID id) {
        Optional<AtendimentoModel> atendimentoO = atendimentoRepository.findById(id);
        if (atendimentoO.isEmpty()) { //Também poderia fazer a lógica inversa utilizando o product0.isPresent()
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoO.get());
    }

    @PutMapping("/atendimentos/{id}")
    public ResponseEntity<Object> updateService(@PathVariable(value = "id") UUID id, @RequestBody @Valid AtendimentoRecordDto atendimentoRecordDto){
        Optional<AtendimentoModel> atendimentoO = atendimentoRepository.findById(id);
        if (atendimentoO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }

        AtendimentoModel atendimentoModel = atendimentoO.get();
        BeanUtils.copyProperties(atendimentoRecordDto, atendimentoModel);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoRepository.save(atendimentoModel));
    }

    @DeleteMapping("/atendimentos/{id}")
    public ResponseEntity<Object> deleteService(@PathVariable(value = "id") UUID id) {
        Optional<AtendimentoModel> atendimentoO = atendimentoRepository.findById(id);

        if (atendimentoO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }

        atendimentoRepository.delete(atendimentoO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Service deleted successfully");
    }

    @GetMapping("/atendimentos/cliente/{id_cliente}")
    public ResponseEntity<Object> getManyServicesByIdCliente(@PathVariable(value = "id_cliente") UUID id_cliente, @RequestParam(value = "data") String data){
        List<AtendimentoModel> atendimentoModel = atendimentoRepository.findByIdCliente(id_cliente,data);
        if (atendimentoModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoModel);
    }

    @PutMapping("/atendimentos/updateLink")
    public ResponseEntity<Object> updateLinkRelatorio(@RequestParam(value = "link") String link, @RequestParam(value = "id") UUID id){
        int updated = atendimentoRepository.updateLinkRelatorio(link, id);
        if (updated == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Service updated successfully");
    }
}
