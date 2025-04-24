package br.com.sistemacadastro.sistemacadastro.security;

import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Collaborator;
import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.CollaboratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Component
public class CustomCollaboratordetailsService implements UserDetailsService {

    @Autowired
    private CollaboratorRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collaborator collaborator = this.repository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(collaborator.getEmail(), collaborator.getSenha(), new ArrayList<>());
    }
}
