package com.isd.parking.config.security;

import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import java.util.Arrays;

import static com.isd.parking.utils.ColorConsoleOutput.blTxt;

@Configuration
@EnableLdapRepositories(basePackages = "com.isd.parking.** ** ")
@Slf4j
public class LdapConfig {

    private final Environment env;

    private final ColorConsoleOutput console;

    @Autowired
    public LdapConfig(Environment env, ColorConsoleOutput console) {
        this.env = env;
        this.console = console;
    }

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(env.getRequiredProperty("ldap.url"));
        contextSource.setBase(env.getRequiredProperty("ldap.partitionSuffix"));
        /*contextSource.setUserDn(env.getRequiredProperty("ldap.username"));
        contextSource.setPassword(env.getRequiredProperty("ldap.password"));*/

        log.info(console.classMsg("ldap configuration"));
        log.info(blTxt(contextSource.getBaseLdapPathAsString()));
        log.info(blTxt(String.valueOf(contextSource.getBaseLdapName())));
        log.info(blTxt(String.valueOf(contextSource.getAuthenticationSource())));
        log.info(blTxt(Arrays.toString(contextSource.getUrls())));

        contextSource.afterPropertiesSet();
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }

    /*@Bean
    public UserService ldapClient() {
        return new UserService();
    }*/
}