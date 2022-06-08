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
public class DepartmentDto {
    @NotNull(message = "Please fill the department name!")
    private String name;
    @NotNull(message = "Please choose the company to assign a department to it!")
    private Integer companyId;
}
