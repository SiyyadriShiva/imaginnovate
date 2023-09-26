package loc.test.emptaxdeduction.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import loc.test.emptaxdeduction.dto.ResponseDTO;
import loc.test.emptaxdeduction.entity.Employee;
import loc.test.emptaxdeduction.payload.CurrentFinancialYear;
import loc.test.emptaxdeduction.payload.EmployeePayload;
import loc.test.emptaxdeduction.repository.EmployeeRepository;
import loc.test.emptaxdeduction.response.EmployeeTaxDeduction;

@Service
public class EmployeeService {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseDTO<Employee> storeEmployeeData(EmployeePayload empPayload) {
		Employee saveEmp = null;
		
		ResponseDTO<Employee> resp = new ResponseDTO("90", "Issue in Storing", saveEmp);
		try {
			saveEmp = new Employee();
			BeanUtils.copyProperties(empPayload, saveEmp);
			employeeRepository.save(saveEmp);
			resp = new ResponseDTO("00", "Success", saveEmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resp;
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseDTO<List<EmployeeTaxDeduction>> getEmployeeTaxDeductionForCurrentFinancialYear(CurrentFinancialYear finYear) {
		// Get employees list
		 List<Employee> employees = employeeRepository.getFinancialReportBasedOnUserId(finYear.getFromDate(), finYear.getToDate());
		 
		// Calculate tax deduction for each employee
        List<EmployeeTaxDeduction> employeeTaxDeductions = new ArrayList();
		
		ResponseDTO<List<EmployeeTaxDeduction>> resp = new ResponseDTO("90", "Issue in EmployeeTaxDeduction", employeeTaxDeductions);
		try {
			
			        employees.stream()
					.collect(Collectors
							.groupingBy(Employee::getEmployeeId,
									    Collectors.summingDouble(Employee::getMonthlySalary)))
					.entrySet().forEach((e)-> {
						
						// Calculate tax amount based on total salary
			            BigDecimal taxAmount = calculateTaxAmount(new BigDecimal(e.getValue()));
			            
			            // Calculate cess amount
			            BigDecimal cessAmount = calculateCessAmount(new BigDecimal(e.getValue()));
			            
			            Employee employee = employees.stream().filter(emp->emp.getEmployeeId() == e.getKey()).findAny().get();
			           
			            // Create an EmployeeTaxDeduction object
			            EmployeeTaxDeduction employeeTaxDeduction = new EmployeeTaxDeduction();
			            employeeTaxDeduction.setEmployeeCode(employee.getEmployeeId());
			            employeeTaxDeduction.setFirstName(employee.getFirstName());
			            employeeTaxDeduction.setLastName(employee.getLastName());
			            employeeTaxDeduction.setYearlySalary(e.getValue());
			            employeeTaxDeduction.setTaxAmount(taxAmount.doubleValue());
			            employeeTaxDeduction.setCessAmount(cessAmount.doubleValue());
			            
			            // Add the EmployeeTaxDeduction object to the list
			            employeeTaxDeductions.add(employeeTaxDeduction);
					});
			 
			resp = new ResponseDTO("00", "Success", employeeTaxDeductions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resp;
		
	}
	
	private BigDecimal calculateTaxAmount(BigDecimal totalSalary) {
		
		 // Tax slabs
       BigDecimal taxSlab1 = new BigDecimal(250000);
       BigDecimal taxSlab2 = new BigDecimal(500000);
       BigDecimal taxSlab3 = new BigDecimal(1000000);

       // Tax rates
       BigDecimal taxRate1 = new BigDecimal(0);
       BigDecimal taxRate2 = new BigDecimal(0.05);
       BigDecimal taxRate3 = new BigDecimal(0.1);
       BigDecimal taxRate4 = new BigDecimal(0.2);

       // Calculate tax amount
       BigDecimal taxAmount=null;
       if (totalSalary.doubleValue() <= taxSlab1.doubleValue()) {
       	// No Tax
    	   taxAmount = taxRate1.multiply(totalSalary);
       }else if (totalSalary.doubleValue() > taxSlab1.doubleValue() && totalSalary.doubleValue() <= taxSlab2.doubleValue()) {
    	// 5% Tax
       	taxAmount = taxRate2.multiply(totalSalary);
       }else if (totalSalary.doubleValue() > taxSlab2.doubleValue() && totalSalary.doubleValue() <= taxSlab3.doubleValue()) {
    	// 10% Tax
         taxAmount = taxRate3.multiply(totalSalary);	
       } else if (totalSalary.doubleValue() > taxSlab3.doubleValue()) {
    	// 20% Tax
          taxAmount = taxRate4.multiply(totalSalary);
      }
       
		return taxAmount;
	}
	
	
	private BigDecimal calculateCessAmount(BigDecimal totalSalary) {
		 BigDecimal cessValue = new BigDecimal(0);
		 BigDecimal cessRate = new BigDecimal(0.02);
		 BigDecimal salaryCondition = new BigDecimal(2500000);
		 
		 if(totalSalary.doubleValue()>salaryCondition.doubleValue()) {
			 cessValue = cessRate.multiply(totalSalary);
		 }
		
		return cessValue;
	}
	
	
	

}
