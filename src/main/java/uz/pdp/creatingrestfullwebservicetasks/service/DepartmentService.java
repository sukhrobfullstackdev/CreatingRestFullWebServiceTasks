package uz.pdp.creatingrestfullwebservicetasks.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.creatingrestfullwebservicetasks.entity.Company;
import uz.pdp.creatingrestfullwebservicetasks.entity.Department;
import uz.pdp.creatingrestfullwebservicetasks.payload.DepartmentDto;
import uz.pdp.creatingrestfullwebservicetasks.payload.Message;
import uz.pdp.creatingrestfullwebservicetasks.repository.CompanyRepository;
import uz.pdp.creatingrestfullwebservicetasks.repository.DepartmentRepository;

import java.util.Objects;
import java.util.Optional;

@Service
public class DepartmentService {
    final DepartmentRepository departmentRepository;
    final CompanyRepository companyRepository;

    public DepartmentService(DepartmentRepository departmentRepository, CompanyRepository companyRepository) {
        this.departmentRepository = departmentRepository;
        this.companyRepository = companyRepository;
    }

    public ResponseEntity<Page<Department>> getDepartmentsService(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(departmentRepository.findAll(pageable));
    }

    public ResponseEntity<Department> getDepartmentService(Integer id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        return optionalDepartment.map(department -> ResponseEntity.status(HttpStatus.OK).body(department)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    public ResponseEntity<Message> addDepartmentService(DepartmentDto departmentDto) {
        if (!Objects.equals(departmentDto.getName(), "")) {
            Optional<Company> optionalCompany = companyRepository.findById(departmentDto.getCompanyId());
            if (optionalCompany.isPresent()) {
                Company company = optionalCompany.get();
                boolean exists = departmentRepository.existsByNameAndCompanyId(departmentDto.getName(), company.getId());
                if (!exists) {
                    departmentRepository.save(new Department(departmentDto.getName(), company));
                    return ResponseEntity.status(HttpStatus.CREATED).body(new Message(true, "The department has been successfully added!"));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(false, "This department is already exists in this company!"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false, "The company has not been found to assign a department to it!"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(false, "Please enter the department name!"));
        }
    }

    public ResponseEntity<Message> editDepartmentService(Integer id, DepartmentDto departmentDto) {
        if (!Objects.equals(departmentDto.getName(), "")) {
            Optional<Department> optionalDepartment = departmentRepository.findById(id);
            if (optionalDepartment.isPresent()) {
                Optional<Company> optionalCompany = companyRepository.findById(departmentDto.getCompanyId());
                if (optionalCompany.isPresent()) {
                    Department department = optionalDepartment.get();
                    Company company = optionalCompany.get();
                    boolean exists = departmentRepository.existsByNameAndCompanyAndIdNot(department.getName(), company, id);
                    if (!exists) {
                        department.setName(departmentDto.getName());
                        department.setCompany(company);
                        departmentRepository.save(department);
                        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Message(true, "The department has been successfully edited!"));
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(true, "This department is already exists in this company!"));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false, "The company that you want to assign to this department has not been found!"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(true, "The department that you want to edit has not been found!"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "Please fill department name and choose company to assign to it!"));
        }
    }

    public ResponseEntity<Message> deleteDepartmentService(Integer id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()) {
            departmentRepository.delete(optionalDepartment.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Message(true, "The department has been successfully deleted!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false, "The department has not been found to delete!"));
        }
    }
}
