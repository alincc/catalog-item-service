package no.nb.microservices.catalogitem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;

import no.nb.htrace.annotation.EnableTracing;
import no.nb.metrics.annotation.EnableMetrics;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@RefreshScope
@EnableTracing
@EnableMetrics
@EnableHypermediaSupport(type= {HypermediaType.HAL})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
