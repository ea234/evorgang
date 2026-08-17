package de.ea234.evorgang.model.benutzer;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table ( name = "evg_auth_users" )
public class EvgBenutzer implements UserDetails
{
  @Id
  @GeneratedValue ( strategy = GenerationType.IDENTITY )
  private Integer                    id;

  private String                     username;

  private String                     password;

  private boolean                    enable;

  private boolean                    locked;

  @OneToMany ( mappedBy = "userEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL )
  private Set< EvgGrandetAuthority > authorities;

  public EvgBenutzer()
  {
  }

  public EvgBenutzer( String username, String password, Collection< ? extends GrantedAuthority > authorities )
  {
    this.username = username;
    this.password = password;
    this.authorities = authorities.stream().map( GrantedAuthority::getAuthority ).map( a -> new EvgGrandetAuthority( a, this ) ).collect( Collectors.toSet() );
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername( String username )
  {
    this.username = username;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword( String password )
  {
    this.password = password;
  }

  @Override
  public Collection< ? extends GrantedAuthority > getAuthorities()
  {
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired()
  {
    return true;
  }

  @Override
  public boolean isAccountNonLocked()
  {
    return !this.locked;
  }

  @Override
  public boolean isCredentialsNonExpired()
  {
    return true;
  }

  @Override
  public boolean isEnabled()
  {
    return this.enable;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId( Integer id )
  {
    this.id = id;
  }

  public boolean isEnable()
  {
    return enable;
  }

  public void setEnable( boolean enable )
  {
    this.enable = enable;
  }

  public boolean isLocked()
  {
    return locked;
  }

  public void setLocked( boolean locked )
  {
    this.locked = locked;
  }

}
