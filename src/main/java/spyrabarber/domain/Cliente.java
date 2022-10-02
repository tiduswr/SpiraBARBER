package spyrabarber.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "clientes")
public class Cliente extends AbstractEntity{

    @NotEmpty(message = "O nome não pode estar vazio")
    @Column(name = "name", nullable = false)
    private String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "A data de nascimento é obrigatória")
    @Column(name = "dt_nascimento", nullable = false)
    private LocalDate dtNascimento;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "A data de cadastro não pode ser vazia")
    @Column(name = "dt_cad", nullable = false)
    private LocalDate dtCadastro;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private Usuario user;

    public Cliente(){super();}

    public Cliente(Long id){super.setId(id);}

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

    public LocalDate getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(LocalDate dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }
}
