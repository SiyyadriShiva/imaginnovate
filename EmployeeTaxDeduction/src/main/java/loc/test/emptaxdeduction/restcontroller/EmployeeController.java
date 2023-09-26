package loc.test.emptaxdeduction.restcontroller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import loc.test.emptaxdeduction.dto.ResponseDTO;
import loc.test.emptaxdeduction.entity.Employee;
import loc.test.emptaxdeduction.payload.CurrentFinancialYear;
import loc.test.emptaxdeduction.payload.EmployeePayload;
import loc.test.emptaxdeduction.response.EmployeeTaxDeduction;
import loc.test.emptaxdeduction.service.EmployeeService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
   
	@SuppressWarnings("rawtypes")
	@PostMapping(value="/storeEmpDetails")
	public ResponseEntity<ResponseDTO> getUserList(@RequestBody @Valid EmployeePayload empPayload){
		ResponseDTO<Employee> responseDTO = employeeService.storeEmployeeData(empPayload);
		return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(value="/empTaxDeduction")
	public ResponseEntity<ResponseDTO> getEmployeeTaxDeductionForCurrentFinancialYear(@RequestBody CurrentFinancialYear finYear){
		ResponseDTO<List<EmployeeTaxDeduction>> responseDTO = employeeService.getEmployeeTaxDeductionForCurrentFinancialYear(finYear);
		return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
	}
}
