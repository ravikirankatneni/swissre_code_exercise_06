package com.bigcompany;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bigcompany.exceptions.FailedToReadData;
import com.bigcompany.exceptions.InvalidFile;


/**
 * 
 * @author ravikiran katneni
 * 
 * This class is a helper class to convert csv file to employee map
 * 
 */
public class CSVLoader {
	
	
	/**
	 * @param file -- is the csv file path available in classpath
	 * @return A map of employeeId to Employee
	 * @throws InvalidFile
	 * @throws FailedToReadData
	 */
	public static Map<Long, Employee> readEmployees(String file) throws InvalidFile, FailedToReadData {
		try {

			List<String> lines = readFile(file);

			Map<Long, Employee> employees = traslateCSVEntryToEmployee(lines);

			return employees;

		} catch (URISyntaxException e) {
			System.out.println("Invalid filepath : " + file);
			e.printStackTrace();
			throw new InvalidFile("Invalid filepath : " + file, e);
			
		} catch (IOException e) {
			System.out.println("Failed to read data from file : " + file);
			e.printStackTrace();
			throw new FailedToReadData("Failed to read data from file :" + file, e);
		}
	}

	private static Map<Long, Employee> traslateCSVEntryToEmployee(List<String> lines) {
		Map<Long, Employee> employees = lines.stream().skip(1).map(line -> {
			String[] values = line.split(",");
			long manager = -1;
			if (values.length == 5) {
				manager = Long.parseLong(values[4]);
			}
			return new Employee(Long.parseLong(values[0]), values[1], values[2], Float.parseFloat(values[3]),
					manager);
		}).collect(Collectors.toMap(Employee::getId, value -> value));
		return employees;
	}

	private static List<String> readFile(String file) throws IOException, URISyntaxException {
		URL url = Thread.currentThread().getContextClassLoader().getResource(file);
		if(url == null) {
			throw new InvalidFile("Invalid filepath : " + file, null);
		}
		
		List<String> lines = Files.readAllLines(
				Paths.get(url.toURI()),
				StandardCharsets.UTF_8);
		return lines;
	}

}
