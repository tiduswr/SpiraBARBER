package spyrabarber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spyrabarber.repository.CargoHistoricoRepository;

@Service
public class CargoHistoricoService {

    @Autowired
    private CargoHistoricoRepository cargoHistoricoRepository;

}
