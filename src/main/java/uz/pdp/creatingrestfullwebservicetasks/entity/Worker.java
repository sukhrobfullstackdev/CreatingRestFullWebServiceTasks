package uz.pdp.creatingrestfullwebservicetasks.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    @OneToOne(optional = false)
    private Address address;
    @ManyToOne(optional = false)
    private Department department;

    public Worker(String name, String phoneNumber, Address address, Department department) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.department = department;
    }
}
