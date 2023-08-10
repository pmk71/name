package com.myt.employee.service.impl;

import com.myt.employee.controller.EmployeeController;
import com.myt.employee.entity.EmployeeEntity;
import com.myt.employee.exception.EmployeeException;
import com.myt.employee.model.Employee;
import com.myt.employee.repository.EmployeeRepository;
import com.myt.employee.service.EmployeeService;
import com.myt.employee.validation.ValidateData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    public static final Logger logger= LogManager.getLogger(EmployeeController.class);
    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public Employee addEmpoyee(Employee employee) throws EmployeeException {
        // Create a new EmployeeEntity and set its properties using the data from the provided 'employee' parameter
        Employee savedEmployee = new Employee();
      boolean isValid =  ValidateData.validateEmployeeData(employee);
      if(isValid) {
          EmployeeEntity newEmployeeEntity = new EmployeeEntity();
          newEmployeeEntity.setEmpFirstName(employee.getEmpFirstName());
          newEmployeeEntity.setEmpLastName(employee.getEmpLastName());
          newEmployeeEntity.setEmpEmail(employee.getEmpEmail());
          newEmployeeEntity.setDoj(employee.getDoj());
          newEmployeeEntity.setSalary(employee.getSalary());
          // Set other properties if needed

          // Save the new EmployeeEntity to the database
          EmployeeEntity savedEmployeeEntity = employeeRepository.save(newEmployeeEntity);

          // Convert the saved EmployeeEntity back to an Employee object and return it
         // Employee savedEmployee = new Employee();
          savedEmployee.setEmpId(savedEmployeeEntity.getEmpId());
          savedEmployee.setEmpFirstName(savedEmployeeEntity.getEmpFirstName());
          savedEmployee.setEmpLastName(savedEmployeeEntity.getEmpLastName());
          savedEmployee.setEmpEmail(savedEmployeeEntity.getEmpEmail());
          savedEmployee.setDoj(savedEmployeeEntity.getDoj());
          savedEmployee.setSalary(savedEmployeeEntity.getSalary());
          // Set other properties if needed
      }
        return savedEmployee;
    }

    @Override
    public Employee updateEmpoyee(Employee employee) throws EmployeeException {
        Long empId = employee.getEmpId();
        Optional<EmployeeEntity> empOptional = employeeRepository.findById(empId);

        if (empOptional.isPresent()) {
            // If the employee exists, update its properties with the data from the provided 'employee' parameter
            EmployeeEntity existingEmployeeEntity = empOptional.get();
            existingEmployeeEntity.setEmpFirstName(employee.getEmpFirstName());
            existingEmployeeEntity.setEmpLastName(employee.getEmpLastName());
            existingEmployeeEntity.setEmpEmail(employee.getEmpEmail());
            existingEmployeeEntity.setEmpId(employee.getEmpId());
            existingEmployeeEntity.setDoj(employee.getDoj());
            existingEmployeeEntity.setSalary(employee.getSalary());
            // Set other properties if needed

            // Save the updated employee back to the database
            EmployeeEntity updatedEmployeeEntity = employeeRepository.save(existingEmployeeEntity);

            // Convert the updated EmployeeEntity back to an Employee object and return it
            Employee updatedEmployee = new Employee();
            updatedEmployee.setEmpId(updatedEmployeeEntity.getEmpId());
            updatedEmployee.setEmpFirstName(updatedEmployeeEntity.getEmpFirstName());
            updatedEmployee.setEmpLastName(updatedEmployeeEntity.getEmpLastName());
            updatedEmployee.setEmpEmail(updatedEmployeeEntity.getEmpEmail());
            updatedEmployee.setDoj(updatedEmployeeEntity.getDoj());
            updatedEmployee.setSalary(updatedEmployeeEntity.getSalary());
            // Set other properties if needed

            return updatedEmployee;
        } else {
            throw new EmployeeException("Employee with ID " + empId + " not found");
        }
    }

    @Override
    public Employee getEmployeeById(Long empId) throws EmployeeException {
        Optional<EmployeeEntity> empById = employeeRepository.findById(empId);
        if (empById.isPresent()) {
            EmployeeEntity employeeEntity = empById.get();
            Employee employee = new Employee();
            employee.setEmpId(employeeEntity.getEmpId());
            employee.setEmpFirstName(employeeEntity.getEmpFirstName());
            employee.setEmpLastName(employeeEntity.getEmpLastName());
            employee.setEmpEmail(employeeEntity.getEmpEmail());
            employee.setDoj(employeeEntity.getDoj());
            employee.setSalary(employeeEntity.getSalary());
            // Set other properties if needed

            return employee;
        } else {
            throw new EmployeeException("Employee with ID " + empId + " not found");
        }
    }

    @Override
    public void deleteEmployeeById(Long empId) throws EmployeeException {
        if(empId!=0 ) {
            employeeRepository.deleteById(empId);
        }else {
            throw new EmployeeException("Employee ID" + empId + "cannot be null");
        }
    }

    @Override
    public List<Employee> getAllEmployee() throws EmployeeException {

        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();
        List<Employee>employees=new ArrayList<>();
        for (EmployeeEntity empEntity:employeeEntities){
            Employee employee = new Employee();
            employee.setEmpFirstName(empEntity.getEmpFirstName());
            employee.setEmpLastName(empEntity.getEmpLastName());
            employee.setEmpEmail(empEntity.getEmpEmail());
            employee.setEmpId(empEntity.getEmpId());
            employee.setDoj(empEntity.getDoj());
            employee.setSalary(empEntity.getSalary());
            employees.add(employee);

        }
        return employees;

    }

    @Override
    public List<Employee> getEmployeeByFirstName(String firstName) {
        List<EmployeeEntity> employeeEntities = employeeRepository.findByEmpFirstNameLike(firstName);
        List<Employee>employees=new ArrayList<>();
        for (EmployeeEntity empEntity:employeeEntities){
            if(empEntity!=null) {
                Employee employee = new Employee();
                employee.setEmpId(empEntity.getEmpId());
                employee.setEmpFirstName(empEntity.getEmpFirstName());
                employee.setEmpLastName(empEntity.getEmpLastName());
                employee.setEmpEmail(empEntity.getEmpEmail());
                employee.setDoj(empEntity.getDoj());
                employee.setSalary(empEntity.getSalary());
                employees.add(employee);
            }else {
                throw new EmployeeException("Employee FirstName cannot found");
            }
        }
        return employees;

    }

    @Override
    public Page<EmployeeEntity> getEmployeePagination(Integer pageNumber, Integer pageSize) {
//        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Sort sort=Sort.by(Sort.Direction.ASC,"salary");
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
//        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Page<EmployeeEntity> entityPage = employeeRepository.findAll(pageable);
        List<Employee>employees=new ArrayList<>();
        for (EmployeeEntity page:entityPage){
            Employee employee = new Employee();
            employee.setEmpId(page.getEmpId());
            employee.setEmpFirstName(page.getEmpFirstName());
            employee.setEmpLastName(page.getEmpLastName());
            employee.setEmpEmail(page.getEmpEmail());
            employee.setDoj(page.getDoj());
            employee.setSalary(page.getSalary());
            employees.add(employee);

        }
        return entityPage;
    }
}
