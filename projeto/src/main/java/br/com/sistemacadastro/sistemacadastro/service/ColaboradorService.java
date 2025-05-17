package br.com.sistemacadastro.sistemacadastro.service;

import br.com.sistemacadastro.sistemacadastro.dto.ColaboradorDTO;
import br.com.sistemacadastro.sistemacadastro.dto.EditDTO;
import br.com.sistemacadastro.sistemacadastro.model.Cargos;
import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Contrato;
import br.com.sistemacadastro.sistemacadastro.model.Endereco;
import br.com.sistemacadastro.sistemacadastro.repository.CargoRepository;
import br.com.sistemacadastro.sistemacadastro.repository.ColaboradorRepository;
import br.com.sistemacadastro.sistemacadastro.repository.ContratoRepository;
import br.com.sistemacadastro.sistemacadastro.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColaboradorService {

    @Autowired
    private ColaboradorRepository repo;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private CargoRepository cargoRepository;

    public List<Cargos> listarCargos() {
        return cargoRepository.findAll();
    }

    public Optional<Colaborador> buscarPorEmail(String email) {
        return repo.findByEmail(email);
    }

    public void salvarColaborador(ColaboradorDTO colaboradorDto) {
        Colaborador colaborador = new Colaborador();
        colaborador.setNome(colaboradorDto.getNome());
        colaborador.setEmail(colaboradorDto.getEmail());
        colaborador.setSenha(colaboradorDto.getSenha());
        colaborador.setTipoUsuario(colaboradorDto.getTipoUsuario());
        colaborador.setTelefone(colaboradorDto.getTelefone());
        colaborador.setCpf(colaboradorDto.getCpf());
        colaborador.setDataNascimento(colaboradorDto.getDataNascimento());

        Endereco endereco = enderecoRepository.save(colaboradorDto.getEndereco());
        colaborador.setEndereco(endereco);

        Contrato contrato = colaboradorDto.getContrato();
        contrato.setColaborador(colaborador);

        Cargos cargo = cargoRepository.findById(colaboradorDto.getCargoId())
                .orElseThrow(() -> new RuntimeException("Cargo não encontrado"));
        contrato.setCargos(cargo);

        colaborador.setContrato(contrato);

        repo.save(colaborador);
    }

    public Colaborador buscarPorId(int id) {
        Colaborador colaborador = repo.findById(id);
        if (colaborador == null) {
            throw new RuntimeException("Colaborador não encontrado");
        }
        return colaborador;
    }


    public void atualizarColaborador(EditDTO editDto) {
        Colaborador colaborador = repo.findById(editDto.getId());

        colaborador.setNome(editDto.getNome());
        colaborador.setEmail(editDto.getEmail());
        colaborador.setTelefone(editDto.getTelefone());
        colaborador.setCpf(editDto.getCpf());
        colaborador.setTipoUsuario(editDto.getTipoUsuario());

        colaborador.getEndereco().setBairro(editDto.getEndereco().getBairro());
        colaborador.getEndereco().setRua(editDto.getEndereco().getRua());
        colaborador.getEndereco().setCep(editDto.getEndereco().getCep());
        colaborador.getEndereco().setNumero(editDto.getEndereco().getNumero());

        colaborador.getContrato().setAtivo(editDto.getContrato().isAtivo());

        repo.save(colaborador);
    }

    public void deletarColaborador(int id) {
        Colaborador colaborador = repo.findById(id);
        repo.delete(colaborador);
    }
}
