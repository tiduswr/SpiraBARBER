package spyrabarber.domain;

public enum CargoTipo {
    BARBEIRO(1, "BARBEIRO"), AUXILIAR_ADMINISTRATIVO(2, "AUXILIAR ADMINISTRATIVO");

    private final long cod;
    private final String desc;

    private CargoTipo(long cod, String desc){
        this.cod = cod;
        this.desc = desc;
    }

    public Cargo buildCargo(){
        return new Cargo(cod, desc);
    }

    public static Cargo buildByCod(long cod){
        for(CargoTipo c : values()){
            if(c.cod == cod) return c.buildCargo();
        }
        return null;
    }

    public long getCod(){
        return cod;
    }

    public String getDesc(){
        return desc;
    }

}
