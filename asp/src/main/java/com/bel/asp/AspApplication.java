package com.bel.asp;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.bel.asp.entity.User;
import com.bel.asp.repo.UserRepository;
import com.google.common.base.Predicate;

import cn.licoy.encryptbody.annotation.EnableEncryptBody;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@SpringBootApplication(
//		exclude = {SecurityAutoConfiguration.class} 
		)
@ComponentScan()
//@EnableEncryptBody
public class AspApplication implements CommandLineRunner {

//	@Autowired
//	private PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		ConfigurableApplicationContext ctxt =  SpringApplication.run(AspApplication.class, args);
//		UserRepository bean = ctxt.getBean(UserRepository.class);
//		Optional<User> entity = bean.findByUsername("bhanu");
//		System.out.println(entity);
	}
	
	@Bean
	public ModelMapper modelMapper() {
	 return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
//		System.out.println(this.passwordEncoder.encode("xyz"));
	}
}
