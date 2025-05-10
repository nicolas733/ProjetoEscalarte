package br.com.sistemacadastro.sistemacadastro.modules.operador.solicitacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SolicitacoesDto {

    @NotNull(message = "Informe a data de solicitação desejada")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataSolicitacao;

    @NotBlank(message = "Descrava o motivo da alteração")
    private String descricaoSolicitacao;

}
