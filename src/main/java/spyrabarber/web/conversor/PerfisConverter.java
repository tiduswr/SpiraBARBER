package spyrabarber.web.conversor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import spyrabarber.domain.Perfil;
import spyrabarber.domain.PerfilTipo;

import java.util.HashSet;
import java.util.Set;

@Component
public class PerfisConverter implements Converter<String[], Set<Perfil>> {

    @Override
    public Set<Perfil> convert(String[] source) {
        Set<Perfil> perfis = new HashSet<>();

        for(String s : source){
            if(isNumeric(s)) perfis.add(PerfilTipo.buildByCod(Long.parseLong(s)));
        }

        return perfis;
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
