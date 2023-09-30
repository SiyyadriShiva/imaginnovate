
-- Payloads for testing the service endpoints --

-> Store Employee details inDB endpoint details
[
URL:: http://localhost:8066/api/employee/storeEmpDetails
Request Type:: POST
PayLoad:: 
{
    "employeeId": 1,
    "firstName": "Siyyadri",
    "lastName": "Shiva",
    "email": "aaa@gamil.com",
    "phoneNumbers": "99999999,990000000",
    "doj": "16-04-2020",
    "monthlySalary": 140000,
    "salaryCreditDate": "28-02-2022"
}]

-> Get Employees Tax Deductions list for current Financial Year 
[
URL:: http://localhost:8066/api/employee/empTaxDeduction
Request Type:: GET
PayLoad:: 
{
    "fromDate":"01-04-2021",
    "toDate":"31-03-2022"
}]
