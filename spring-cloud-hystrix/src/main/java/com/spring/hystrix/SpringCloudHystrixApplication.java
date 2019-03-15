package com.spring.hystrix;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;



@SpringBootApplication
@EnableHystrix
@EnableHystrixDashboard
@RestController
@RequestMapping("/hello")
public class SpringCloudHystrixApplication {
	
	public static Logger LOG=LoggerFactory.getLogger(SpringCloudHystrixApplication.class);
	@Autowired
	protected RestTemplate restTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudHystrixApplication.class, args);
		System.out.println("Hystrix enabled service...");
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	
	String url="http://localhost:8081/hello/server";
	@GetMapping("/hystrix")
	@HystrixCommand(fallbackMethod="fallbackHello")
	public String hello() {
		LOG.info("before calling the server");
		String hello=restTemplate.getForObject(url, String.class);
		if(RandomUtils.nextBoolean()) {
			throw new RuntimeException("Getting customized exception");
		}
		LOG.info("After calling the server");
		return hello;
		//return "Hello from client";
	}
	public String fallbackHello() {
		return "fallback method hello";
		//return "Hello from client";
	}
}
