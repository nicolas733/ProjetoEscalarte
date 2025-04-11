package br.com.sistemacadastro.sistemacadastro.modules.collaborator.controller;

import br.com.sistemacadastro.sistemacadastro.modules.collaborator.model.Collaborator;
import br.com.sistemacadastro.sistemacadastro.modules.collaborator.model.CollaboratorDto;
import br.com.sistemacadastro.sistemacadastro.modules.collaborator.repository.CollaboratorRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @Controller
    @RequestMapping("/collaborator")
    public class CollaboratorController {

        @Autowired
        private CollaboratorRepository repo;

        @GetMapping("/login")
        public String login() {
            return "login";
        }

        @PostMapping("/logar")
        public String logarCollaborator(Collaborator collaborator, Model model, HttpServletResponse response) {
            Collaborator collaboratorLogado = this.repo.login(collaborator.getEmail(), collaborator.getSenha());
            if (collaboratorLogado != null) {
                return "redirect:/admin/dashboard";
            }
            model.addAttribute("erro", "Usuario invalido");
            return "login";
        }



        @GetMapping("")
        public String home() {
            return "usuarios";
        }


        @GetMapping("/cadastrar")
        public String showCadastrarPage(Model model) {
            CollaboratorDto collaboratorDto = new CollaboratorDto();
            model.addAttribute("collaboratorDto", collaboratorDto);
            return "cadastroCo"; // Retorna o template correto diretamente
        }

        @PostMapping("/cadastrar")
        public String cadastrarColaborador(@Valid @ModelAttribute CollaboratorDto collaboratorDto, BindingResult result) {
            if (result.hasErrors()) {
                return "cadastroCo";
            }
            Collaborator collaborator = new Collaborator();
            collaborator.setNome(collaboratorDto.getNome());
            collaborator.setEmail(collaboratorDto.getEmail());
            collaborator.setSenha(collaboratorDto.getSenha());
            collaborator.setTypeuser(collaboratorDto.getTypeuser());
            collaborator.setTelefone(collaboratorDto.getTelefone());
            collaborator.setCpf(collaboratorDto.getCpf());
            collaborator.setDataNascimento(collaboratorDto.getDataNascimento());

            repo.save(collaborator);

            return "redirect:/admin/main";
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
                collaboratorDto.setTypeuser(collaborator.getTypeuser());
                collaboratorDto.setTelefone(collaborator.getTelefone());
                collaboratorDto.setCpf(collaborator.getCpf());
                collaboratorDto.setDataNascimento(collaborator.getDataNascimento());

                model.addAttribute("collaboratorDto", collaboratorDto);

            } catch (Exception ex) {
                System.out.println("Erro: " + ex.getMessage());
                return "redirect:/admin/main";
            }

            return "EditCollaborator";
        }


        @PostMapping("/edit")
        public String updateCollaborator(Model model, @RequestParam int id,@Valid @ModelAttribute CollaboratorDto collaboratorDto, BindingResult result) {
            try{
                Collaborator collaborator = repo.findById(id);
                System.out.println(collaborator);
                model.addAttribute("collaborator", collaborator);

                if (result.hasErrors()) {
                    return "EditCollaborator";
                }
                collaborator.setNome(collaboratorDto.getNome());
                collaborator.setEmail(collaboratorDto.getEmail());
                collaborator.setSenha(collaboratorDto.getSenha());
                collaborator.setTypeuser(collaboratorDto.getTypeuser());
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

