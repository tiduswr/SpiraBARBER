package spyrabarber.domain;

import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "servicos")
public class Servico extends AbstractEntity{

    @NotEmpty(message = "O nome do serviço é obrigatório!")
    @Column(name = "nome", nullable = false)
    private String nome;

    @DecimalMin(value = "1", message = "Digite um tempo de conclusão válido")
    @Column(name = "tempo_conclusao_min", nullable = false)
    private int tempo;

    @NotEmpty(message = "A descrição do serviço é obrigatória!")
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @NotNull(message = "O preço do serviço é obrigatório!")
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    @Column(name = "preco", nullable = false)
    private BigDecimal preco;

    @NotEmpty(message = "A imagem do serviço é obrigatória!")
    @Column(name = "image_name", nullable = false)
    private String imageName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_has_servico",
            joinColumns = {@JoinColumn(name = "servico_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private Set<Usuario> users;

    public Servico(long id){
        super.setId(id);
    }

    public Servico(){}

    public Set<Usuario> getUsers() {
        return users;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
