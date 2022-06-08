package uz.pdp.creatingrestfullwebservicetasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.creatingrestfullwebservicetasks.entity.Address;
import uz.pdp.creatingrestfullwebservicetasks.entity.Company;
import uz.pdp.creatingrestfullwebservicetasks.payload.CompanyDto;
import uz.pdp.creatingrestfullwebservicetasks.payload.Message;
import uz.pdp.creatingrestfullwebservicetasks.repository.AddressRepository;
import uz.pdp.creatingrestfullwebservicetasks.repository.CompanyRepository;

import java.util.Objects;
import java.util.Optional;

@Service
public class CompanyService {
    final CompanyRepository companyRepository;
    final AddressRepository addressRepository;

    public CompanyService(CompanyRepository companyRepository, AddressRepository addressRepository) {
        this.companyRepository = companyRepository;
        this.addressRepository = addressRepository;
    }

    public Page<Company> getCompaniesService(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return companyRepository.findAll(pageable);
    }

    public Company getCompanyService(Integer id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        return optionalCompany.orElse(null);
    }

    public ResponseEntity<Message> addCompanyService(CompanyDto companyDto) {
        if (!Objects.equals(companyDto.getCorpName(), "") && !Objects.equals(companyDto.getDirectorName(), "") && !Objects.equals(companyDto.getStreet(), "") && !Objects.equals(companyDto.getHomeNumber(), "")) {
            boolean exists = companyRepository.existsByCorpNameOrAddress_HomeNumberAndAddress_Street(companyDto.getCorpName(), companyDto.getHomeNumber(), companyDto.getStreet());
            if (!exists) {
                Address address = new Address(companyDto.getStreet(), companyDto.getHomeNumber());
                Address savedAddress = addressRepository.save(address);
                Company company = new Company(companyDto.getCorpName(), companyDto.getDirectorName(), savedAddress);
                companyRepository.save(company);
                return ResponseEntity.status(HttpStatus.CREATED).body(new Message(true, "The company has been successfully added!"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "This company name is already exists or another company is located in this address!"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "Please fill all fields fully!"));
        }
    }

    public ResponseEntity<Message> editCompanyService(Integer id, CompanyDto companyDto) {
        if (!Objects.equals(companyDto.getCorpName(), "") && !Objects.equals(companyDto.getDirectorName(), "") && !Objects.equals(companyDto.getStreet(), "") && !Objects.equals(companyDto.getHomeNumber(), "")) {
            Optional<Company> optionalCompany = companyRepository.findById(id);
            if (optionalCompany.isPresent()) {
                boolean existsByCorpName = companyRepository.existsByCorpNameAndIdNot(companyDto.getCorpName(), id);
                boolean existsByAddress = companyRepository.existsByAddress_HomeNumberAndAddress_StreetAndIdNot(companyDto.getHomeNumber(), companyDto.getStreet(), id);
                if (!existsByCorpName && !existsByAddress) {
                    Company company = optionalCompany.get();
                    Address address = company.getAddress();
                    address.setStreet(companyDto.getStreet());
                    address.setHomeNumber(companyDto.getHomeNumber());
                    addressRepository.save(address);
                    company.setCorpName(company.getCorpName());
                    company.setDirectorName(company.getDirectorName());
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Message(true, "The company has been successfully edited!"));
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "The company info that you entered has already assigned to another company!"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false, "The company that you want to edit has not been found!"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "Please fill all fields fully!"));
        }
    }

    public ResponseEntity<Message> deleteCompanyService(Integer id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            companyRepository.delete(optionalCompany.get());
            addressRepository.delete(optionalCompany.get().getAddress());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Message(true, "The company has been successfully deleted!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false, "The company that you want to delete has not been found!"));
        }
    }
}
