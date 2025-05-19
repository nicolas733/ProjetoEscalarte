package br.com.sistemacadastro.sistemacadastro.controller;

import br.com.sistemacadastro.sistemacadastro.dto.ColaboradorDTO;
import br.com.sistemacadastro.sistemacadastro.dto.EditDTO;
import br.com.sistemacadastro.sistemacadastro.model.Cargos;
import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Contrato;
import br.com.sistemacadastro.sistemacadastro.model.Endereco;
import br.com.sistemacadastro.sistemacadastro.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/colaborador")
public class ColaboradorController {

    @Autowired
    private ColaboradorRepository repo;

    @Autowired
    private EnderecoRepository repository;

    @Autowired
    private ContratoRepository repoContrato;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private SolicitacaoRepository solicitacaoRepo;

    @GetMapping("/cadastrar")
    public String showCadastrarPage(Model model) {
        ColaboradorDTO colaboradorDto = new ColaboradorDTO();
        List<Cargos> cargos = cargoRepository.findAll();
        model.addAttribute("cargos", cargos);
        model.addAttribute("colaboradorDTO", colaboradorDto);
        return "adminpages/cadastroColaborador"; // Retorna o template correto diretamente
    }

    @PostMapping("/cadastrar")
    public String cadastrarColaborador(@Valid @ModelAttribute ColaboradorDTO colaboradorDto, BindingResult result, Model model) {
        Optional<Colaborador> colaboradorExistente = this.repo.findByEmail(colaboradorDto.getEmail());
        if (result.hasErrors()) {
            List<Cargos> cargos = cargoRepository.findAll();
            model.addAttribute("cargos", cargos);
            return "adminpages/cadastroColaborador";
        }
        if (colaboradorExistente.isEmpty()) {

            // Cria e preenche o colaborador
            Colaborador colaborador = new Colaborador();
            colaborador.setNome(colaboradorDto.getNome());
            colaborador.setEmail(colaboradorDto.getEmail());
            colaborador.setSenha(colaboradorDto.getSenha());
            colaborador.setTipoUsuario(colaboradorDto.getTipoUsuario());
            colaborador.setTelefone(colaboradorDto.getTelefone());
            colaborador.setCpf(colaboradorDto.getCpf());
            colaborador.setDataNascimento(colaboradorDto.getDataNascimento());

            Endereco endereco = repository.save(colaboradorDto.getEndereco());
            colaborador.setEndereco(endereco);


            // Cria e associa o contrato
            Contrato contrato = colaboradorDto.getContrato();
            contrato.setColaborador(colaborador);


            Cargos cargo = cargoRepository.findById(colaboradorDto.getCargoId()).orElseThrow(() -> new RuntimeException("Cargo não encontrado"));
            contrato.setCargos(cargo);

            colaborador.setContrato(contrato);
            // Salva o colaborador
            repo.save(colaborador);
            return "redirect:/admin/main?sucesso=true";
        } else {
            model.addAttribute("emailJaCadastrado", true);
            List<Cargos> cargos = cargoRepository.findAll();
            model.addAttribute("cargos", cargos);
            return "adminpages/cadastroColaborador";
        }
    }


    @GetMapping("/editar/{id}")
    public String mostrarPagEdicao(Model model, @PathVariable("id") int id) {
        try {
            Colaborador colaborador = repo.findById(id);  // Verifique se 'repo.findById(id)' retorna um colaborador válido.
            model.addAttribute("colaborador", colaborador);


            EditDTO editDto = new EditDTO();
            editDto.setId(colaborador.getId());
            editDto.setNome(colaborador.getNome());
            editDto.setEmail(colaborador.getEmail());
            editDto.setTelefone(colaborador.getTelefone());
            editDto.setCpf(colaborador.getCpf());
            editDto.setTipoUsuario(colaborador.getTipoUsuario());

            editDto.setEndereco(colaborador.getEndereco());
            editDto.setContrato(colaborador.getContrato());


            model.addAttribute("editDto", editDto);

        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
            return "redirect:/admin/main";
        }
        return "adminpages/EditColaborador";
    }


    @PostMapping("/editar")
    public String atualizarColaborador (Model model, @Valid @ModelAttribute EditDTO editDto, BindingResult result){
        try {
            Colaborador colaborador = repo.findById(editDto.getId());
            System.out.println(colaborador);
            model.addAttribute("colaborador", colaborador);
            model.addAttribute("editDto", editDto);

            if (result.hasErrors()) {
                return "adminpages/EditColaborador";
            }
            colaborador.setNome(editDto.getNome());
            colaborador.setEmail(editDto.getEmail());
            colaborador.setTelefone(editDto.getTelefone());
            colaborador.setCpf(editDto.getCpf());
            colaborador.setTipoUsuario(editDto.getTipoUsuario());// Esta linha é essencial para garantir que o tipo de usuário seja atualizado
            colaborador.getEndereco().setBairro(editDto.getEndereco().getBairro());
            colaborador.getEndereco().setRua(editDto.getEndereco().getRua());
            colaborador.getEndereco().setCep(editDto.getEndereco().getCep());

            colaborador.getEndereco().setNumero(editDto.getEndereco().getNumero());
            colaborador.getContrato().setAtivo(editDto.getContrato().isAtivo());

            System.out.println(colaborador);
            repo.save(colaborador);
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
        return "redirect:/admin/main?editado=true";
    }


    @GetMapping("/deletar")
    @Transactional
    public String excluirColaborador(@RequestParam int id) {
        try {
            Colaborador colaborador = repo.findById(id);
            if (colaborador != null) {
                solicitacaoRepo.deleteByColaborador(colaborador); // <- delete solicitações primeiro
                repo.delete(colaborador);
                System.out.println("Colaborador excluído com sucesso.");
            }
        } catch (Exception ex) {
            System.out.println("Erro ao excluir colaborador: " + ex.getMessage());
        }

        return "redirect:/admin/main?excluido=true";
    }



}
