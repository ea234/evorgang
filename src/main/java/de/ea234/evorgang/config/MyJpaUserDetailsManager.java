package de.ea234.evorgang.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import de.ea234.evorgang.model.benutzer.EvgBenutzer;
import de.ea234.evorgang.model.benutzer.EvgBenutzerRepository;

@Service
public class MyJpaUserDetailsManager implements UserDetailsManager {

  private final EvgBenutzerRepository userRepository;

  public MyJpaUserDetailsManager( EvgBenutzerRepository userRepository )
  {
    this.userRepository = userRepository;
  }

  @Override
  public void createUser( UserDetails user )
  {
    EvgBenutzer my_user = new EvgBenutzer( user.getUsername(), user.getPassword(), user.getAuthorities() );

    my_user.setEnable( true );
    
    my_user.setLocked( ! user.isAccountNonLocked() );

    userRepository.save( my_user );
  }

  @Override
  public void updateUser( UserDetails user )
  {
    createUser( user );
  }

  @Override
  public void deleteUser( String username )
  {
    userRepository.deleteByUsername( username );
  }

  @Override
  public void changePassword( String oldPassword, String newPassword )
  {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();

    var user = userRepository.findByUsernameIgnoreCase( username );

    user.setPassword( newPassword );

    userRepository.save( user );
  }

  @Override
  public boolean userExists( String username )
  {
    return userRepository.existsByUsername( username );
  }

  @Override
  public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException
  {
    var my_user = userRepository.findByUsernameIgnoreCase( username );

    if ( my_user == null )
    {
      throw new UsernameNotFoundException( username );
    }

    return my_user;
  }
}