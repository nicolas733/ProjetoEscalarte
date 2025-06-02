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
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private CargoRepository cargoRepository;

    public List<Cargos> listarCargos() {
        return cargoRepository.findAll();
    }

    public List<Colaborador> listarOperadores() {
        return colaboradorRepository.findByTipoUsuario(Colaborador.TipoUsuario.valueOf("OPERADOR"));
    }

    public Optional<Colaborador> buscarPorEmail(String email) {
        return colaboradorRepository.findByEmail(email);
    }

    public void salvarColaborador(ColaboradorDTO colaboradorDto) {
        // Validação de CPF duplicado
        if (colaboradorRepository.existsByCpf(colaboradorDto.getCpf())) {
            throw new IllegalArgumentException("Este CPF já está cadastrado");
        }

        // Validação de data de nascimento no futuro
        if (colaboradorDto.getDataNascimento().isAfter(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento não pode ser no futuro");
        }

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

        colaboradorRepository.save(colaborador);
    }

    public Colaborador buscarPorId(int id) {
        Colaborador colaborador = colaboradorRepository.findById(id);
        if (colaborador == null) {
            throw new RuntimeException("Colaborador não encontrado");
        }
        return colaborador;
    }


    public void atualizarColaborador(EditDTO editDto) {
        Colaborador colaborador = colaboradorRepository.findById(editDto.getId());

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

        colaboradorRepository.save(colaborador);
    }

//    public void deletarColaborador(int id) {
//        Colaborador colaborador = colaboradorRepository.findById(id);
//        colaboradorRepository.delete(colaborador);
//    }
//
//    public List<Colaborador> buscarPorSetorComContratoAtivo(Long setorId) {
//        return colaboradorRepository.findBySetor_IdAndContratoAtivoTrue(setorId);
//    }
}
