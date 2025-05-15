package br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador;

import br.com.sistemacadastro.sistemacadastro.modules.admin.cargo.CargoRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.cargo.Cargos;
import br.com.sistemacadastro.sistemacadastro.modules.admin.contrato.Contrato;
import br.com.sistemacadastro.sistemacadastro.modules.admin.contrato.ContratoRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.endereco.Endereco;
import br.com.sistemacadastro.sistemacadastro.modules.admin.endereco.EnderecoRepository;

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


    @GetMapping("/cadastrar")
        public String showCadastrarPage(Model model) {
            ColaboradorDto colaboradorDto = new ColaboradorDto();
            List<Cargos> cargos = cargoRepository.findAll();
            model.addAttribute("cargos", cargos);
            model.addAttribute("colaboradorDto", colaboradorDto);
            return "adminpages/cadastroColaborador"; // Retorna o template correto diretamente
        }

    @PostMapping("/cadastrar")
    public String cadastrarColaborador(@Valid @ModelAttribute ColaboradorDto colaboradorDto, BindingResult result, Model model) {
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

            return "redirect:/admin/main";
        } else {
            model.addAttribute("emailJaCadastrado", true); // Adiciona uma variável para a view
            return "adminpages/cadastroColaborador";
        }
    }



    @GetMapping("/editar/{id}")
    public String mostrarPagEdicao(Model model, @PathVariable("id") int id) {
        try {
            Colaborador colaborador = repo.findById(id);  // Verifique se 'repo.findById(id)' retorna um colaborador válido.
            model.addAttribute("colaborador", colaborador);



            EditDto editDto = new EditDto();
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
    public String atualizarColaborador(Model model, @Valid @ModelAttribute EditDto editDto, BindingResult result) {
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
        return "redirect:/admin/main";
    }


    @GetMapping("/deletar")
        public String excluirColaborador(@RequestParam int id) {
            try{
                Colaborador colaborador = repo.findById(id);
                repo.delete(colaborador);
            }catch (Exception ex) {
                System.out.println("Erro: " + ex.getMessage());
            }

            return "redirect:/admin/main";
        }

    }

