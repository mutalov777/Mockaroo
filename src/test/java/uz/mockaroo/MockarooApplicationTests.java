package uz.mockaroo;

import com.github.javafaker.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;

//@SpringBootTest
class MockarooApplicationTests {

    @Test
    void contextLoads() {
        Faker faker = new Faker( );
        Name name = faker.name();
        System.out.println( name.fullName() );
        System.out.println( name.firstName() );
        System.out.println( name.name() );



    }

}
