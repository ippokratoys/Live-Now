package application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

/**
 * Created by thanasis on 23/8/2017.
 */

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter{
    private final String USER_NAME_QUERY="select login.username, login.password, login.enabled    from login      where login.username=?";
    private final String USER_ROLE_QUERY="select login.username,user_role.role "+" from login,user_role "+" where login.username = ? and user_role.username=login.username";

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery(this.USER_NAME_QUERY)
                .authoritiesByUsernameQuery(this.USER_ROLE_QUERY)
                .dataSource(dataSource);
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeRequests()
                    .antMatchers("**").permitAll()
                .and()
                    .formLogin().loginPage("/login").permitAll().failureForwardUrl("/login?error=login-failed").successForwardUrl("/")
                .and()
                    .logout().logoutSuccessUrl("/login?logout").permitAll();

    }

}
