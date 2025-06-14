package com.bigcompany;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.bigcompany.exceptions.InvalidFile;

public class EmployeeStructureAnalyzerTest {
	
	@Test
	void testInvalidFilePath() {
		
		assertThrows(InvalidFile.class , ()->{
			EmployeeStructureAnalyzer analyzer = new EmployeeStructureAnalyzer();
			analyzer.analyze("emp::xys");
		});
	}
	
	@Test
	void testWithSmallData() {
		EmployeeStructureAnalyzer analyzer = new EmployeeStructureAnalyzer();
		analyzer.analyze("employees.csv");
		assertTrue(analyzer.getManagersWithLessPay().size() == 1);
		assertTrue(analyzer.getManagersWithLessPay().get(0).getId() == 124);
		assertTrue(analyzer.getManagersWithHighPay().size() == 0);
		assertTrue(analyzer.getEmployeesWithLongReportingLine().size() == 0);

	}

	@Test
	void testWithLargeData() {
		EmployeeStructureAnalyzer analyzer = new EmployeeStructureAnalyzer();
		analyzer.analyze("generated_employees.csv");
		assertTrue(analyzer.getManagersWithLessPay().size() == 349);
		assertTrue(analyzer.getManagersWithLessPay().stream().map(Employee::getId).collect(Collectors.toList())
				.containsAll(Arrays.asList(new Long[] { 1015l, 1017l, 1018l, 1438l, 1442l, 1444l, 1446l, 1449l, 1889l,
						1897l, 1901l, 1932l })));
		assertTrue(analyzer.getManagersWithHighPay().size() == 74);
		assertTrue(analyzer.getManagersWithHighPay().stream().map(Employee::getId).collect(Collectors.toList())
				.containsAll(Arrays.asList(new Long[] { 1030l, 1034l, 1042l, 1060l, 1274l, 1284l, 1291l })));
		assertTrue(analyzer.getEmployeesWithLongReportingLine().size() == 660);
		assertTrue(analyzer.getEmployeesWithLongReportingLine().keySet().stream().map(Employee::getId)
				.collect(Collectors.toList()).containsAll(Arrays.asList(new Long[] { 1951l, 1658l, 1473l, 1249l, 1232l,
						1766l, 1832l, 1961l, 1665l, 1881l, 1515l, 1754l, 1846l })));
	}
}
