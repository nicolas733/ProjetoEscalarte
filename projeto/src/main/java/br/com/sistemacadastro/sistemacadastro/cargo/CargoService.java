package br.com.sistemacadastro.sistemacadastro.cargo;

import br.com.sistemacadastro.sistemacadastro.cargosPorSetor.CargosPorSetor;
import br.com.sistemacadastro.sistemacadastro.repository.CargoRepository;
import br.com.sistemacadastro.sistemacadastro.repository.CargosPorSetorRepository;
import br.com.sistemacadastro.sistemacadastro.repository.ContratoRepository;
import br.com.sistemacadastro.sistemacadastro.repository.SetoresRepository;
import br.com.sistemacadastro.sistemacadastro.setor.Setores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
public class CargoService {

    @Autowired
    private CargoRepository repository;

    @Autowired
    private SetoresRepository setoresRepository;

    @Autowired
    private CargosPorSetorRepository cargosPorSetorRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    public void cadastrarCargo(CargosDto cargosDto) {
        Optional<Cargos> existente = repository.findByNomeCargo(cargosDto.getNomeCargo());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Cargo já existe");
        }

        Cargos cargos = new Cargos();
        cargos.setNomeCargo(cargosDto.getNomeCargo());
        cargos.setCargaHorarioLimite(cargosDto.getCargoHorarioLimite());
        repository.save(cargos);

        Setores setor = setoresRepository.findById(cargosDto.getSetorId());
        CargosPorSetor cps = new CargosPorSetor();
        cps.setCargo(cargos);
        cps.setSetor(setor);
        cargosPorSetorRepository.save(cps);
    }

    public String prepararEdicao(int id, Model model) {
        Cargos cargos = repository.findById(id);
        if (cargos == null) {
            return "redirect:/admin/setorcargo";
        }

        CargosDto cargosDto = new CargosDto();
        cargosDto.setId(cargos.getId());
        cargosDto.setNomeCargo(cargos.getNomeCargo());
        cargosDto.setCargoHorarioLimite(cargos.getCargaHorarioLimite());

        CargosPorSetor cps = cargosPorSetorRepository.findByCargo(cargos);
        if (cps != null && cps.getSetor() != null) {
            cargosDto.setSetorId(cps.getSetor().getId());
        }

        List<Setores> setores = setoresRepository.findAll();

        model.addAttribute("cargos", cargos);
        model.addAttribute("cargosDto", cargosDto);
        model.addAttribute("setores", setores);

        return "adminpages/EditCargo";
    }

    public void editarCargo(int id, CargosDto dto) {
        Cargos cargos = repository.findById(id);
        if (cargos == null) return;

        cargos.setNomeCargo(dto.getNomeCargo());
        cargos.setCargaHorarioLimite(dto.getCargoHorarioLimite());
        repository.save(cargos);

        CargosPorSetor cps = cargosPorSetorRepository.findByCargo(cargos);
        if (cps == null) {
            cps = new CargosPorSetor();
            cps.setCargo(cargos);
        }

        Setores setor = setoresRepository.findById(dto.getSetorId());
        if (setor != null) {
            cps.setSetor(setor);
            cargosPorSetorRepository.save(cps);
        }
    }

    public void excluirCargo(int id) {
        Cargos cargos = repository.findById(id);
        if (cargos == null) return;

        boolean hasContracts = contratoRepository.existsByCargosAndAtivo(cargos, true);
        if (hasContracts) {
            throw new IllegalStateException("Não é possível excluir o cargo pois existe um colaborador com contrato ativo");
        }

        CargosPorSetor cps = cargosPorSetorRepository.findByCargo(cargos);
        if (cps != null) {
            cargosPorSetorRepository.delete(cps);
        }

        repository.delete(cargos);
    }
}
