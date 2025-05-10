package br.com.sistemacadastro.sistemacadastro.modules.admin.Controller;

import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Cargos;
import br.com.sistemacadastro.sistemacadastro.modules.admin.DTOs.CargosDto;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.CargosPorSetor;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Setores;
import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
@Controller
@RequestMapping("/cargo")
public class CargoController{

    @Autowired
    private CargoRepository repository;

    @Autowired
    private SetoresRepository setoresRepository;

    @Autowired
    private CargosPorSetorRepository cargosPorSetorRepository;

    @Autowired
    private ContratoRepository contratoRepository;


    @GetMapping("/cadastrar")
    public String showCadastrarPageCargo(Model model) {
        CargosDto cargosDto = new CargosDto();
        model.addAttribute("cargosDto", cargosDto);

        List<Setores> setores = setoresRepository.findAll(); // Buscar todos os setores
        model.addAttribute("setores", setores);

        return "adminpages/cadastroCargo"; // Retorna o template correto diretamente
    }

    @PostMapping("/cadastrar")
    public String cadastrarCargos(@Valid @ModelAttribute CargosDto cargosDto, BindingResult result, Model model) {
        Optional<Cargos> cargoss = repository.findByNomeCargo(cargosDto.getNomeCargo());
        if (cargoss.isEmpty()) {
            Cargos cargos = new Cargos();
            cargos.setNomeCargo(cargosDto.getNomeCargo());
            cargos.setCargaHorarioLimite(cargosDto.getCargoHorarioLimite());
            repository.save(cargos); // Salva o cargo primeiro

            // Agora cria a associação Cargo-Setor
            Setores setor = setoresRepository.findById(cargosDto.getSetorId());
            CargosPorSetor cargosPorSetor = new CargosPorSetor();
            cargosPorSetor.setCargo(cargos);
            cargosPorSetor.setSetor(setor);
            cargosPorSetorRepository.save(cargosPorSetor); // salva a associação

            return "redirect:/admin/setorcargo";

        } else {
            model.addAttribute("nomeJaCadastrado", true);
            return "adminpages/cadastroCargo";
        }
    }



    @GetMapping("/editar/{id}")
    public String showEditPageCargos(Model model, @PathVariable("id") int id) {
        try {
            // Busca o cargo
            Cargos cargos = repository.findById(id);  // Verifique se 'repo.findById(id)' retorna um cargo válido
            if (cargos == null) {
                return "redirect:/admin/setorcargo";  // Se o cargo não for encontrado, redireciona
            }

            model.addAttribute("cargos", cargos);  // Passando o objeto cargos

            // DTO para a edição
            CargosDto cargosDto = new CargosDto();
            cargosDto.setNomeCargo(cargos.getNomeCargo());
            cargosDto.setCargoHorarioLimite(cargos.getCargaHorarioLimite());

            // Buscando a associação com o setor através de CargosPorSetor
            CargosPorSetor cargosPorSetor = cargosPorSetorRepository.findByCargo(cargos);
            if (cargosPorSetor != null && cargosPorSetor.getSetor() != null) {
                cargosDto.setSetorId(cargosPorSetor.getSetor().getId());  // Definindo o ID do setor no DTO
            }

            model.addAttribute("cargosDto", cargosDto);  // Passando o DTO para o Thymeleaf

            // Buscando todos os setores para exibição no select
            List<Setores> setores = setoresRepository.findAll();
            model.addAttribute("setores", setores);  // Passando a lista de setores para o Thymeleaf

        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
            return "redirect:/admin/setorcargo";  // Caso aconteça algum erro, redireciona
        }

        return "adminpages/EditCargo";  // Retorna a página para edição
    }



    @PostMapping("/editar")
    public String updateCargo(Model model, @RequestParam int id, @Valid @ModelAttribute CargosDto cargosDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return "adminpages/EditCargo";  // Caso haja erros de validação, retorna para a edição
            }

            Cargos cargos = repository.findById(id);
            if (cargos == null) {
                return "redirect:/admin/setorcargo";  // Caso o cargo não exista
            }

            // Atualizando o cargo
            cargos.setNomeCargo(cargosDto.getNomeCargo());
            cargos.setCargaHorarioLimite(cargosDto.getCargoHorarioLimite());
            repository.save(cargos);  // Salva o cargo

            // Atualizando a relação na tabela CargosPorSetor
            CargosPorSetor cargosPorSetor = cargosPorSetorRepository.findByCargo(cargos);
            if (cargosPorSetor == null) {
                cargosPorSetor = new CargosPorSetor();  // Caso não exista, cria um novo registro
                cargosPorSetor.setCargo(cargos);
            }

            // Atualizando o setor associado
            Setores setor = setoresRepository.findById(cargosDto.getSetorId());
            if (setor != null) {
                cargosPorSetor.setSetor(setor);  // Associando o setor ao cargo
                cargosPorSetorRepository.save(cargosPorSetor);  // Salvando a relação
            }

        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }

        return "redirect:/admin/setorcargo";  // Redireciona para a página de lista de cargos
    }





    @GetMapping("/deletar")
    public String deleteCargo(@RequestParam int id) {
        try {
            // Buscar o cargo a ser excluído
            Cargos cargos = repository.findById(id);

            // Verificar se o cargo foi encontrado
            if (cargos == null) {
                System.out.println("Cargo não encontrado com ID: " + id);
                return "redirect:/admin/setorcargo?error=" + URLEncoder.encode("Cargo não encontrado", "UTF-8");
            }

            // Verificar se há contratos ativos associados ao cargo
            boolean hasActiveContracts = contratoRepository.existsByCargosAndAtivo(cargos, true);
            System.out.println("Verificando contratos ativos para o cargo ID " + id + ": " + hasActiveContracts);

            // Se houver contratos ativos, não permite a exclusão
            if (hasActiveContracts) {
                return "redirect:/admin/setorcargo?error=" + URLEncoder.encode("Não é possível excluir o cargo pois existe um colaborador com contrato ativo", "UTF-8");
            }

            // Verificar se o cargo tem alguma associação com setores
            CargosPorSetor cargosPorSetor = cargosPorSetorRepository.findByCargo(cargos);
            if (cargosPorSetor != null) {
                cargosPorSetorRepository.delete(cargosPorSetor); // Excluir a associação
                System.out.println("Associação Cargo-Setor removida.");
            }

            // Excluir o cargo
            repository.delete(cargos); // Exclui o cargo
            System.out.println("Cargo excluído com sucesso.");
        } catch (Exception ex) {
            System.out.println("Erro ao excluir cargo: " + ex.getMessage());
            ex.printStackTrace(); // Imprime o stack trace completo para ajudar na depuração

            try {
                return "redirect:/admin/setorcargo?error=" + URLEncoder.encode("Erro ao excluir cargo", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "redirect:/admin/setorcargo?error=Erro%20ao%20excluir%20cargo"; // Fallback para codificação padrão
            }
        }

        return "redirect:/admin/setorcargo";
}





}