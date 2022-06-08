package uz.pdp.creatingrestfullwebservicetasks.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.creatingrestfullwebservicetasks.entity.Company;
import uz.pdp.creatingrestfullwebservicetasks.payload.CompanyDto;
import uz.pdp.creatingrestfullwebservicetasks.payload.Message;
import uz.pdp.creatingrestfullwebservicetasks.service.CompanyService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/company")
public class CompanyController {
    final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<Page<Company>> getCompaniesController(@RequestParam int page) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getCompaniesService(page));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Company> getCompanyController(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getCompanyService(id));
    }

    @PostMapping
    public ResponseEntity<Message> addCompanyController(@Valid @RequestBody CompanyDto companyDto) {
        return companyService.addCompanyService(companyDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Message> editCompanyController(@Valid @RequestBody CompanyDto companyDto, @PathVariable Integer id) {
        return companyService.editCompanyService(id, companyDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Message> deleteCompanyController(@PathVariable Integer id) {
        return companyService.deleteCompanyService(id);
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
