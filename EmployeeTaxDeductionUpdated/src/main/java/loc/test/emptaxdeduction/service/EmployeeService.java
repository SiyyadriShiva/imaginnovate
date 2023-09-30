package loc.test.emptaxdeduction.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

		ResponseDTO<Employee> resp = new ResponseDTO("90", "Issue in storing employee details in DB", saveEmp);
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
	public ResponseDTO<List<EmployeeTaxDeduction>> getEmployeeTaxDeductionForCurrentFinancialYear(
			CurrentFinancialYear finYear) {

		// Get employees list from DB
		List<Employee> employees = employeeRepository.getFinancialReportBasedOnUserId(finYear.getFromDate(),
				finYear.getToDate());

		// Initialize response data
		List<EmployeeTaxDeduction> employeeTaxDeductions = new ArrayList();

		// Prepare non success response
		ResponseDTO<List<EmployeeTaxDeduction>> resp = new ResponseDTO("90", "Issue in Employee Tax Deduction",
				employeeTaxDeductions);
		try {

			// Get Employee List who joined on May 16th
			List<Employee> empList1 = getReqEmpList(employees).get(true);
			if (empList1.size() > 0) {
				List<Employee> empModifyList1 = getEmpListOnGivenCondition(empList1);
				onCalEmpTaxDeduction(empModifyList1, employeeTaxDeductions);
			}

			// Get Employee List who joined not on May 16th
			List<Employee> empList2 = getReqEmpList(employees).get(false);
			if (empList2.size() > 0) {
				onCalEmpTaxDeduction(empList2, employeeTaxDeductions);
			}

			// Prepare success response
			resp = new ResponseDTO("00", "Success", employeeTaxDeductions);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resp;
	}

	// Spilt Employees based on DOJ
	private Map<Boolean, List<Employee>> getReqEmpList(List<Employee> empList) {
		Map<Boolean, List<Employee>> empPartitions = empList.stream().collect(Collectors.partitioningBy((user) -> {
			String spliter[] = user.getDoj().split("-");
			if (spliter[0].equals("16") && spliter[1].equals("05")) {
				return true;
			}
			return false;
		}));

		return empPartitions;
	}

	// Filter Employees on given condition(s)
	private List<Employee> getEmpListOnGivenCondition(List<Employee> empList) {
		return empList.stream().filter((emp) -> {
			String spliter[] = emp.getSalaryCreditDate().split("-");
			if (!spliter[1].equals("04")) {
				return true;
			}
			return false;
		}).map((emp) -> {
			String spliter[] = emp.getSalaryCreditDate().split("-");
			if (spliter[1].equals("05")) {
				double daysOfFifteenSalary = 15 * (emp.getMonthlySalary() / 30);
				emp.setMonthlySalary(emp.getMonthlySalary() - daysOfFifteenSalary);
			}
			return emp;
		}).collect(Collectors.toList());

	}

	// Start Calculation of Employee Tax Deduction Here
	private void onCalEmpTaxDeduction(List<Employee> empList, List<EmployeeTaxDeduction> respList) {
		empList.stream().collect(
				Collectors.groupingBy(Employee::getEmployeeId, Collectors.summingDouble(Employee::getMonthlySalary)))
				.entrySet().forEach((e) -> {
					calEmpTaxDeductionAndIncludeInResponseList(empList, e, respList);
				});
	}

	// Calculate Employee Tax Deduction and include in response list
	private void calEmpTaxDeductionAndIncludeInResponseList(List<Employee> list, Map.Entry<Integer, Double> emEntrySet,
			List<EmployeeTaxDeduction> respList) {
		int empId = emEntrySet.getKey();
		double empYearlySalary = emEntrySet.getValue();

		BigDecimal taxAmount = calculateTaxAmount(new BigDecimal(empYearlySalary));

		BigDecimal cessAmount = calculateCessAmount(new BigDecimal(empYearlySalary));

		Employee employee = getEmployeeByEmpId(list, empId);

		// Create an EmployeeTaxDeduction Object
		EmployeeTaxDeduction employeeTaxDeduction = new EmployeeTaxDeduction();
		employeeTaxDeduction.setEmployeeCode(employee.getEmployeeId());
		employeeTaxDeduction.setFirstName(employee.getFirstName());
		employeeTaxDeduction.setLastName(employee.getLastName());
		employeeTaxDeduction.setYearlySalary(empYearlySalary);
		employeeTaxDeduction.setTaxAmount(taxAmount.doubleValue());
		employeeTaxDeduction.setCessAmount(cessAmount.doubleValue());

		// Add the EmployeeTaxDeduction object to the list
		respList.add(employeeTaxDeduction);
	}

	// Get Employee Tax Amount
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
		BigDecimal taxAmount = null;
		if (totalSalary.doubleValue() <= taxSlab1.doubleValue()) {
			// No Tax
			taxAmount = taxRate1.multiply(totalSalary);
		} else if (totalSalary.doubleValue() > taxSlab1.doubleValue()
				&& totalSalary.doubleValue() <= taxSlab2.doubleValue()) {
			// 5% Tax
			taxAmount = taxRate2.multiply(totalSalary);
		} else if (totalSalary.doubleValue() > taxSlab2.doubleValue()
				&& totalSalary.doubleValue() <= taxSlab3.doubleValue()) {
			// 10% Tax
			taxAmount = taxRate3.multiply(totalSalary);
		} else if (totalSalary.doubleValue() > taxSlab3.doubleValue()) {
			// 20% Tax
			taxAmount = taxRate4.multiply(totalSalary);
		}

		return taxAmount;
	}

	// Get Employee cess amount
	private BigDecimal calculateCessAmount(BigDecimal totalSalary) {
		BigDecimal cessValue = new BigDecimal(0);
		BigDecimal cessRate = new BigDecimal(0.02);
		BigDecimal salaryCondition = new BigDecimal(2500000);

		if (totalSalary.doubleValue() > salaryCondition.doubleValue()) {
			cessValue = cessRate.multiply(totalSalary);
		}

		return cessValue;
	}

	// Get Employee details by Employee Id
	private Employee getEmployeeByEmpId(List<Employee> list, int empId) {
		return list.stream().filter(emp -> emp.getEmployeeId() == empId).findAny().get();
	}
}
