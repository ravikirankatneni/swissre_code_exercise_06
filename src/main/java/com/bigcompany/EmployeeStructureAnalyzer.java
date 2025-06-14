package com.bigcompany;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * @author ravikiran katneni
 * 
 * This class provides functionality to find mangers with high pay, low pay and 
 * employees with long reporting path
 */
public class EmployeeStructureAnalyzer {
	private Map<Employee, Integer> employeesWithLongReportingLine;
	private List<Employee> managersWithHighPay;
	private List<Employee> managersWithLessPay;

	/**
	 * @param employeeDataCSV -- is the csv file path with employee data  available in classpath
	 */
	public void analyze(String employeeDataCSV) {

		Map<Long, Employee> employees = CSVLoader.readEmployees(employeeDataCSV);

		checkAndPrintManagersWithHighAndLowSalary(employees);

		checkAndPrintEmployeesWithLongReportingLine(employees);

	}

	/**
	 * This method recursively goes through each employee reporting path and identifies employees with long
	 * reporting path.
	 * 
	 * @param employees 
	 */
	private void checkAndPrintEmployeesWithLongReportingLine(Map<Long, Employee> employees) {
		int maxAllowedReportingLine = 5;
		employeesWithLongReportingLine = new HashMap<>();
		for (Employee e : employees.values()) {
			int depth = findReportingDepth(e, employees);
			if (depth > maxAllowedReportingLine) {
				employeesWithLongReportingLine.put(e, depth - maxAllowedReportingLine);
			}
		}
		System.out.println("Employees with longest reporting line: \n");
		employeesWithLongReportingLine.entrySet()
				.forEach(e -> System.out.println(String.format(
						" Employee: \"%s\" has longest reporting which is \"%d\" morethan maximum allowed reporting ",
						e.getKey(), e.getValue())));
	}

	private int findReportingDepth(Employee e, Map<Long, Employee> employees) {
		if (e.getManagerId() == -1) {
			return 0;
		}

		return 1 + findReportingDepth(employees.get(e.getManagerId()), employees);
	}

	
	/**
	 * This method calculates average salary of subordinates of each manager. 
	 * Prepares list of managers with low pay when they are paid not more than 20% average salary of their subordinates
	 * Prepares list of managers with high pay when they are paid more than 50% average salary of their subordinates
	 * 
	 * @param employees
	 */
	private void checkAndPrintManagersWithHighAndLowSalary(Map<Long, Employee> employees) {
		Map<Long, Double> averageDirectSubordinateSalary = employees.values().stream().collect(
				Collectors.groupingBy(Employee::getManagerId, Collectors.averagingDouble(Employee::getSalary)));
		managersWithHighPay = new ArrayList<>();
		managersWithLessPay = new ArrayList<>();
		for (var aggregateEntry : averageDirectSubordinateSalary.entrySet()) {
			Employee manager = employees.get(aggregateEntry.getKey());
			if (manager == null) {
				continue;
			}
			if (manager.getSalary() > aggregateEntry.getValue() * 150 / 100) {
				managersWithHighPay.add(manager);
			} else if (manager.getSalary() < aggregateEntry.getValue() * 120 / 100) {
				managersWithLessPay.add(manager);
			}
		}

		System.out.println("Managers With High Pay: \n");
		managersWithHighPay.forEach(m -> System.out.println(m));
		System.out.println("Managers With Less Pay: \n ");
		managersWithLessPay.forEach(m -> System.out.println(m));
	}

	public Map<Employee, Integer> getEmployeesWithLongReportingLine() {
		return employeesWithLongReportingLine;
	}

	public List<Employee> getManagersWithHighPay() {
		return managersWithHighPay;
	}

	public List<Employee> getManagersWithLessPay() {
		return managersWithLessPay;
	}

}
