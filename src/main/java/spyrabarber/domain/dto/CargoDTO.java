package spyrabarber.domain.dto;

import org.springframework.format.annotation.DateTimeFormat;
import spyrabarber.domain.Cargo;
import spyrabarber.domain.CargoHistorico;
import spyrabarber.domain.Usuario;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class CargoDTO {

    @NotNull(message = "É necessário informar um cargo")
    private Cargo cargo;

    @NotNull(message = "O usuário é obrigatório")
    private Usuario user;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "A data de admissao é obrigatório")
    private LocalDate dtAdmissao;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dtDemissao;

    private List<CargoHistorico> expirados;

    public CargoDTO() {
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public LocalDate getDtAdmissao() {
        return dtAdmissao;
    }

    public void setDtAdmissao(LocalDate dtAdmissao) {
        this.dtAdmissao = dtAdmissao;
    }

    public LocalDate getDtDemissao() {
        return dtDemissao;
    }

    public void setDtDemissao(LocalDate dtDemissao) {
        this.dtDemissao = dtDemissao;
    }

    public List<CargoHistorico> getExpirados() {
        return expirados;
    }

    public void setExpirados(List<CargoHistorico> expirados) {
        this.expirados = expirados;
    }

}
