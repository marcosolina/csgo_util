package com.marco.csgoutil.roundparser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.marco.csgoutil.roundparser.services.interfaces.StorageService;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class RoundParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoundParserApplication.class, args);
	}

	@Value("${com.marco.csgoutil.roundparser.demfolder}")
    private String demFolder;
	
	@Autowired
	private StorageService ss;
	
	@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                ss.init(demFolder);
            }
        };
    }
}
