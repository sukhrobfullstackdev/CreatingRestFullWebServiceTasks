package uz.pdp.creatingrestfullwebservicetasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.creatingrestfullwebservicetasks.entity.Address;
import uz.pdp.creatingrestfullwebservicetasks.entity.Department;
import uz.pdp.creatingrestfullwebservicetasks.entity.Worker;
import uz.pdp.creatingrestfullwebservicetasks.payload.Message;
import uz.pdp.creatingrestfullwebservicetasks.payload.WorkerDto;
import uz.pdp.creatingrestfullwebservicetasks.repository.AddressRepository;
import uz.pdp.creatingrestfullwebservicetasks.repository.DepartmentRepository;
import uz.pdp.creatingrestfullwebservicetasks.repository.WorkerRepository;

import java.util.Objects;
import java.util.Optional;

@Service
public class WorkerService {
    final WorkerRepository workerRepository;
    final DepartmentRepository departmentRepository;
    final AddressRepository addressRepository;

    public WorkerService(WorkerRepository workerRepository, DepartmentRepository departmentRepository, AddressRepository addressRepository) {
        this.workerRepository = workerRepository;
        this.departmentRepository = departmentRepository;
        this.addressRepository = addressRepository;
    }

    public ResponseEntity<Page<Worker>> getWorkersService(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.status(HttpStatus.OK).body(workerRepository.findAll(pageable));
    }

    public ResponseEntity<Worker> getWorkerService(Integer id) {
        Optional<Worker> optionalWorker = workerRepository.findById(id);
        return optionalWorker.map(worker -> ResponseEntity.status(HttpStatus.OK).body(worker)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    public ResponseEntity<Message> addWorkerService(WorkerDto workerDto) {
        if (!Objects.equals(workerDto.getName(), "") && !Objects.equals(workerDto.getPhoneNumber(), "") && !Objects.equals(workerDto.getHomeNumber(), "") && !Objects.equals(workerDto.getStreet(), "") && !Objects.equals(workerDto.getDepartmentId(), 0)) {
            boolean exists = workerRepository.existsByPhoneNumber(workerDto.getPhoneNumber());
            boolean existsByFields = workerRepository.existsByAddress_HomeNumberAndAddress_StreetAndDepartment_IdAndName(workerDto.getHomeNumber(), workerDto.getStreet(), workerDto.getDepartmentId(), workerDto.getName());
            if (!exists && !existsByFields) {
                Optional<Department> optionalDepartment = departmentRepository.findById(workerDto.getDepartmentId());
                if (optionalDepartment.isPresent()) {
                    Department department = optionalDepartment.get();
                    Address address = addressRepository.save(new Address(workerDto.getStreet(), workerDto.getHomeNumber()));
                    workerRepository.save(new Worker(workerDto.getName(), workerDto.getPhoneNumber(), address, department));
                    return ResponseEntity.status(HttpStatus.CREATED).body(new Message(true, "The worker has been successfully added!"));
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false, "The department has not been found to assign a new worker to it!"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "This worker is already exists!"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "Please enter all fields fully!"));
        }
    }

    public ResponseEntity<Message> editWorkerService(Integer id, WorkerDto workerDto) {
        if (!Objects.equals(workerDto.getName(), "") && !Objects.equals(workerDto.getPhoneNumber(), "") && !Objects.equals(workerDto.getHomeNumber(), "") && !Objects.equals(workerDto.getStreet(), "") && !Objects.equals(workerDto.getDepartmentId(), 0)) {
            Optional<Worker> optionalWorker = workerRepository.findById(id);
            if (optionalWorker.isPresent()) {
                Worker worker = optionalWorker.get();
                boolean exists = workerRepository.existsByNameAndPhoneNumberAndAddress_HomeNumberAndAddress_StreetAndDepartmentIdAndIdNot(workerDto.getName(), workerDto.getPhoneNumber(), workerDto.getHomeNumber(), workerDto.getStreet(), workerDto.getDepartmentId(), id);
                if (!exists) {
                    Optional<Department> optionalDepartment = departmentRepository.findById(workerDto.getDepartmentId());
                    if (optionalDepartment.isPresent()) {
                        Address address = worker.getAddress();
                        address.setStreet(workerDto.getStreet());
                        address.setHomeNumber(workerDto.getHomeNumber());
                        addressRepository.save(address);
                        worker.setDepartment(optionalDepartment.get());
                        worker.setName(worker.getName());
                        worker.setPhoneNumber(worker.getPhoneNumber());
                        workerRepository.save(worker);
                        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Message(true, "The worker has been successfully edited!"));
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false, "The department has not been found to assign this worker to it!"));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(false, "This worker is already exists in this department"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false, "The worker that you want  to edit has not been found!"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "Please enter all fields fully"));
        }
    }

    public ResponseEntity<Message> deleteWorkerService(Integer id) {
        Optional<Worker> optionalWorker = workerRepository.findById(id);
        if (optionalWorker.isPresent()) {
            workerRepository.delete(optionalWorker.get());
            addressRepository.delete(optionalWorker.get().getAddress());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Message(true, "The worker has been successfully deleted!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false, "The worker that you want to delete has not been found to delete!"));
        }
    }
}
