package spyrabarber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spyrabarber.domain.Pessoa;
import spyrabarber.domain.Usuario;
import spyrabarber.repository.PessoaRepository;
import spyrabarber.repository.UsuarioRepository;
import spyrabarber.web.exception.UsuarioNotFoundException;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public Pessoa buscarPorUsuarioId(Long id) {
        return pessoaRepository.findByUserId(id).orElse(new Pessoa(new Usuario(id)));
    }

    @Transactional(readOnly = false)
    public Pessoa atualizarUsuarioComDadosPessoais(Pessoa pessoa) throws UsuarioNotFoundException{
        if(pessoa.getUser() == null) throw new UsuarioNotFoundException("Usuário não encontrado na base de dados");
        Usuario user = usuarioRepository.findById(pessoa.getUser().getId())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado na base de dados"));
        pessoa.setUser(user);
        return pessoaRepository.save(pessoa);
    }
}
