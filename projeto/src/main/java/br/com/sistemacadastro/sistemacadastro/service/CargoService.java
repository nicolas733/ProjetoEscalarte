package br.com.sistemacadastro.sistemacadastro.service;

import br.com.sistemacadastro.sistemacadastro.dto.CargosDTO;
import br.com.sistemacadastro.sistemacadastro.model.Cargos;
import br.com.sistemacadastro.sistemacadastro.model.CargosPorSetor;
import br.com.sistemacadastro.sistemacadastro.model.Setores;
import br.com.sistemacadastro.sistemacadastro.repository.CargoRepository;
import br.com.sistemacadastro.sistemacadastro.repository.CargosPorSetorRepository;
import br.com.sistemacadastro.sistemacadastro.repository.ContratoRepository;
import br.com.sistemacadastro.sistemacadastro.repository.SetoresRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
public class CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private SetoresRepository setoresRepository;

    @Autowired
    private CargosPorSetorRepository cargosPorSetorRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    public void cadastrarCargo(CargosDTO cargosDto) {

        Cargos cargos = new Cargos();
        cargos.setNomeCargo(cargosDto.getNomeCargo());
        cargos.setCargaHorarioLimite(cargosDto.getCargoHorarioLimite());
        cargos.setIntervaloInterjornada(cargosDto.getIntervaloInterjornada());
        cargoRepository.save(cargos);

        Setores setor = setoresRepository.findById(cargosDto.getSetorId())
                .orElseThrow(() -> new RuntimeException("Setor não encontrado"));
        CargosPorSetor cps = new CargosPorSetor();
        cps.setCargo(cargos);
        cps.setSetor(setor);
        cargosPorSetorRepository.save(cps);
    }

    public String prepararEdicao(int id, Model model) {
        Cargos cargos = cargoRepository.findById(id);
        if (cargos == null) {
            return "redirect:/admin/setorcargo";
        }

        CargosDTO cargosDto = new CargosDTO();
        cargosDto.setId(cargos.getId());
        cargosDto.setNomeCargo(cargos.getNomeCargo());
        cargosDto.setCargoHorarioLimite(cargos.getCargaHorarioLimite());
        cargosDto.setIntervaloInterjornada(cargos.getIntervaloInterjornada());

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

    public void editarCargo(int id, CargosDTO dto) {
        Cargos cargos = cargoRepository.findById(id);
        if (cargos == null) return;

        cargos.setNomeCargo(dto.getNomeCargo());
        cargos.setCargaHorarioLimite(dto.getCargoHorarioLimite());
        cargos.setIntervaloInterjornada(dto.getIntervaloInterjornada());
        cargoRepository.save(cargos);

        CargosPorSetor cps = cargosPorSetorRepository.findByCargo(cargos);
        if (cps == null) {
            cps = new CargosPorSetor();
            cps.setCargo(cargos);
        }

        Setores setor = setoresRepository.findById(dto.getSetorId())
                .orElseThrow(() -> new RuntimeException("Setor não encontrado"));
        if (setor != null) {
            cps.setSetor(setor);
            cargosPorSetorRepository.save(cps);
        }
    }

    public void excluirCargo(int id) {
        Cargos cargos = cargoRepository.findById(id);
        if (cargos == null) return;

        boolean hasContracts = contratoRepository.existsByCargosAndAtivo(cargos, true);
        if (hasContracts) {
            throw new IllegalStateException("Não é possível excluir o cargo pois existe um colaborador com contrato ativo");
        }

        CargosPorSetor cps = cargosPorSetorRepository.findByCargo(cargos);
        if (cps != null) {
            cargosPorSetorRepository.delete(cps);
        }

        cargoRepository.delete(cargos);
    }
}
