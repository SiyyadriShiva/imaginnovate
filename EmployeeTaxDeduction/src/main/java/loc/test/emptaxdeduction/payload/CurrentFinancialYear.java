package loc.test.emptaxdeduction.payload;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class CurrentFinancialYear {
	@NotNull(message = "Date should not be null")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private String fromDate;
	
	@NotNull(message = "Date should not be null")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private String toDate;
	
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
}
