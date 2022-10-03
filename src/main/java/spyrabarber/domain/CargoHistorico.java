package spyrabarber.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "cargo_historico")
public class CargoHistorico extends AbstractEntity{

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Usuario user;

    @Column(name = "dt_admissao", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "A data de admissão é obrigatória")
    private LocalDate dtAdm;

    @Column(name = "dt_demissao", nullable = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dtDemissao;

    public CargoHistorico(){}

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

    public LocalDate getDtAdm() {
        return dtAdm;
    }

    public void setDtAdm(LocalDate dtAdm) {
        this.dtAdm = dtAdm;
    }

    public LocalDate getDtDemissao() {
        return dtDemissao;
    }

    public void setDtDemissao(LocalDate dtDemissao) {
        this.dtDemissao = dtDemissao;
    }

}
