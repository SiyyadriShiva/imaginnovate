package loc.test.emptaxdeduction.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import loc.test.emptaxdeduction.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	@Query(value = "select * from employee_tbl where to_date(SALARY_CREDIT_DATE) between to_date(:dateFrom,'DD-MM-YYYY') and  to_date(:dateTo,'DD-MM-YYYY')", nativeQuery = true)
	List<Employee> getFinancialReportBasedOnUserId(@Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo);
}
