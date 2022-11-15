package net.javaguides.springboot;

import net.javaguides.springboot.model.AppRole;
import net.javaguides.springboot.model.AppUser;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SpringbootbackendApplication implements CommandLineRunner {

    @Value("${spring.datasource.name},${server.port}")
    String environementport;

	public static void main(String[] args) {SpringApplication.run(SpringbootbackendApplication.class, args);
	}

	@Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        System.err.println(environementport);
       // Employee employee = new Employee();
        //employee.setFirstName("babay1");
       // employee.setLastName("babyuuuu1");
        //employee.setEmailId("just1@gmail.com");
       // employeeRepository.save(employee);

        //Employee employee2 = new Employee();
       // employee2.setFirstName("babay22");
       // employee2.setLastName("bayyyuu2");
       // employee2.setEmailId("just22@gmail.com");
       // employeeRepository.save(employee2);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
    
    @Bean
    CommandLineRunner start(AccountService accountService){
        return  args -> {
            accountService.addNewRole(new AppRole(1,"USER"));
            accountService.addNewRole(new AppRole(2,"ADMIN"));
            accountService.addNewRole(new AppRole(3,"CUSTOMER"));
            accountService.addNewRole(new AppRole(4,"PRODUCT"));


            accountService.addNewUser(new AppUser(1,"User1","1234",new ArrayList<>()));
            accountService.addNewUser(new AppUser(2,"admin","1234",new ArrayList<>()));
            accountService.addNewUser(new AppUser(3,"User2","1234",new ArrayList<>()));
            accountService.addNewUser(new AppUser(4,"User3","1234",new ArrayList<>()));


            accountService.addRoleToUser("user1","USER");
            accountService.addRoleToUser("user1","ADMIN");
            accountService.addRoleToUser("admin","ADMIN");
            accountService.addRoleToUser("user2","CUSTOMER");
            accountService.addRoleToUser("user3","PRODUCT");
        };
        
    }
}
