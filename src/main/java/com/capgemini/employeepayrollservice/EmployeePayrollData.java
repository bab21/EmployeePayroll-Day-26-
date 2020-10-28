package com.capgemini.employeepayrollservice;

import java.time.LocalDate;

public class EmployeePayrollData {
	public int id;
	public String name;
	public double salary;
	public LocalDate start;
	public char gender;
	
	public EmployeePayrollData(int id,String name,double salary) {
		this.id=id;
		this.name=name;
		this.salary=salary;
	}
	public EmployeePayrollData(int id,String name,double salary,LocalDate start,char gender) {
		this(id, name, salary);
		this.start=start;
		this.gender=gender;
	}
	
	public String toString() {
		return id+","+name+","+salary+","+start+","+gender;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this==o) return true;
		if(o==null|| this.getClass()!=o.getClass()) return false;
		EmployeePayrollData that=(EmployeePayrollData)o;
		
		return id==that.id &&
				Double.compare(that.salary, salary)==0 &&
				name.equals(that.name) &&
				that.gender==gender &&
				start.compareTo(that.start)==0;
	}

}
