package uz.pdp.creatingrestfullwebservicetasks.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CompanyDto {
    @NotNull(message = "Please fill the company name!")
    private String corpName;
    @NotNull(message = "Please fill the director name!")
    private String directorName;
    @NotNull(message = "Please fill the street where the company is located!")
    private String street;
    @NotNull(message = "Please fill the home number where the company is located!")
    private String homeNumber;
}
