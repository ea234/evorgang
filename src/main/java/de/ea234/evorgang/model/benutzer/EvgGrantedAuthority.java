package de.ea234.evorgang.model.benutzer;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table ( name = "evg_auth_authorities" )
public class EvgGrantedAuthority implements GrantedAuthority
{
  public EvgGrantedAuthority()
  {
  }

  public EvgGrantedAuthority( String authority, EvgBenutzer userEntity )
  {
    this.authority = authority;
    this.userEntity = userEntity;
  }

  @Id
  @GeneratedValue ( strategy = GenerationType.IDENTITY )
  private Integer     id;

  private String      authority = null;

  @ManyToOne ( optional = false )
  private EvgBenutzer userEntity;

  public Integer getId()
  {
    return id;
  }

  public void setId( Integer id )
  {
    this.id = id;
  }

  public EvgBenutzer getUser()
  {
    return userEntity;
  }

  public void setUser( EvgBenutzer user )
  {
    this.userEntity = user;
  }

  public void setAuthority( String authority )
  {
    this.authority = authority;
  }

  @Override
  public String getAuthority()
  {
    return authority;
  }

}
