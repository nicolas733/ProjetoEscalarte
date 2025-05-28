package br.com.sistemacadastro.sistemacadastro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SolicitacoesDTO {

    @NotNull(message = "Informe a data de solicitação desejada")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataSolicitacao;

    @NotBlank(message = "Descreva o motivo da alteração")
    @Size(min = 8, message = "A descrição deve ter no minimo 8 caracteres e no maximo 100", max = 100)
    private String descricaoSolicitacao;

    @AssertTrue(message = "A data da solicitação deve ser a data de hoje.")
    public boolean isDataSolicitacaoValida() {
        return dataSolicitacao != null && dataSolicitacao.equals(LocalDate.now());
    }
}
