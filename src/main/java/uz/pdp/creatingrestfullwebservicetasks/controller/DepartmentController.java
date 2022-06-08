package uz.pdp.creatingrestfullwebservicetasks.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.creatingrestfullwebservicetasks.entity.Department;
import uz.pdp.creatingrestfullwebservicetasks.payload.DepartmentDto;
import uz.pdp.creatingrestfullwebservicetasks.payload.Message;
import uz.pdp.creatingrestfullwebservicetasks.service.DepartmentService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/department")
public class DepartmentController {
    final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<Page<Department>> getDepartmentsController(@RequestParam int page) {
        return departmentService.getDepartmentsService(page);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Department> getDepartmentController(@PathVariable Integer id) {
        return departmentService.getDepartmentService(id);
    }

    @PostMapping
    public ResponseEntity<Message> addDepartmentController(@Valid @RequestBody DepartmentDto departmentDto) {
        return departmentService.addDepartmentService(departmentDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Message> editDepartmentController(@Valid @RequestBody DepartmentDto departmentDto, @PathVariable Integer id) {
        return departmentService.editDepartmentService(id, departmentDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Message> deleteDepartmentController(@PathVariable Integer id) {
        return departmentService.deleteDepartmentService(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

