package spyrabarber.domain.dto;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;
import spyrabarber.domain.Servico;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ServicoDTO {

    private Long id;

    @NotEmpty(message = "O nome do serviço é obrigatório!")
    private String nome;

    @DecimalMin(value = "1", message = "Digite um tempo de conclusão válido")
    private int tempo;

    @NotEmpty(message = "A descrição do serviço é obrigatória!")
    private String descricao;

    @NotNull(message = "O preço do serviço é obrigatório!")
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    private BigDecimal preco;

    @NotNull(message = "A imagem do serviço é obrigatória!")
    private MultipartFile image;

    public ServicoDTO(Servico s) {
        id = s.getId();
        nome = s.getNome();
        tempo = s.getTempo();
        descricao = s.getDescricao();
        preco = s.getPreco();
        image = null;
    }

    public ServicoDTO(){};

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

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
