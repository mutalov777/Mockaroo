package uz.mockaroo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.mockaroo.entity.Data;
import uz.mockaroo.entity.Field;
import uz.mockaroo.enums.FakerType;
import uz.mockaroo.enums.FileFormat;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static uz.mockaroo.enums.FakerType.*;

@Service
public class DataService {
    private static final Faker faker = new Faker();

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Gson gson = new Gson();

    private final AtomicInteger atomicInteger = new AtomicInteger( 1 );
    private final AtomicInteger fileNameCounter = new AtomicInteger( 1 );
    private final HashMap<FakerType, Function<Field, String>> functions = new HashMap<>() {{
        put( USER_NAME , ( field ) -> faker.name().username() );
        put( FULL_NAME , ( field ) -> faker.name().fullName() );
        put( ANIMAL_NAME , ( field ) -> faker.animal().name() );
        put( COMPANY_NAME , ( field ) -> faker.company().name() );
        put( ADDRESS , ( field ) -> faker.address().fullAddress() );
        put( EMAIL , ( field ) -> faker.internet().emailAddress() );
        put( FIRST_NAME , ( field ) -> faker.name().firstName() );
        put( LAST_NAME , ( field ) -> faker.name().lastName() );
        put( ROW_NUMBER , ( field ) -> String.valueOf( atomicInteger.getAndIncrement() ) );
    }};

    private final HashMap<FileFormat, Function<Data, ResponseEntity<?>>> formatFunctions = new HashMap<>() {{
        put( FileFormat.JSON , ( data ) -> generateJson( data ) );
        put( FileFormat.SQL , ( data ) -> generateSql( data ) );
        put( FileFormat.EXCEL , ( data ) -> generateExcel( data ) );
    }};

    public String export( List<?> data ) throws JsonProcessingException {
        return mapper.writeValueAsString( data );
    }

    public String export( Map<?, ?> data ) throws JsonProcessingException {
        return mapper.writeValueAsString( data );
    }

    public String exportByGson( List<?> data ) {
        return gson.toJson( data );
    }

    public ResponseEntity<?> generate( Data data ) {
        return formatFunctions.get( data.getFormat() ).apply( data );
    }

    public ResponseEntity<?> generateJson( Data dto ) {

        List<Map<String, String>> list = new ArrayList<>( dto.getCount() );
        IntStream.range( 0 , dto.getCount() ).parallel().forEach( i -> {
            Map<String, String> collect = dto.getField().parallelStream().collect( Collectors.toMap( Field::getName , t -> functions.get( t.getType() ).apply( t ) ) );
            list.add( collect );
        } );
        byte[] result = exportByGson( list ).getBytes();
        atomicInteger.set( 1 );
        return ResponseEntity.ok().header( HttpHeaders.CONTENT_DISPOSITION , "attachment;filename=customers.json" )
                .contentType( MediaType.APPLICATION_JSON )
                .contentLength( result.length )
                .body( result );
    }

    public  ResponseEntity<?> generateSql( Data dto ) {
        File file = new File( "src\\main\\resources\\MOCK_DATA" + fileNameCounter.getAndIncrement() + ".sql" );
        try {
            FileOutputStream writer = new FileOutputStream( file );
            BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( writer ) );

            IntStream.range( 0 , dto.getCount() ).parallel().forEach( i -> {
                StringBuilder key = new StringBuilder();
                StringBuilder value = new StringBuilder();
                for ( Field field: dto.getField() ) {
                    key.append( field.getName() ).append( "," );
                    value.append( "'" ).append( functions.get( field.getType() ).apply( field ) ).append( "'," );
                }
                String format = String.format( "insert into MOCK_DATA (%s) values (%s);\n" , key.substring( 0 , key.length() - 1 ) , value.substring( 0 , value.length() - 1 ) );
                try {
                    bw.write( format );
                } catch ( IOException e ) {
                    throw new RuntimeException( e );
                }
            } );

            InputStreamResource i = new InputStreamResource( new FileInputStream( file ) );
            bw.close();
            writer.close();
            return ResponseEntity.ok().header( HttpHeaders.CONTENT_DISPOSITION , "attachment;filename=" + file.getName() ).contentLength( file.length() ).contentType( MediaType.TEXT_PLAIN ).body( i );
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            atomicInteger.set( 1 );

        }
        return null;
    }

    public ResponseEntity<?> generateExcel( Data dto ) {
        XSSFWorkbook workbook;
        File file = new File( "src\\main\\resources\\MOCK_DATA.xlsx" );
        try ( FileOutputStream out = new FileOutputStream( file ) ) {
            workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet( "Mock data" );
            AtomicInteger rowcount = new AtomicInteger( 0 );
            XSSFRow row = sheet.createRow( rowcount.incrementAndGet() );
            for ( int i = 0; i < dto.getField().size(); i++ ) {
                XSSFCell cell = row.createCell( i + 1 );
                cell.setCellValue( dto.getField().get( i ).getName() );
            }

            IntStream.range( 0 , dto.getCount() ).forEach( i -> {
                AtomicInteger cellcount = new AtomicInteger( 0 );
                XSSFRow row1 = sheet.createRow( rowcount.incrementAndGet() );
                for ( int j = 0; j < dto.getField().size(); j++ ) {
                    XSSFCell c = row1.createCell( cellcount.incrementAndGet() );
                    c.setCellValue( functions.get( dto.getField().get( j ).getType() ).apply( dto.getField().get( j ) ) );
                }
            } );

            workbook.write( out );
            workbook.close();
            InputStreamResource i = new InputStreamResource( new FileInputStream( file ) );
            return ResponseEntity.ok().header( HttpHeaders.CONTENT_DISPOSITION , "attachment;filename=" + file.getName() ).contentLength( file.length() ).contentType( MediaType.parseMediaType( "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ) ).body( i );
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            atomicInteger.set( 1 );
        }
        return null;
    }
}

