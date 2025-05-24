package maua.moove.backend.demo.service;


import maua.moove.backend.demo.model.TipoUsuario;
import maua.moove.backend.demo.model.User;
import maua.moove.backend.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
@Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));

    // Converter o tipo de usuário em uma autoridade
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    
    // Adicionar autoridade baseada no tipo de usuário
    if (user.getTipoUsuario() == TipoUsuario.admin) {
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    } else {
        authorities.add(new SimpleGrantedAuthority("ROLE_ALUNO"));
    }

    return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            true, // active fixo como true
            true,
            true,
            true,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
    );
}
}