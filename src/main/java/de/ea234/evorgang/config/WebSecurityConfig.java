package de.ea234.evorgang.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig
{
  /*
  docker run --link some_database:db -p 8080:8083 adminer
  docker run --link jdbc:mariadb://localhost:3306/epost2 -p 8080:8083 adminer
  
  docker run --name adminer   --add-host=host.docker.internal:host-gateway   -p 8083:8080   adminer
  
  
  INSERT INTO 'tbl_auth_users' ( 'username', 'password', 'enable', 'locked') VALUES
  ('admin',  '{noop}geheim',  CONVERT(b'1', UNSIGNED),  CONVERT(b'0', UNSIGNED));
  
  INSERT INTO 'tbl_auth_authorities' ('authority', 'userEntity_id') VALUES
  ('ROLE_ADMIN',  1),
  ('ROLE_USER',  1);
  
  */
//  @Bean
//  public UserDetailsManager userDetailsManager()
//  {
//    UserDetails user = User.withDefaultPasswordEncoder().username( "user" ).password( "secret" ).roles( "USER" ).build();
//
//    UserDetails admin = User.withDefaultPasswordEncoder().username( "admin" ).password( "secret" ).roles( "USER", "ADMIN" ).build();
//
//    return new InMemoryUserDetailsManager( user, admin );
//  }
//
//  @Autowired
//  public void configureAuth( AuthenticationManagerBuilder builder, DataSource datasource ) throws Exception
//  {
//    var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    
//    UserDetails user = User.withUsername( "user" )
//      .password( "secret" )
//      .passwordEncoder( encoder::encode )
//      .roles( "USER" )
//      .build();
//
//    UserDetails admin = User.withUsername( "admin" )
//      .password( "secret" )
//      .passwordEncoder( encoder::encode )
//      .roles( "USER", "ADMIN" )
//      .build();
//
//    builder.jdbcAuthentication()    // erstellt UserDetailsManager
//           .dataSource( datasource )
//           //.withDefaultSchema() // erzeugt db shema
//           .passwordEncoder( encoder );
//           //.withUser( user )
//           //.withUser( admin );
//  }
//
//  

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider( UserDetailsManager manager, PasswordEncoder encoder )
  {
    var daoAuthenticationProvider = new DaoAuthenticationProvider(manager);

    daoAuthenticationProvider.setPasswordEncoder( encoder );
    
    return daoAuthenticationProvider;
  }
  
  
 // @Bean
  public CommandLineRunner initUsers( UserDetailsManager manager, PasswordEncoder encoder) {

      return args -> {

          if (!manager.userExists("user")) {
              UserDetails user = User.withUsername("user")
                      .password(encoder.encode("secret"))
                      .roles("USER")
                      .build();

              manager.createUser(user);
          }

          if (!manager.userExists("admin")) {
              UserDetails admin = User.withUsername("admin")
                      .password(encoder.encode("secret"))
                      .roles("USER", "ADMIN")
                      .build();

              manager.createUser(admin);
          }
      };
  }
  
  @Bean
  public PasswordEncoder passwordEncoder()
  {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  @Order ( 1 )
  SecurityFilterChain h2( HttpSecurity http ) throws Exception
  {
    http.securityMatcher( "/h2/**" ).csrf( csrf -> csrf.ignoringRequestMatchers( "/h2/**" ) )
                                    .headers( h -> h.frameOptions( f -> f.sameOrigin() ) )
                                    .authorizeHttpRequests( auth -> auth.anyRequest().permitAll() );

    return http.build();
  }

  @Bean
  @Order ( 2 )
  SecurityFilterChain app( HttpSecurity http ) throws Exception
  {
    http.authorizeHttpRequests( auth -> 
        auth.requestMatchers( "/admin1/**" ).hasRole( "ADMIN" )
            .requestMatchers( "/admin2/**" ).permitAll()
            .requestMatchers( "/logoutsuccessfull" ).permitAll().anyRequest().authenticated() )
            .formLogin( form -> form.permitAll() );
    //.oauth2Login(Customizer.withDefaults());

    return http.build();
  }

/*
 * <dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
 */

//
}