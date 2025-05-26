package br.com.sistemacadastro.sistemacadastro.service;

import br.com.sistemacadastro.sistemacadastro.model.Contrato;
import br.com.sistemacadastro.sistemacadastro.repository.ContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;

    public List<Contrato> buscarContratosAtivos() {
        return contratoRepository.findByAtivoTrue();
    }

}
