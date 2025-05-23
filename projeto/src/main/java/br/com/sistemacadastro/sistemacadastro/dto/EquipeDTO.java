package br.com.sistemacadastro.sistemacadastro.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EquipeDTO {
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private String nomeCargo;
    private String nomeSetor;
    private String tipoUsuario;

}
