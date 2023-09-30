package loc.test.emptaxdeduction.payload;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class EmployeePayload {
	
	@Min(value = 0, message = "The employee Id Should be valid number")
	private int employeeId;

	@NotNull(message = "FirstName should not be null")
	@NotEmpty(message = "FirstName should not be empty")
	private String firstName;

	@NotNull(message = "LastName should not be null")
	@NotEmpty(message = "LastName should not be empty")
	private String lastName;

	@Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", message = "Please provide valid email")
	private String email;

	@NotNull(message = "Phone Number list should not be null")
	@NotEmpty(message = "Phone Number list should not be empty")
	private String phoneNumbers;

	@NotNull(message = "DOJ should not be null")
	@NotEmpty(message = "DOJ should not be empty")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private String doj;

	@Min(value = 0L, message = "The salary value must be valid number")
	private double monthlySalary;
	
	@NotNull(message = "SalaryCreditDate should not be null")
	@NotEmpty(message = "SalaryCreditDate should not be empty")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private String salaryCreditDate;
	

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(String phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public String getDoj() {
		return doj;
	}

	public void setDoj(String doj) {
		this.doj = doj;
	}

	public double getMonthlySalary() {
		return monthlySalary;
	}

	public void setMonthlySalary(double monthlySalary) {
		this.monthlySalary = monthlySalary;
	}
	
	public String getSalaryCreditDate() {
		return salaryCreditDate;
	}

	public void setSalaryCreditDate(String salaryCreditDate) {
		this.salaryCreditDate = salaryCreditDate;
	}

}
