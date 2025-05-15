package br.com.sistemacadastro.sistemacadastro.modules.admin.endereco;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity(name = "endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "O bairro deve ser obrigatorio")
    private String bairro;

    @NotBlank(message = "A rua deve ser obrigatoria")
    private String rua;

    @NotBlank(message = "O cep deve ser obrigatorio")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "O CEP deve estar no formato 99999-999")
    private String cep;

    private String complemento;

    @NotNull(message = "É obrigatorio o numero")
    @Min(value = 1, message = "O número deve ser maior que zero")
    private Integer numero;

}
