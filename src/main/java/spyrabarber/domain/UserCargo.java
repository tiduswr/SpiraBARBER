package spyrabarber.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "user_cargo")
public class UserCargo extends AbstractEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    @Column(name = "dt_admissao", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "A data de admissão é obrigatória")
    private LocalDate dtAdm;

    @Column(name = "dt_demissao", nullable = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dtDemissao;

    public UserCargo() {
    }

    public UserCargo(Cargo c){
        this.setCargo(c);
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
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
