package sk.stu.fei.uim.oop.insurancesystem.backend.contract.web.resource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import sk.stu.fei.uim.oop.insurancesystem.backend.address.Address;
import sk.stu.fei.uim.oop.insurancesystem.backend.address.attribute.PostalCodeParseException;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.HouseFlatInsurance;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HouseFlatInsuranceResource extends NonLifeInsuranceResource
{
    protected boolean garageInsurance;

    public HouseFlatInsuranceResource(HouseFlatInsurance houseFlatInsurance)
    {
        super.idContract = houseFlatInsurance.getIdContract();
        super.insurer = houseFlatInsurance.getInsurer();
        super.startOfInsurance = houseFlatInsurance.getStartOfInsurance();
        super.endOfInsurance = houseFlatInsurance.getEndOfInsurance();
        super.amountOfInsuranceIndemnity = houseFlatInsurance.getAmountOfInsuranceIndemnity();
        super.monthlyPayment = houseFlatInsurance.getMonthlyPayment();
        super.propertyType = houseFlatInsurance.getPropertyType();
        Address address = houseFlatInsurance.getAddress();
        super.street = address.getNameOfStreet();
        super.numberOfHouse = address.getNumberOfHouse();
        super.city = address.getNameOfCity();
        super.postalCode = address.getPostalCode().getPostalCode();
        super.valueOfProperty = houseFlatInsurance.getValueOfProperty();
        this.garageInsurance = houseFlatInsurance.isGarageInsurance();
    }

    public HouseFlatInsurance toHouseFlatInsurance(long id, LocalDateTime dateOfFormation) throws PostalCodeParseException {
        return new HouseFlatInsurance(id,dateOfFormation,super.insurer,super.startOfInsurance,super.endOfInsurance,super.amountOfInsuranceIndemnity,super.monthlyPayment,super.propertyType,super.getAddress(),super.valueOfProperty,this.garageInsurance);
    }
}
