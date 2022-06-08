package uz.pdp.creatingrestfullwebservicetasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.creatingrestfullwebservicetasks.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    boolean existsByCorpNameOrAddress_HomeNumberAndAddress_Street(String corpName, String address_homeNumber, String address_street);
    boolean existsByCorpNameAndIdNot(String corpName, Integer id);
    boolean existsByAddress_HomeNumberAndAddress_StreetAndIdNot(String address_homeNumber, String address_street, Integer id);
}
