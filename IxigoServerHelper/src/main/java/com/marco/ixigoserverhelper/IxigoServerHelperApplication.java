package com.marco.ixigoserverhelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IxigoServerHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(IxigoServerHelperApplication.class, args);
    }

    /*
    @Autowired
    private DemFilesService ss;

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                ss.postLastDemFile();
            }
        };
    }
    */
}
