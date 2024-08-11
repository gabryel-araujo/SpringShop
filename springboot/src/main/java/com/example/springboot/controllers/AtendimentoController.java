package com.example.springboot.controllers;

import com.example.springboot.dtos.AtendimentoRecordDto;
import com.example.springboot.dtos.AtendimentoSumaryRecordDto;
import com.example.springboot.dtos.ClienteRecordDto;
import com.example.springboot.models.AtendimentoModel;
import com.example.springboot.models.ClienteModel;
import com.example.springboot.models.FuncionarioModel;
import com.example.springboot.projections.AtendimentoSummaryProjection;
import com.example.springboot.projections.AtendimentoTriProjection;
import com.example.springboot.projections.AtendimentoVisitasCliProjection;
import com.example.springboot.projections.AtendimentosDadosLineProjection;
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
        Optional<FuncionarioModel> funcionarioOptional = funcionarioRepository.findById(Integer.parseInt(atendimentoRecordDto.id_funcionario()));

        if (funcionarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        //Buscando o cliente
        Optional<ClienteModel> clienteOptional = clienteRepository.findById(Integer.parseInt(atendimentoRecordDto.id_cliente()));

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
    public ResponseEntity<Object> getOneService(@PathVariable(value = "id") int id) {
        Optional<AtendimentoModel> atendimentoO = atendimentoRepository.findById(id);
        if (atendimentoO.isEmpty()) { //Também poderia fazer a lógica inversa utilizando o product0.isPresent()
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoO.get());
    }

    @PutMapping("/atendimentos/{id}")
    public ResponseEntity<Object> updateService(@PathVariable(value = "id") int id, @RequestBody @Valid AtendimentoRecordDto atendimentoRecordDto){
        Optional<AtendimentoModel> atendimentoO = atendimentoRepository.findById(id);
        if (atendimentoO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }

        AtendimentoModel atendimentoModel = atendimentoO.get();
        BeanUtils.copyProperties(atendimentoRecordDto, atendimentoModel);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoRepository.save(atendimentoModel));
    }

    @DeleteMapping("/atendimentos/{id}")
    public ResponseEntity<Object> deleteService(@PathVariable(value = "id") int id) {
        Optional<AtendimentoModel> atendimentoO = atendimentoRepository.findById(id);

        if (atendimentoO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }

        atendimentoRepository.delete(atendimentoO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Service deleted successfully");
    }

    @GetMapping("/atendimentos/cliente/{id_cliente}")
    public ResponseEntity<Object> getManyServicesByIdCliente(@PathVariable(value = "id_cliente") int id_cliente, @RequestParam(value = "data") String data){
        List<AtendimentoModel> atendimentoModel = atendimentoRepository.findByIdCliente(id_cliente,data);
        if (atendimentoModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoModel);
    }

    @PutMapping("/atendimentos/updateLink")
    public ResponseEntity<Object> updateLinkRelatorio(@RequestParam(value = "link") String link, @RequestParam(value = "id") int id){
        int updated = atendimentoRepository.updateLinkRelatorio(link, id);
        if (updated == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Service updated successfully");
    }

    @GetMapping ("/atendimentos/visitasMes/{mes}/{ano}")
    public ResponseEntity<Integer> getVisitasMes(@PathVariable(value = "mes") int mes, @PathVariable(value = "ano") int ano){
        Integer totalVisitas = atendimentoRepository.visitasMes(mes, ano);
        if (totalVisitas == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }
        return ResponseEntity.status(HttpStatus.OK).body(totalVisitas);
    }

    @GetMapping("/atendimentos/novosClientes/{mes}/{ano}")
    public ResponseEntity<Integer> getNovosClientes(@PathVariable(value = "mes") int mes, @PathVariable(value = "ano") int ano){
        Integer totalNovosClientes = atendimentoRepository.novosClientes(mes, ano);
        if (totalNovosClientes == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }
        return ResponseEntity.status(HttpStatus.OK).body(totalNovosClientes);
    }

    @GetMapping("/atendimentos/getAtendMes/{mes}/{ano}")
    public ResponseEntity<List<AtendimentoSummaryProjection>> getAtendMes(@PathVariable(value = "mes") int mes, @PathVariable(value = "ano") int ano){
        List<AtendimentoSummaryProjection> atendimentos = atendimentoRepository.getAtendMes(mes, ano);
        if (atendimentos == null || atendimentos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(atendimentos);
    }

    @GetMapping("/atendimentos/getVisitasTri/{mes1}/{mes2}/{mes3}/{ano}")
    public ResponseEntity<List<AtendimentoTriProjection>> getVisitasTri(@PathVariable(value = "mes1") int mes1, @PathVariable(value = "mes2") int mes2, @PathVariable(value = "mes3") int mes3, @PathVariable(value = "ano") int ano){
        List<AtendimentoTriProjection> visitasTri = atendimentoRepository.getVisitasTri(mes1, mes2, mes3, ano);
        if (visitasTri == null || visitasTri.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(visitasTri);
    }

    @GetMapping("/atendimentos/visitasPorCli")
    public ResponseEntity<List<AtendimentoVisitasCliProjection>> visitasPorCli(){
        List<AtendimentoVisitasCliProjection> visitasPorCli = atendimentoRepository.visitasPorCli();
        if (visitasPorCli == null || visitasPorCli.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(visitasPorCli);
    }

    @GetMapping("/atendimentos/dadosLine/{mes}/{ano}")
    public ResponseEntity<AtendimentosDadosLineProjection> dadosLine(@PathVariable(value = "mes") int mes, @PathVariable(value = "ano") int ano){
        AtendimentosDadosLineProjection dadosLine = atendimentoRepository.dadosLine(mes, ano);
        if (dadosLine == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dadosLine);
    }

    @GetMapping("/atendimentos/orderByDataAtendimento")
    public ResponseEntity<List<AtendimentoModel>> findAllOrderByDataAtendimentoDesc(){
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoRepository.findAllOrderByDataAtendimentoDesc());
    }

    @GetMapping("/atendimentos/atendimentoPorNome/{nome}")
    public ResponseEntity<List<Object[]>> atendimentoPorNome(@PathVariable(value = "nome") String nome){
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoRepository.atendimentoPorNome(nome));
    }

}
