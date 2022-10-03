package spyrabarber.domain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "cargos")
public class Cargo extends AbstractEntity{

    @NotEmpty(message = "O nome não pode estar vazio")
    @Column(name = "desc", nullable = false)
    private String desc;

    public Cargo(){super();}

    public Cargo(Long id, String desc){
        super.setId(id);
        this.desc = desc;
    }

    public Cargo(Long id){super.setId(id);}

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "desc='" + desc + '\'' +
                '}';
    }
}
