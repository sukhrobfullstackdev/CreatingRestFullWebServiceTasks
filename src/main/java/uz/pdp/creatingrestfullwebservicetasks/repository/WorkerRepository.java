package uz.pdp.creatingrestfullwebservicetasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.creatingrestfullwebservicetasks.entity.Worker;

@Repository
public interface WorkerRepository extends JpaRepository<Worker,Integer> {
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByAddress_HomeNumberAndAddress_StreetAndDepartment_IdAndName(String address_homeNumber, String address_street, Integer department_id, String name);
    boolean existsByNameAndPhoneNumberAndAddress_HomeNumberAndAddress_StreetAndDepartmentIdAndIdNot(String name, String phoneNumber, String address_homeNumber, String address_street, Integer department_id, Integer id);
}
