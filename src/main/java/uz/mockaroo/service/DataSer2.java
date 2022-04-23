//package uz.mockaroo.service;
//
//import com.github.javafaker.Faker;
//import com.google.gson.Gson;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import uz.mockaroo.entity.Data;
//import uz.mockaroo.entity.Field;
//import uz.mockaroo.enums.FakerType;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//import static uz.mockaroo.enums.FakerType.*;
//
//public class DataSer2 {
//
//    package uz.mockaroo.service;
//
//import com.github.javafaker.Faker;
//import com.google.gson.Gson;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import uz.mockaroo.entity.Data;
//import uz.mockaroo.entity.Field;
//import uz.mockaroo.enums.FakerType;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//import static uz.mockaroo.enums.FakerType.*;
//
//    @Service
//    public record DataService(Faker faker) {
//
//
//
//        public ResponseEntity<byte[]> generate( Data dto ) {
//
////        AtomicInteger atomicInteger = new AtomicInteger( 1 );
//
//            HashMap<FakerType, Function<Field, String>> functions = new HashMap<>();
//
//            Function<Field, String> username = ( field ) -> faker.name().username();
//            Function<Field, String> id = ( field ) -> faker.name().username();
//            Function<Field, String> fullName = ( field ) -> faker.name().fullName();
//            Function<Field, String> firstName = ( field ) -> faker.name().firstName();
//            Function<Field, String> lastName = ( field ) -> faker.name().lastName();
//            Function<Field, String> address = ( field ) -> faker.address().fullAddress();
//            Function<Field, String> email = ( field ) -> faker.internet().emailAddress();
//            Function<Field, String> animal = ( field ) -> faker.animal().name();
//            functions.put( USER_NAME , username );
//            functions.put( FULL_NAME , fullName );
//            functions.put( ANIMAL_NAME , animal );
//            functions.put( ADDRESS , address );
//            functions.put( EMAIL , email );
//            functions.put( FIRST_NAME , firstName );
//            functions.put( LAST_NAME , lastName );
//
//            List<Map<String, String>> list = new ArrayList<>( dto.getCount() );
//            IntStream.range( 0 , dto.getCount() )
//                    .parallel()
//                    .forEach( i -> {
//                        List<Field> fields = dto.getField();
//                        Map<String, String> collect = fields.parallelStream()
//                                .collect( Collectors.toMap( Field::getName , t -> functions.get( t.getType() ).apply( t ) ) );
//                        list.add( collect );
//                    } );
//
//            String export = export( list );
//            byte[] result = export.getBytes();
//            return ResponseEntity
//                    .ok()
//                    .header( HttpHeaders.CONTENT_DISPOSITION , "attachment;filename=customers.json" )
//                    .contentType( MediaType.APPLICATION_JSON )
//                    .contentLength( result.length )
//                    .body( result );
//        }
//
//
//
//        public String export( List<Map<String, String>> data ) {
//            Gson gson = new Gson();
//            return gson.toJson( data );
//        }
//    }
//
//}
