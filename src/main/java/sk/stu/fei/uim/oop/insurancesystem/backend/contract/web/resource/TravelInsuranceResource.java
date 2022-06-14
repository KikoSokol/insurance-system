package sk.stu.fei.uim.oop.insurancesystem.backend.contract.web.resource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.PurposeOfTrip;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.TravelInsurance;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TravelInsuranceResource extends LifeInsuranceResource
{
    protected boolean inEurope;
    @NotNull
    protected PurposeOfTrip purposeOfTrip;

    public TravelInsuranceResource(TravelInsurance travelInsurance)
    {
        super.idContract = travelInsurance.getIdContract();
        super.insurer = travelInsurance.getInsurer();
        super.startOfInsurance = travelInsurance.getStartOfInsurance();
        super.endOfInsurance = travelInsurance.getEndOfInsurance();
        super.amountOfInsuranceIndemnity = travelInsurance.getAmountOfInsuranceIndemnity();
        super.monthlyPayment = travelInsurance.getMonthlyPayment();
        super.insured = travelInsurance.getInsured();
        this.inEurope = travelInsurance.isInEurope();
        this.purposeOfTrip = travelInsurance.getPurposeOfTrip();
    }

    public TravelInsurance toTravelInsurance(Long id, LocalDateTime dateOfFormation)
    {
        return new TravelInsurance(id,dateOfFormation, super.insurer, super.startOfInsurance, super.endOfInsurance, super.amountOfInsuranceIndemnity, super.monthlyPayment, super.insured, this.inEurope, this.purposeOfTrip);
    }

}
