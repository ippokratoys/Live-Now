package application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.sql.DataSource;

/**
 * Created by thanasis on 23/8/2017.
 */

@Configuration
@EnableWebSecurity
public class Security {
    private final String USER_NAME_QUERY="select email,pwd,active    from login      where email=?";
    private final String USER_ROLE_QUERY="select infos.email,infos.role "+"from login infos "+" where infos.email = ?";

    @Autowired
    private DataSource dataSource;

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery(this.USER_NAME_QUERY)
                .authoritiesByUsernameQuery(this.USER_ROLE_QUERY)
                .dataSource(dataSource);
    }

    public void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeRequests()
                .antMatchers("**").permitAll()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().logoutSuccessUrl("/login?logout").permitAll();

    }

}
