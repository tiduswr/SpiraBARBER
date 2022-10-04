package spyrabarber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import spyrabarber.domain.*;
import spyrabarber.domain.dto.CargoDTO;
import spyrabarber.repository.CargoHistoricoRepository;
import spyrabarber.repository.CargoRepository;
import spyrabarber.repository.UserCargoRepository;
import spyrabarber.repository.UsuarioRepository;
import spyrabarber.web.exception.UsuarioNotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
public class CargoService {

    @Autowired
    private UsuarioRepository userRepo;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private UserCargoRepository userCargoRepository;

    @Autowired
    private CargoHistoricoRepository cargoHistoricoRepository;

    @Transactional(readOnly = true)
    public List<Cargo> buscarTodosOsCargos(){
        return cargoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CargoDTO montarCargoDTOByUser(Long userid) throws UsuarioNotFoundException{
        Usuario user = userRepo.findById(userid)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario não encontrado na base de dados"));
        CargoDTO cargoDTO = new CargoDTO();
        cargoDTO.setUser(user);

        List<CargoHistorico> expirados = cargoHistoricoRepository.findAllByUserId(userid);
        UserCargo u = user.getUserCargo();
        if(u == null) u = new UserCargo();

        cargoDTO.setCargo(u.getCargo());
        cargoDTO.setDtAdmissao(u.getDtAdm());
        cargoDTO.setDtDemissao(u.getDtDemissao());
        cargoDTO.setExpirados(expirados);

        return cargoDTO;
    }

    @Transactional(readOnly = false)
    public boolean atualizarCargo(CargoDTO c, BindingResult errors) {

        if(!checkDates(c, errors)) return false;

        Usuario user = userRepo.findById(c.getUser().getId())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario não encontrado na base de dados"));
        UserCargo u = user.getUserCargo();

        if(u == null) u = new UserCargo(c.getCargo());

        user.setUserCargo(u);
        u.setCargo(c.getCargo());
        u.setDtDemissao(c.getDtDemissao());
        u.setDtAdm(c.getDtAdmissao());

        if(u.getDtDemissao() != null){
            CargoHistorico ch = new CargoHistorico();
            ch.setCargo(u.getCargo());
            ch.setDtAdm(u.getDtAdm());
            ch.setDtDemissao(u.getDtDemissao());
            ch.setUser(user);

            cargoHistoricoRepository.save(ch);
            user.setUserCargo(null);
            userRepo.save(user);
            userCargoRepository.delete(u);
            return true;
        }

        userCargoRepository.save(u);
        userRepo.save(user);
        return true;
    }

    private boolean checkDates(CargoDTO c, BindingResult errors){
        LocalDate dtAdm = c.getDtAdmissao();
        LocalDate dtDem = c.getDtDemissao();
        List<CargoHistorico> historico = cargoHistoricoRepository.findAllByUserId(c.getUser().getId());

        LocalDate maxDemDate = historico
                .stream()
                .map(CargoHistorico::getDtDemissao)
                .max(LocalDate::compareTo)
                .orElse(null);

        if(maxDemDate != null && !dtAdm.isAfter(maxDemDate)){
            errors.addError(new FieldError("dtAdmissao", "dtAdmissao",
                    "A data de admissão precisa ser posterior a ultima data de demissão."));
            return false;
        }
        if(dtDem != null && !dtDem.isAfter(dtAdm)){
            errors.addError(new FieldError("dtDemissao", "dtDemissao",
                    "A data de demissão precisa ser posterior a data de admissão."));
            return false;
        }
        if(dtAdm.isAfter(LocalDate.now())){
            errors.addError(new FieldError("dtAdmissao", "dtAdmissao",
                    "A data de admissão precisa ser posterior a data de hoje."));
            return false;
        }
        if(dtDem != null && dtDem.isAfter(LocalDate.now())){
            errors.addError(new FieldError("dtDemissao", "dtDemissao",
                    "A data de demissão precisa ser posterior a data de hoje."));
            return false;
        }

        return true;
    }

}
