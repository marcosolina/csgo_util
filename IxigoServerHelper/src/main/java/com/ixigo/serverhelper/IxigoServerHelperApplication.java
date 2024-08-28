package com.ixigo.serverhelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ixigo.serverhelper.services.interfaces.DemFilesService;
import com.ixigo.serverhelper.services.interfaces.DnsUpdater;

import reactor.core.scheduler.Schedulers;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
// to scan for components defined in the Library project
@ComponentScan(basePackages = "com.ixigo")
public class IxigoServerHelperApplication {

	public static void main(String[] args) {
		SpringApplication.run(IxigoServerHelperApplication.class, args);
	}
	@Autowired
    private DnsUpdater updater;
	@Autowired
	private DemFilesService demFileService;

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                demFileService.postLastDemFiles(false)
                    .subscribeOn(Schedulers.parallel())
            	    .subscribe(b -> updater.updateDnsEntry());
            }
        };
    }
}
