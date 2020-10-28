package com.capgemini.employeepayrollservice;
import java.util.*;

public class EmployeePayrollService {
	public enum IOService{CONSOLE_IO,DB_IO,REST_IO,FILE_IO}
	private List<EmployeePayrollData> employeePayrollList;
	
	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		this.employeePayrollList=employeePayrollList;
	}
	public EmployeePayrollService() {
		
	}
	
	public void readEmployeePayrollData(Scanner consoleInputReader) {
		System.out.println("Enter Employee ID: ");
		int id=consoleInputReader.nextInt();
		System.out.println("Enter Employee Name: ");
		String name=consoleInputReader.next();
		System.out.println("Enter Employee Salary: ");
		double salary=consoleInputReader.nextDouble();
		employeePayrollList.add(new EmployeePayrollData(id,name,salary));
		
	}
	public void writeEmployeePayrollData(IOService ioService) {
		if(ioService.equals(IOService.CONSOLE_IO))
		System.out.println("Employee Payroll Data"+employeePayrollList);
		else if(ioService.equals(IOService.FILE_IO))
			new EmployeePayrollFileIOService().writeData(employeePayrollList);
		
	}
	public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) {
		List<EmployeePayrollData> employePayrollDataList=new ArrayList<EmployeePayrollData>();
		if(ioService.equals(IOService.FILE_IO))
			employePayrollDataList=new EmployeePayrollFileIOService().readData();
		return employePayrollDataList;
	}
	public void printData(IOService ioService) {
		if(ioService.equals(IOService.FILE_IO))
		  new EmployeePayrollFileIOService().printData();
	}
	public long countNumberOfEmployees() {
		return new EmployeePayrollFileIOService().countEntries();
	}
	
}
