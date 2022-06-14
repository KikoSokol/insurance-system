package sk.stu.fei.uim.oop.insurancesystem.backend.contract.web.resource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import sk.stu.fei.uim.oop.insurancesystem.backend.address.Address;
import sk.stu.fei.uim.oop.insurancesystem.backend.address.attribute.PostalCodeParseException;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.HouseholdInsurance;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HouseholdInsuranceResource extends NonLifeInsuranceResource
{
    @Positive
    protected double valueOfHouseholdAppliances;

    public HouseholdInsuranceResource(HouseholdInsurance householdInsurance)
    {
        super.idContract = householdInsurance.getIdContract();
        super.insurer = householdInsurance.getInsurer();
        super.startOfInsurance = householdInsurance.getStartOfInsurance();
        super.endOfInsurance = householdInsurance.getEndOfInsurance();
        super.amountOfInsuranceIndemnity = householdInsurance.getAmountOfInsuranceIndemnity();
        super.monthlyPayment = householdInsurance.getMonthlyPayment();
        super.propertyType = householdInsurance.getPropertyType();
        Address address = householdInsurance.getAddress();
        super.street = address.getNameOfStreet();
        super.numberOfHouse = address.getNumberOfHouse();
        super.city = address.getNameOfCity();
        super.postalCode = address.getPostalCode().getPostalCode();
        super.valueOfProperty = householdInsurance.getValueOfProperty();
        this.valueOfHouseholdAppliances = householdInsurance.getValueOfHouseholdAppliences();
    }

    public HouseholdInsurance toHouseholdInsurance(long id, LocalDateTime dateOfFormation) throws PostalCodeParseException {
        return new HouseholdInsurance(id,dateOfFormation,super.insurer,super.startOfInsurance,super.endOfInsurance,super.amountOfInsuranceIndemnity,super.monthlyPayment,super.propertyType,super.getAddress(),super.valueOfProperty,this.valueOfHouseholdAppliances);
    }
}
