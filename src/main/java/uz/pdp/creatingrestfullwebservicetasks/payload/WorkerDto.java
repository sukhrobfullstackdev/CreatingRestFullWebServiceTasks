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
public class WorkerDto {
    @NotNull(message = "Please enter the worker's name!")
    private String name;
    @NotNull(message = "Please enter the worker's phone number!")
    private String phoneNumber;
    @NotNull(message = "Please enter the worker's street!")
    private String street;
    @NotNull(message = "Please enter the worker's home number!")
    private String homeNumber;
    @NotNull(message = "Please choose which department the worker works for!")
    private Integer departmentId;
}
