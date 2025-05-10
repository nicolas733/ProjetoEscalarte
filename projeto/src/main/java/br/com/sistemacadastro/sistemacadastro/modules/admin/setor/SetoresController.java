package br.com.sistemacadastro.sistemacadastro.modules.admin.setor;

import br.com.sistemacadastro.sistemacadastro.modules.admin.contrato.ContratoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/setores")
public class SetoresController {

    @Autowired
    private SetoresRepository repo;

    @Autowired
    private ContratoRepository contratoRepository;


    @GetMapping("")
    public String home() {
        return "setores";
    }

    @GetMapping("/cadastrar")
    public String showCadastrarPage(Model model) {
        SetoresDto setoresDto = new SetoresDto();
        model.addAttribute("setoresDto", setoresDto);
        return "adminpages/cadastroSe"; // Retorna o template correto diretamente
    }

    @PostMapping("/cadastrar")
    public String cadastrarSetores(@Valid @ModelAttribute SetoresDto setoresDto, BindingResult result, Model model) {
        Optional<Setores> setoress = repo.findByNomesetor(setoresDto.getNomeSetor());
        if (setoress.isEmpty()) {
            Setores setores = new Setores();
            setores.setNomesetor(setoresDto.getNomeSetor());
            setores.setQuantidadeColaboradores(setoresDto.getQuantidadeColaboradores());
            repo.save(setores);

            return "redirect:/admin/setorcargo";
        } else {
            model.addAttribute("nomeJaCadastrado", true);
            return "adminpages/cadastroSe";

        }
    }



    @GetMapping("/editar/{id}")
    public String showEditPage(Model model, @PathVariable("id") int id) {
        try {
            Setores setores = repo.findById(id);  // Verifique se 'repo.findById(id)' retorna um colaborador válido.
            model.addAttribute("setores", setores);

            SetoresDto setoresDto = new SetoresDto();
            setoresDto.setNomeSetor(setores.getNomesetor());
            setoresDto.setQuantidadeColaboradores(setores.getQuantidadeColaboradores());

            model.addAttribute("setoresDto", setoresDto);
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
            return "redirect:/admin/setorcargo";
        }

        return "adminpages/EditSetores";
    }


    @PostMapping("/editar")
    public String updateCollaborator(Model model, @RequestParam int id,@Valid @ModelAttribute SetoresDto setoresDto, BindingResult result) {
        try{
            Setores setores = repo.findById(id);
            System.out.println(setores);
            model.addAttribute("setores", setores);

            if (result.hasErrors()) {
                return "adminpages/EditSetores";
            }
            setores.setNomesetor(setoresDto.getNomeSetor());
            setores.setQuantidadeColaboradores(setoresDto.getQuantidadeColaboradores());
            System.out.println(setores);
            repo.save(setores);

        }catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
        return "redirect:/admin/setorcargo";
    }

    @Transactional
    @GetMapping("/deletar")
    public String deleteSetores(@RequestParam int id) {
        try {
            Setores setor = repo.findById(id);

            // Verificar se o setor tem cargos associados
            if (!setor.getCargosPorSetor().isEmpty()) {
                return "redirect:/admin/setorcargo?error=Não é possivel excluir pois esse setor tem cargos associados";
            }

            // Se não houver cargos associados, pode-se excluir o setor
            repo.delete(setor);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "redirect:/admin/setorcargo?error=Erro ao excluir o setor: " + ex.getMessage();
        }
        return "redirect:/admin/setorcargo";
    }


}


