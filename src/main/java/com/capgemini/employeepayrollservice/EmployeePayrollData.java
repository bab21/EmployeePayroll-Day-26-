package com.capgemini.employeepayrollservice;

public class EmployeePayrollData {
	public int id;
	public String name;
	public double salary;
	
	public EmployeePayrollData(int id,String name,double salary) {
		this.id=id;
		this.name=name;
		this.salary=salary;
	}
	
	public String toString() {
		return id+","+name+","+salary;
	}

}
