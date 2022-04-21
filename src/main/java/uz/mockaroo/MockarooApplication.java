package uz.mutalov.mockaroo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class MockarooApplication {

    public static void main( String[] args ) {
        SpringApplication.run( MockarooApplication.class , args );
    }

}
