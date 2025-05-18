package br.com.sistemacadastro.sistemacadastro.service;

import br.com.sistemacadastro.sistemacadastro.dto.EquipeDTO;
import br.com.sistemacadastro.sistemacadastro.dto.PasswordChangeDTO;
import br.com.sistemacadastro.sistemacadastro.model.Cargos;
import br.com.sistemacadastro.sistemacadastro.model.CargosPorSetor;
import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Solicitacoes;
import br.com.sistemacadastro.sistemacadastro.repository.CargosPorSetorRepository;
import br.com.sistemacadastro.sistemacadastro.repository.ColaboradorRepository;
import br.com.sistemacadastro.sistemacadastro.repository.GerenteRepository;
import br.com.sistemacadastro.sistemacadastro.repository.SolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GerenteService {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private CargosPorSetorRepository cargosPorSetorRepository;

    @Autowired
    private GerenteRepository gerenteRepository;

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    public List<Colaborador> listarColaboradoresPorSetorGerente(Integer gerenteId) {
        return gerenteRepository.findColaboradoresPorSetorDoGerente(gerenteId);
    }


    public Colaborador buscarColaboradorPorId(Long id) {
        return colaboradorRepository.findById(id);
    }

    public List<Colaborador> listarColaboradores() {
        return colaboradorRepository.findAll();
    }

    public List<Solicitacoes> listarSolicitacoes() {
        return solicitacaoRepository.findAll();
    }

    public Colaborador buscarPorEmail(String email) {
        return colaboradorRepository.findCollaboratorByEmail(email);
    }

    public boolean alterarSenha(PasswordChangeDTO dto) {
        Colaborador colaborador = colaboradorRepository.findCollaboratorByEmail(dto.getEmail());
        if (colaborador != null && colaborador.getSenha().equals(dto.getSenha())) {
            colaborador.setSenha(dto.getNovaSenha());
            colaboradorRepository.save(colaborador);
            return true;
        }
        return false;
    }

    public String obterIniciais(String nomeCompleto) {
        String[] partes = nomeCompleto.trim().split(" ");
        if (partes.length >= 2) {
            return partes[0].substring(0, 1).toUpperCase() + partes[1].substring(0, 1).toUpperCase();
        } else if (partes.length == 1) {
            return partes[0].substring(0, 1).toUpperCase();
        }
        return "";
    }

    public EquipeDTO toEquipeDTO(Colaborador colaborador) {
        EquipeDTO dto = new EquipeDTO();
        dto.setId(colaborador.getId());
        dto.setNome(colaborador.getNome());
        dto.setEmail(colaborador.getEmail());

        if (colaborador.getContrato() != null && colaborador.getContrato().getCargos() != null) {
            Cargos cargo = colaborador.getContrato().getCargos();
            dto.setNomeCargo(cargo.getNomeCargo());

            // Buscar o CargosPorSetor usando o repositório
            CargosPorSetor cargoPorSetor = cargosPorSetorRepository.findByCargo(cargo);
            if (cargoPorSetor != null && cargoPorSetor.getSetor() != null) {
                dto.setNomeSetor(cargoPorSetor.getSetor().getNomesetor());
            } else {
                dto.setNomeSetor("Setor não atribuído");
            }
        } else {
            dto.setNomeCargo("Cargo não atribuído");
            dto.setNomeSetor("Setor não atribuído");
        }

        return dto;
    }

    public CargosPorSetor buscarCargoPorSetor(int cargoId) {
        return cargosPorSetorRepository.findByCargoId(cargoId);
    }
}