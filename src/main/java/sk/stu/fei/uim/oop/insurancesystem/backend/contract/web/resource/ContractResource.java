package sk.stu.fei.uim.oop.insurancesystem.backend.contract.web.resource;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public abstract class ContractResource
{
    protected long idContract;
    @NotNull
    @Positive
    protected Long insurer;
    @NotNull
    @FutureOrPresent
    protected LocalDateTime startOfInsurance;
    @NotNull
    @FutureOrPresent
    protected LocalDateTime endOfInsurance;
    @Positive
    protected double amountOfInsuranceIndemnity;
    @Positive
    protected double monthlyPayment;
}
