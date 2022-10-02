package spyrabarber.web.conversor;

import org.springframework.stereotype.Component;
import spyrabarber.domain.Perfil;

import org.springframework.core.convert.converter.Converter;
import spyrabarber.domain.PerfilTipo;

@Component
public class PerfilConverter implements Converter<String, Perfil> {

    @Override
    public Perfil convert(String s) {
        if(isNumeric(s)) return PerfilTipo.buildByCod(Long.parseLong(s));
        return new Perfil();
    }

    private boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}
