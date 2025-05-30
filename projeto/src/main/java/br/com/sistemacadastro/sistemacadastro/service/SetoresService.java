package br.com.sistemacadastro.sistemacadastro.service;

import br.com.sistemacadastro.sistemacadastro.model.Cargos;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sistemacadastro.sistemacadastro.dto.SetoresDTO;
import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Setores;
import br.com.sistemacadastro.sistemacadastro.repository.ColaboradorRepository;
import br.com.sistemacadastro.sistemacadastro.repository.ContratoRepository;
import br.com.sistemacadastro.sistemacadastro.repository.SetoresRepository;

import java.util.List;

@Service
public class SetoresService {

    @Autowired
    private SetoresRepository setoresRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Transactional
    public void cadastrarSetor(SetoresDTO setoresDto) {
        if (setoresRepository.findByNomesetor(setoresDto.getNomeSetor()).isPresent()) {
            throw new RuntimeException("Setor com este nome já existe.");
        }

        Setores setores = new Setores();
        setores.setNomesetor(setoresDto.getNomeSetor());

        Colaborador gerente = setoresDto.getGerenteSetor();
        setores.setGerenteSetor(gerente);

        setoresRepository.save(setores);

        if (gerente != null) {
            Colaborador colaborador = colaboradorRepository.findById(gerente.getId());
            if (colaborador == null) {
                throw new RuntimeException("Colaborador para gerente não encontrado");
            }
            colaborador.setTipoUsuario(Colaborador.TipoUsuario.GERENTE);
            colaboradorRepository.save(colaborador);
        }
    }


    public SetoresDTO prepararEdicao(int id) {
        Setores setores = setoresRepository.findById(id);
        SetoresDTO setoresDto = new SetoresDTO();
        setoresDto.setId(setores.getId());
        setoresDto.setNomeSetor(setores.getNomesetor());
        setoresDto.setGerenteSetor(setores.getGerenteSetor());
        return setoresDto;
    }

    @Transactional
    public void editarSetor(int id, SetoresDTO setoresDto) {
        Setores setores = setoresRepository.findById(id);
        if (setores == null) {
            throw new RuntimeException("Setor não encontrado com id: " + id);
        }

        Colaborador antigoGerente = setores.getGerenteSetor();
        Colaborador novoGerente = setoresDto.getGerenteSetor();

        setores.setNomesetor(setoresDto.getNomeSetor());
        setores.setGerenteSetor(novoGerente);
        setoresRepository.save(setores);

        // Promove o novo gerente
        if (novoGerente != null) {
            Colaborador colaborador = colaboradorRepository.findById(novoGerente.getId());
            if (colaborador != null) {
                colaborador.setTipoUsuario(Colaborador.TipoUsuario.GERENTE);
                colaboradorRepository.save(colaborador);
            } else {
                throw new RuntimeException("Novo gerente não encontrado com id: " + novoGerente.getId());
            }
        }

        // Rebaixa o antigo gerente (se for diferente do novo)
        if (antigoGerente != null && (novoGerente == null || antigoGerente.getId() != novoGerente.getId())) {
            Colaborador colaboradorAntigo = colaboradorRepository.findById(antigoGerente.getId());
            if (colaboradorAntigo != null) {
                colaboradorAntigo.setTipoUsuario(Colaborador.TipoUsuario.valueOf("OPERADOR"));
                colaboradorRepository.save(colaboradorAntigo);
            }
        }
    }

    @Transactional
    public void excluirSetor(int id) {
        Setores setor = setoresRepository.findById(id);
        if (setor == null) {
            throw new RuntimeException("Setor não encontrado.");
        }

        if (!setor.getCargosPorSetor().isEmpty()) {
            throw new RuntimeException("Não é possível excluir o setor pois existem cargos associados.");
        }

        Colaborador gerente = setor.getGerenteSetor();
        if (gerente != null) {
            Colaborador colaboradorGerente = colaboradorRepository.findById(gerente.getId());
            if (colaboradorGerente != null) {
                colaboradorGerente.setTipoUsuario(Colaborador.TipoUsuario.OPERADOR);
                colaboradorRepository.save(colaboradorGerente);
            }
        }

        setoresRepository.delete(setor);
    }

    public List<Setores> listarTodos() {
        return setoresRepository.findAll();
    }


}
