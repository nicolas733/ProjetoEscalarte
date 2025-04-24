package br.com.sistemacadastro.sistemacadastro.modules.operador.DTOs;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class SolicitacoesDto {

    @NotNull(message = "Informe a data de solicitação desejada")
    @Temporal(TemporalType.DATE)
    private Date dataSolicitacao;

    @NotBlank(message = "Descrava o motivo da alteração")
    private String descricaoSolicitacao;
}
