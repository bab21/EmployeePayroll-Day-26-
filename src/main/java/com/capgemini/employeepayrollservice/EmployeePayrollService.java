package com.capgemini.employeepayrollservice;
import java.util.*;

public class EmployeePayrollService {
	public enum IOService{CONSOLE_IO,DB_IO,REST_IO,FILE_IO}
	private  List<EmployeePayrollData> employeePayrollList;
	private EmployeePayrollDBService employeePayrollDBService;
	
	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		this();
		this.employeePayrollList=employeePayrollList;
	}
	public EmployeePayrollService() {
		employeePayrollDBService=EmployeePayrollDBService.getInstance();
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
	public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) throws EmployeePayrollException {
		
		if(ioService.equals(IOService.FILE_IO))
			this.employeePayrollList=new EmployeePayrollFileIOService().readData();
		if(ioService.equals(IOService.DB_IO))
			this.employeePayrollList=employeePayrollDBService.readData();
		return employeePayrollList;
	}
	
	public void printData(IOService ioService) {
		if(ioService.equals(IOService.FILE_IO))
		  new EmployeePayrollFileIOService().printData();
	}
	public long countNumberOfEmployees() {
		return new EmployeePayrollFileIOService().countEntries();
	}
	public void updateEmployeeSalary(String name,double salary) throws EmployeePayrollException {
		int result=employeePayrollDBService.updateEmployeeData(name,salary);
		if(result==0)return;
		EmployeePayrollData employeePayrollData=this.getEmployeePayrollData(name);
		if(employeePayrollData!=null) employeePayrollData.salary=salary;
	}
	private EmployeePayrollData getEmployeePayrollData(String name) {
		
		return this.employeePayrollList.stream()
				.filter(employeePayrollDataItem->employeePayrollDataItem.name.equals(name))
				.findFirst()
				.orElse(null);
	}
	public boolean checkEmployeePayrollInSyncWithDB(String name) throws EmployeePayrollException {
		List<EmployeePayrollData> employeePayrollDataList=employeePayrollDBService.getEmployeePayrollData(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}
	
	public List<EmployeePayrollData> getEmployeeWithDateRange() throws EmployeePayrollException{
		List<EmployeePayrollData> employeePayrollDataList=employeePayrollDBService.getEmployeePayrollDataWithStartDateInGivenRange();
		return employeePayrollDataList;
	}
	
}
