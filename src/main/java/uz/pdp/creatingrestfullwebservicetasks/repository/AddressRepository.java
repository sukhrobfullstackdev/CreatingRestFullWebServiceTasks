package uz.pdp.creatingrestfullwebservicetasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.creatingrestfullwebservicetasks.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address,Integer> {
}
