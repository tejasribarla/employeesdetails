/*
 * You can use the following import statements
 * 
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.*;
 * 
 */
// Write your code here

package com.example.employee.service;

import com.example.employee.model.Employee;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.model.EmployeeRowMapper;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service 
public class EmployeeH2Service implements EmployeeRepository{
    
    @Autowired 
    private JdbcTemplate db;
    @Override 
    public ArrayList <Employee> getEmployees() {
        List <Employee> EmployeeList=db.query("Select * from employeelist", new EmployeeRowMapper());
        ArrayList<Employee> employees=new ArrayList<>(EmployeeList);
        return employees;
    }
    @Override 
    public Employee getEmployeeById(int employeeId) {
        try{
            Employee employee=db.queryForObject("Select * from employeelist where employeeId=?", new EmployeeRowMapper(),employeeId);
            return employee;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @Override 
    public Employee addEmployee(Employee employee) {
        db.update("insert into employeelist (EmployeeName,email,department) values(?,?,?)", employee.getEmployeeName(),employee.getEmail(),employee.getDepartment());
        Employee SavedEmployee=db.queryForObject("Select * From employeelist Where employeeName=? and email=? and department=?", new EmployeeRowMapper(), employee.getEmployeeName(),employee.getEmail(),employee.getDepartment());
        return SavedEmployee;
    }

    @Override
    public Employee updateEmployee(int employeeId, Employee employee) {
        if(employee.getEmployeeName()!=null) {
            db.update("update employeelist set employeeName=? where employeeId=?",employee.getEmployeeName(),employeeId);
        }
        if(employee.getEmail()!=null) {
            db.update("update employeelist set email=? where employeeId=?",employee.getEmail(),employeeId);
        }
        if(employee.getDepartment()!=null) {
            db.update("update employeelist set department=? where employeeId =?", employee.getDepartment(),employeeId);
        }
        return getEmployeeById(employeeId);
    }
    @Override
    public void deleteEmployee(int employeeId) {
        db.update("delete from employeelist where employeeId=?",employeeId);
    }
}
