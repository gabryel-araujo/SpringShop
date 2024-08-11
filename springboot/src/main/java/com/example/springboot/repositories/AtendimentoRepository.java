package com.example.springboot.repositories;

import com.example.springboot.dtos.AtendimentoSumaryRecordDto;
import com.example.springboot.models.AtendimentoModel;
import com.example.springboot.projections.AtendimentoSummaryProjection;
import com.example.springboot.projections.AtendimentoTriProjection;
import com.example.springboot.projections.AtendimentoVisitasCliProjection;
import com.example.springboot.projections.AtendimentosDadosLineProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtendimentoRepository extends JpaRepository<AtendimentoModel, Integer> {

    @Query("SELECT a FROM AtendimentoModel a WHERE a.cliente.id = :id_cliente AND a.data_atendimento like :data")
    List<AtendimentoModel> findByIdCliente(int id_cliente, String data);

    @Modifying
    @Transactional
    @Query("UPDATE AtendimentoModel a SET a.link_relatorio = :link WHERE a.link_relatorio = 'null' AND a.id = :id")
    int updateLinkRelatorio(@Param("link") String link, @Param("id") int id);

    @Query(value = "SELECT * FROM visitasMes(:mes, :ano)", nativeQuery = true)
    Integer visitasMes(@Param("mes") int mes, @Param("ano") int ano);

    @Query(value = "SELECT * FROM novosClientes(:mes, :ano)", nativeQuery = true)
    Integer novosClientes(@Param("mes") int mes, @Param("ano") int ano);

    @Query(value = "SELECT * FROM getAtendMes(:mes, :ano)", nativeQuery = true)
    List<AtendimentoSummaryProjection> getAtendMes(@Param("mes") int mes, @Param("ano") int ano);

    @Query(value = "SELECT * FROM getVisitasTri(:mes1, :mes2, :mes3, :ano)", nativeQuery = true)
    List<AtendimentoTriProjection> getVisitasTri(@Param("mes1") int mes1, @Param("mes2") int mes2, @Param("mes3") int mes3, @Param("ano") int ano);

    @Query(value = "SELECT * FROM visitasPorCli()", nativeQuery = true)
    List<AtendimentoVisitasCliProjection> visitasPorCli();

    @Query(value = "SELECT * FROM dadosLine(:mes, :ano)", nativeQuery = true)
    AtendimentosDadosLineProjection dadosLine(@Param("mes") int mes, @Param("ano") int ano);

    @Query("SELECT a FROM AtendimentoModel a order by a.data_atendimento desc")
    List<AtendimentoModel> findAllOrderByDataAtendimentoDesc();

    @Query(value = "SELECT A.id AS atendimento_id, A.checkin, A.data_atendimento, A.link_relatorio, A.id_funcionario, A.id_cliente, F.id AS funcionario_id, F.nome, F.email, F.tipo, F.cargo, F.visitas, F.status FROM ATENDIMENTOS A JOIN FUNCIONARIO F ON F.id = A.id_funcionario where F.nome like :nome", nativeQuery = true)
    List<Object[]> atendimentoPorNome(@Param("nome") String nome);

}
