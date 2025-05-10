package br.com.sistemacadastro.sistemacadastro.modules.admin.Controller;

import br.com.sistemacadastro.sistemacadastro.modules.admin.DTOs.EditDto;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Cargos;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Collaborator;
import br.com.sistemacadastro.sistemacadastro.modules.admin.DTOs.CollaboratorDto;
import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.CargoRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.CollaboratorRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Contrato;
import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.ContratoRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Endereco;
import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.EnderecoRepository;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/collaborator")
public class CollaboratorController {

    @Autowired
    private CollaboratorRepository repo;

    @Autowired
    private EnderecoRepository repository;

    @Autowired
    private ContratoRepository repoContrato;

    @Autowired
    private CargoRepository cargoRepository;


    @GetMapping("/cadastrar")
        public String showCadastrarPage(Model model) {
            CollaboratorDto collaboratorDto = new CollaboratorDto();
            List<Cargos> cargos = cargoRepository.findAll();
            model.addAttribute("cargos", cargos);
            model.addAttribute("collaboratorDto", collaboratorDto);
            return "adminpages/cadastroCo"; // Retorna o template correto diretamente
        }

    @PostMapping("/cadastrar")
    public String cadastrarColaborador(@Valid @ModelAttribute CollaboratorDto collaboratorDto, BindingResult result, Model model) {
        Optional<Collaborator> colaborador = this.repo.findByEmail(collaboratorDto.getEmail());

        if (colaborador.isEmpty()) {
            // Salva o endereço primeiro
            Endereco endereco = repository.save(collaboratorDto.getEndereco());

            // Cria e preenche o colaborador
            Collaborator collaborator = new Collaborator();
            collaborator.setNome(collaboratorDto.getNome());
            collaborator.setEmail(collaboratorDto.getEmail());
            collaborator.setSenha(collaboratorDto.getSenha());
            collaborator.setUserType(collaboratorDto.getUserType());
            collaborator.setTelefone(collaboratorDto.getTelefone());
            collaborator.setCpf(collaboratorDto.getCpf());
            collaborator.setDataNascimento(collaboratorDto.getDataNascimento());
            collaborator.setEndereco(endereco);

            // Cria e associa o contrato
            Contrato contrato = collaboratorDto.getContrato();
            contrato.setCollaborator(collaborator); // opcional, mantém consistência bidirecional
            collaborator.setContrato(contrato);     // // ESSENCIAL: lado proprietário

            Cargos cargo = cargoRepository.findById(collaboratorDto.getContrato().getCargos().getId());


            contrato.setCargos(cargo);
            // Salva o colaborador (salva também o contrato automaticamente se houver Cascade.PERSIST)
            repo.save(collaborator);

            return "redirect:/admin/main";
        } else {
            model.addAttribute("emailJaCadastrado", true); // Adiciona uma variável para a view
            return "adminpages/cadastroCo";
        }
    }

    @GetMapping("/editar/{id}")
    public String showEditPage(Model model, @PathVariable("id") int id) {
        try {
            Collaborator collaborator = repo.findById(id);  // Verifique se 'repo.findById(id)' retorna um colaborador válido.
            model.addAttribute("collaborator", collaborator);



            EditDto editDto = new EditDto();
            editDto.setId(collaborator.getId());
            editDto.setNome(collaborator.getNome());
            editDto.setEmail(collaborator.getEmail());
            editDto.setTelefone(collaborator.getTelefone());
            editDto.setCpf(collaborator.getCpf());
            editDto.setUserType(collaborator.getUserType());

            editDto.setEndereco(collaborator.getEndereco());
            editDto.setContrato(collaborator.getContrato());



            model.addAttribute("editDto", editDto);

        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
            return "redirect:/admin/main";
        }

        return "adminpages/EditCollaborator";
    }


    @PostMapping("/editar")
    public String updateCollaborator(Model model, @Valid @ModelAttribute EditDto editDto, BindingResult result) {
        try {
            Collaborator collaborator = repo.findById(editDto.getId());
            System.out.println(collaborator);
            model.addAttribute("collaborator", collaborator);
            model.addAttribute("editDto", editDto);

            if (result.hasErrors()) {
                return "adminpages/EditCollaborator";
            }
            collaborator.setNome(editDto.getNome());
            collaborator.setEmail(editDto.getEmail());
            collaborator.setTelefone(editDto.getTelefone());
            collaborator.setCpf(editDto.getCpf());
            collaborator.setUserType(editDto.getUserType());  // Esta linha é essencial para garantir que o tipo de usuário seja atualizado
            collaborator.getContrato().setAtivo(editDto.getContrato().isAtivo());

            System.out.println(collaborator);
            repo.save(collaborator);

        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
        return "redirect:/admin/main";
    }


    @GetMapping("/deletar")
        public String deleteCollaborator(@RequestParam int id) {
            try{
                Collaborator collaborator = repo.findById(id);
                repo.delete(collaborator);
            }catch (Exception ex) {
                System.out.println("Erro: " + ex.getMessage());
            }

            return "redirect:/admin/main";
        }

    }

