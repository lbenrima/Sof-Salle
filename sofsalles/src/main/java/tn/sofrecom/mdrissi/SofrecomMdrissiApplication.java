package tn.sofrecom.mdrissi;

import java.time.ZoneId;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SofrecomMdrissiApplication implements CommandLineRunner {

	@PostConstruct
	  void started() {
		TimeZone.getTimeZone(ZoneId.of("Africa/Tunis"));}
	
	public static void main(String[] args) {
		SpringApplication.run(SofrecomMdrissiApplication.class, args);          
	}
	
	@Override
	public void run(String... args) throws Exception {
		
		


	}
}
