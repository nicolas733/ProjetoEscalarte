package br.com.sistemacadastro.sistemacadastro.endereco.model;

import br.com.sistemacadastro.sistemacadastro.collaborator.model.Collaborator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private int numero;

}
