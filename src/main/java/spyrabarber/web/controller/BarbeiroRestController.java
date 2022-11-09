package spyrabarber.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import spyrabarber.domain.Servico;
import spyrabarber.domain.projections.SimpleServicoProjection;
import spyrabarber.service.ServicosService;

import java.util.List;

@RestController
@RequestMapping("/api/barbers")
public class BarbeiroRestController {

    @Autowired
    private ServicosService servicosService;

    @GetMapping("/servicos/byid/{id}")
    public List<Servico> servicosByUserId(@PathVariable Long id) throws ResponseStatusException {
        return servicosService.getServicosByUserID(id);
    }

    @GetMapping("/servicos/simple_projection/bykeyword/{keyword}")
    public List<SimpleServicoProjection> getServicosByKeyword(@PathVariable("keyword") String keyword){
        return servicosService.getSimpleServicoProjectionByKeyword(keyword);
    }

}
