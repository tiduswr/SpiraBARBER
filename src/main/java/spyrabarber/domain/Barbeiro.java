package spyrabarber.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "barbeiros")
public class Barbeiro extends AbstractEntity{

    @NotEmpty(message = "O nome não pode estar vazio")
    @Column(name = "name", nullable = false)
    private String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "A data de nascimento é obrigatória")
    @Column(name = "dt_nascimento", nullable = false)
    private LocalDate dtNascimento;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "A data de admissão não pode estar vazia")
    @Column(name = "dt_admissao", nullable = false)
    private LocalDate dtAdmissao;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "dt_desligamento", nullable = true)
    private LocalDate dtDesligamento;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private Usuario user;

    public Barbeiro(){super();}
    public Barbeiro(Long id){super.setId(id);}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(LocalDate dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public LocalDate getDtAdmissao() {
        return dtAdmissao;
    }

    public void setDtAdmissao(LocalDate dtAdmissao) {
        this.dtAdmissao = dtAdmissao;
    }

    public LocalDate getDtDesligamento() {
        return dtDesligamento;
    }

    public void setDtDesligamento(LocalDate dtDesligamento) {
        this.dtDesligamento = dtDesligamento;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }
}
