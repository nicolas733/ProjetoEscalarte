package br.com.sistemacadastro.sistemacadastro.service;

import br.com.sistemacadastro.sistemacadastro.model.Turnos;
import br.com.sistemacadastro.sistemacadastro.repository.ContratoRepository;
import br.com.sistemacadastro.sistemacadastro.repository.TurnosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnoService {
    @Autowired
    private TurnosRepository turnosRepository;

//    public List<Turnos> buscarTurnosPorColaborador(Integer colaboradorId) {
//        return turnosRepository.findByColaboradorId(colaboradorId);
//    }
    public List<Turnos> buscarTodos() {
        return turnosRepository.findAll();
    }
}
