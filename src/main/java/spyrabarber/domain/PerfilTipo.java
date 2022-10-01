package spyrabarber.domain;

public enum PerfilTipo {
    ADMIN(1, "ADMIN"), BARBEIRO(2, "BARBEIRO"), CLIENTE(3, "CLIENTE");

    private long cod;
    private String desc;

    private PerfilTipo(long cod, String desc){
        this.cod = cod;
        this.desc = desc;
    }

    public Perfil buildPerfil(){
        return new Perfil(cod, desc);
    }

    public long getCod(){
        return cod;
    }

    public String getDesc(){
        return desc;
    }

}
