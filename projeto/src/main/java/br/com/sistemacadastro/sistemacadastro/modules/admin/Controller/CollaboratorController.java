package br.com.sistemacadastro.sistemacadastro.modules.admin.Controller;

import br.com.sistemacadastro.sistemacadastro.modules.admin.DTOs.ResponseDto;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Collaborator;
import br.com.sistemacadastro.sistemacadastro.modules.admin.DTOs.CollaboratorDto;
import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.CollaboratorRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Contrato;
import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.ContratoRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Endereco;
import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.EnderecoRepository;
import br.com.sistemacadastro.sistemacadastro.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private  PasswordEncoder encoder;
    @Autowired
    private  TokenService tokenService;



    @GetMapping("/cadastrar")
        public String showCadastrarPage(Model model) {
            CollaboratorDto collaboratorDto = new CollaboratorDto();
            model.addAttribute("collaboratorDto", collaboratorDto);
            return "adminpages/cadastroCo"; // Retorna o template correto diretamente
        }

        @PostMapping("/cadastrar")
        public String cadastrarColaborador(@Valid @ModelAttribute CollaboratorDto collaboratorDto, BindingResult result) {
            Optional<Collaborator> colaborador = this.repo.findByEmail(collaboratorDto.getEmail());
            if(colaborador.isEmpty()){
                Endereco endereco = repository.save(collaboratorDto.getEndereco());

                Collaborator collaborator = new Collaborator();
                collaborator.setNome(collaboratorDto.getNome());
                collaborator.setEmail(collaboratorDto.getEmail());
                collaborator.setSenha(passwordEncoder.encode(collaboratorDto.getSenha()));
                collaborator.setUserType(Collaborator.UserType.OPERADOR);
                collaborator.setTelefone(collaboratorDto.getTelefone());
                collaborator.setCpf(collaboratorDto.getCpf());
                collaborator.setDataNascimento(collaboratorDto.getDataNascimento());
                collaborator.setEndereco(endereco);

                //salva os dados do collaborador cadastrados
                Collaborator colaboradorSalvo = repo.save(collaborator);
                String token = this.tokenService.generateToken(colaboradorSalvo);
                ResponseEntity.ok(new ResponseDto(colaboradorSalvo.getNome(), token));

                //Associa o contrato com o colaborador salvo acima
                Contrato contrato = collaboratorDto.getContrato();
                contrato.setCollaborator(colaboradorSalvo);
                contrato.setAtivo(true);

                repoContrato.save(contrato);

                return "redirect:/admin/main";
            }else {
                return "adminpages/cadastroCo";
            }

        }

        @GetMapping("/edit/{id}")
        public String showEditPage(Model model, @PathVariable("id") int id) {
            try {
                Collaborator collaborator = repo.findById(id);  // Verifique se 'repo.findById(id)' retorna um colaborador válido.
                model.addAttribute("collaborator", collaborator);

                CollaboratorDto collaboratorDto = new CollaboratorDto();
                collaboratorDto.setNome(collaborator.getNome());
                collaboratorDto.setEmail(collaborator.getEmail());
                collaboratorDto.setSenha(collaborator.getSenha());
                collaboratorDto.setTelefone(collaborator.getTelefone());
                collaboratorDto.setCpf(collaborator.getCpf());
                collaboratorDto.setDataNascimento(collaborator.getDataNascimento());

                model.addAttribute("collaboratorDto", collaboratorDto);

            } catch (Exception ex) {
                System.out.println("Erro: " + ex.getMessage());
                return "redirect:/admin/main";
            }

            return "adminpages/EditCollaborator";
        }


        @PostMapping("/edit")
        public String updateCollaborator(Model model, @RequestParam int id,@Valid @ModelAttribute CollaboratorDto collaboratorDto, BindingResult result) {
            try{
                Collaborator collaborator = repo.findById(id);
                System.out.println(collaborator);
                model.addAttribute("collaborator", collaborator);

                if (result.hasErrors()) {
                    return "adminpages/EditCollaborator";
                }
                collaborator.setNome(collaboratorDto.getNome());
                collaborator.setEmail(collaboratorDto.getEmail());
                collaborator.setSenha(collaboratorDto.getSenha());
                collaborator.setTelefone(collaboratorDto.getTelefone());
                collaborator.setCpf(collaboratorDto.getCpf());
                collaborator.setDataNascimento(collaboratorDto.getDataNascimento());
                System.out.println(collaborator);
                repo.save(collaborator);

            }catch (Exception ex) {
                System.out.println("Erro: " + ex.getMessage());
            }
            return "redirect:/admin/main";
        }

        @GetMapping("/delete")
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

