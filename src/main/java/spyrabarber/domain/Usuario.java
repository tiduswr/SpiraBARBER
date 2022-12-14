package spyrabarber.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_has_servico",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "servico_id", referencedColumnName = "id")}
    )
    private Set<Servico> servicos;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_cargo_id")
    private UserCargo userCargo;

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

    // adiciona servicos a lista
    public void addServico(Servico servico) {
        if (this.perfis == null) {
            this.perfis = new HashSet<>();
        }
        this.servicos.add(servico);
    }

    public Usuario(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public UserCargo getUserCargo() {
        return userCargo;
    }

    public void setUserCargo(UserCargo cargo) {
        this.userCargo = cargo;
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

    public Set<Servico> getServicos() {
        return servicos;
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

    public void setServicos(Set<Servico> servicos) {
        this.servicos = servicos;
    }
}
