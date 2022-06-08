package uz.pdp.creatingrestfullwebservicetasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.creatingrestfullwebservicetasks.entity.Company;
import uz.pdp.creatingrestfullwebservicetasks.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Integer> {
    boolean existsByNameAndCompanyId(String name, Integer company_id);
    boolean existsByNameAndCompanyAndIdNot(String name, Company company, Integer id);
}
