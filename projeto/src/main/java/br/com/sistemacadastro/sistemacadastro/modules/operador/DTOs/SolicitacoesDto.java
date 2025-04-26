package br.com.sistemacadastro.sistemacadastro.modules.operador.DTOs;

import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Collaborator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
public class SolicitacoesDto {

    @NotNull(message = "Informe a data de solicitação desejada")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataSolicitacao;

    @NotBlank(message = "Descrava o motivo da alteração")
    private String descricaoSolicitacao;

}
