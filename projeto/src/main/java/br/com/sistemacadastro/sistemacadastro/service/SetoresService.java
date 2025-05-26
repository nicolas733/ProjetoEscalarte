package br.com.sistemacadastro.sistemacadastro.service;

import br.com.sistemacadastro.sistemacadastro.model.Cargos;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import br.com.sistemacadastro.sistemacadastro.dto.SetoresDTO;
import br.com.sistemacadastro.sistemacadastro.model.Setores;
import br.com.sistemacadastro.sistemacadastro.repository.ContratoRepository;
import br.com.sistemacadastro.sistemacadastro.repository.SetoresRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SetoresService {

    @Autowired
    private SetoresRepository setoresRepository;

    @Autowired
    private ContratoRepository contratoRepository;


    public SetoresDTO prepararEdicao(int id) {
        Setores setores = setoresRepository.findById(id);
        SetoresDTO setoresDto = new SetoresDTO();
        setoresDto.setId(setores.getId());
        setoresDto.setNomeSetor(setores.getNomesetor());
        setoresDto.setGerenteSetor(setoresDto.getGerenteSetor());
        setoresDto.setQuantidadeColaboradores(setores.getQuantidadeColaboradores());
        return setoresDto;
    }

    public void editarSetor(int id, SetoresDTO setoresDto) {
        Setores setores = setoresRepository.findById(id);
        setores.setNomesetor(setoresDto.getNomeSetor());
        setores.setGerenteSetor(setoresDto.getGerenteSetor());
        setores.setQuantidadeColaboradores(setoresDto.getQuantidadeColaboradores());
        setoresRepository.save(setores);
    }

    @Transactional
    public boolean excluirSetor(int id) {
        Setores setor = setoresRepository.findById(id);

        if (!setor.getCargosPorSetor().isEmpty()) {
            return false;
        }

        setoresRepository.delete(setor);
        return true;
    }

    public Integer getQtdMinimaColaboradores(Long setorId) {
        Setores setor = setoresRepository.findById(setorId); // retorna diretamente
        if (setor == null) {
            throw new RuntimeException("Setor não encontrado");
        }
        return setor.getQuantidadeColaboradores();
    }

    public List<Setores> listarTodos() {
        return setoresRepository.findAll();
    }


}