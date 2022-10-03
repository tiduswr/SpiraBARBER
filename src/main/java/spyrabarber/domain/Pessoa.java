package spyrabarber.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "pessoas")
public class Pessoa extends AbstractEntity{

    @NotEmpty(message = "O nome não pode estar vazio")
    @Column(name = "name", nullable = false)
    private String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "A data de nascimento é obrigatória")
    @Column(name = "dt_nascimento", nullable = false)
    private LocalDate dtNascimento;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Usuario user;

    public Pessoa(){super();}

    public Pessoa(Usuario user){
        this.user = user;
    }

    public Pessoa(Long id){super.setId(id);}

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

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "name='" + name + '\'' +
                ", dtNascimento=" + dtNascimento +
                ", user=" + user.getEmail() +
                '}';
    }
}
