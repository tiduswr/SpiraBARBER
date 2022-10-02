package spyrabarber.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class Usuario extends AbstractEntity{

    @NotEmpty(message = "O email não pode estar vazio")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @NotEmpty(message = "A senha não pode estar vazia")
    @Column(name = "password", nullable = false)
    private String senha;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_has_profile",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "profile_id", referencedColumnName = "id")}
    )
    private Set<Perfil> perfis;

    @Column(name = "active", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean ativo;

    public Usuario() {
        super();
    }

    public Usuario(Long id) {
        super.setId(id);
    }

    // adiciona perfis a lista
    public void addPerfil(PerfilTipo tipo) {
        if (this.perfis == null) {
            this.perfis = new HashSet<>();
        }
        this.perfis.add(tipo.buildPerfil());
    }

    public Usuario(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Set<Perfil> getPerfis() {
        return perfis;
    }

    public void setPerfis(Set<Perfil> perfis) {
        this.perfis = perfis;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", perfis=" + perfis.stream().map(Perfil::getDesc).collect(Collectors.joining(",")) +
                ", ativo=" + ativo +
                '}';
    }
}
