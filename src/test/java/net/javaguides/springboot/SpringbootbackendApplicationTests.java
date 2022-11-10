package net.javaguides.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootbackendApplicationTests {

	@Value("${server.port}")
	String port;

	@Value("${server.port},${server.datasource.name}")
	String environement;



	@Test
	void contextLoads() {
	}

	@Test
	void contextLoads1() {
		System.err.println(environement);
	}
}
