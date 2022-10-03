package spyrabarber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spyrabarber.repository.PessoaRepository;

@Service
public class ClienteService {

    @Autowired
    private PessoaRepository clienteRepository;

}
