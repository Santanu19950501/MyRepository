select e1.EmpName, s1.salary from Employee e1,Salary s1 where e1.EmpID=s1.EmpID and
 0=(select count(distinct Salary) from Salary s2 where s2.Salary>s1.salary);
update Employee,Salary set Salary.Salary = '5000' where Employee.EmpID=Salary.EmpID and 
timestampdiff(YEAR,Employee.Date_of_Birth,curdate())>30;
select Employee.EmpName,Salary.Salary,Employee.Date_of_Birth,timestampdiff(YEAR,Employee.Date_of_Birth,curdate()) 
as Age from Employee, Salary where Employee.EmpID=Salary.EmpID;
