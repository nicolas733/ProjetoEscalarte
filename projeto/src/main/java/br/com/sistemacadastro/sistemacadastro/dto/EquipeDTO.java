package br.com.sistemacadastro.sistemacadastro.dto;

import lombok.Data;

@Data
public class EquipeDTO {
    private int id;
    private String nome;
    private String email;
    private String cpf;
    private String nomeCargo;
    private String nomeSetor;
    private String tipoUsuario;
}
