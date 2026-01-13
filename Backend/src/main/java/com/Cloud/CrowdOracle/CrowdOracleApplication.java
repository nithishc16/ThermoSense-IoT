package com.Cloud.CrowdOracle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.Cloud.CrowdOracle.repository")
public class CrowdOracleApplication 
{

	public static void main(String[] args) 
	{
		SpringApplication.run(CrowdOracleApplication.class, args);
	}

}
