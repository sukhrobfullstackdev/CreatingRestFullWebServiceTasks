package uz.pdp.creatingrestfullwebservicetasks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.creatingrestfullwebservicetasks.entity.Worker;
import uz.pdp.creatingrestfullwebservicetasks.payload.Message;
import uz.pdp.creatingrestfullwebservicetasks.payload.WorkerDto;
import uz.pdp.creatingrestfullwebservicetasks.service.WorkerService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/worker")
public class WorkerController {
    final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping
    public ResponseEntity<Page<Worker>> getWorkersController(@RequestParam int page) {
        return workerService.getWorkersService(page);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Worker> getWorkerController(@PathVariable Integer id) {
        return workerService.getWorkerService(id);
    }

    @PostMapping
    public ResponseEntity<Message> addWorkerController(@Valid @RequestBody WorkerDto workerDto) {
        return workerService.addWorkerService(workerDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Message> editWorkerController(@Valid @RequestBody WorkerDto workerDto, @PathVariable Integer id) {
        return workerService.editWorkerService(id,workerDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Message> deleteWorkerController(@PathVariable Integer id) {
        return workerService.deleteWorkerService(id);
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
