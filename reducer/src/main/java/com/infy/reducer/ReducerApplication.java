package com.infy.reducer;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.reducer.datacompressor.DataCompressor;
import com.infy.reducer.datacompressor.DataCompressorImpl;
import com.infy.reducer.dataconverter.DataConverter;
import com.infy.reducer.dataconverter.DataConverterImpl;
import com.infy.reducer.entity.Person;
import com.infy.reducer.file.CompressedFile;

@SpringBootApplication
public class ReducerApplication {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SpringApplication.run(ReducerApplication.class, args);
		
		try {

			String path = "src/main/resources/Person.json";
			String json = readFileAsString(path);

			Class<?> entityClass = Class.forName("com.infy.reducer.entity.Person");
			
			 Object myObject = entityClass.getDeclaredConstructor().newInstance();
			 
			 DataConverter<Person> dataConverter = new DataConverterImpl<>(Person.class);
			 DataCompressor<Person> dataCompressor = new DataCompressorImpl<>(dataConverter);
			
			System.out.println(Person.class) ;
			System.out.println(entityClass);
			System.out.println(myObject.getClass().getDeclaredMethods()) ;
			


			Person convertedObject = dataConverter.jsonToJavaObject(json);

			byte[] compressedData = dataCompressor.compress(convertedObject);

			CompressedFile.bytetoFile(compressedData);
			Person decompressedData = dataCompressor.decompress(compressedData);

			String result = dataConverter.javaObjectToJson(decompressedData);
//			System.out.println(result) ;
	

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String readFileAsString(String path) throws Exception {
		return new String(Files.readAllBytes(Paths.get(path)));
	}

}
