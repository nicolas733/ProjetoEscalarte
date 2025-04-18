package br.com.sistemacadastro.sistemacadastro.modules.endereco.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity(name = "endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String bairro;

    @NotBlank
    private String rua;

    @NotBlank
    private String cep;

    @NotBlank
    private String complemento;

    @NotNull
    private int numero;

}
