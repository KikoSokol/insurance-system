package sk.stu.fei.uim.oop.insurancesystem.backend.contract.service;

import org.springframework.stereotype.Service;
import sk.stu.fei.uim.oop.insurancesystem.backend.address.Address;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.Contract;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.PropertyType;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.PurposeOfTrip;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.TerritorialValidity;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.domain.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public interface ContractService
{
    Contract registerTravelInsurance(User insurer, LocalDateTime start, LocalDateTime end, double amountOfInsuranceIndemnity, double monthlyPayment, User insured, boolean inEurope, PurposeOfTrip purposeOfTrip);
    Contract registerAccidentInsurance(User insurer, LocalDateTime startOfInsurance, LocalDateTime endOfInsurance, double amountOfInsuranceIndemnity, double monthlyPayment, User insured, double sumInsuredOfLastingConsequences, double sumInsuredOfDeath, double sumInsuredOfDailyCompensation, TerritorialValidity territorialValidity);
    Contract registerHouseFlatInsurance(User insurer, LocalDateTime startOfInsurance, LocalDateTime endOfInsurance, double amountOfInsuranceIndemnity, double monthlyPayment, PropertyType propertyType, Address address, double valueOfProperty, boolean isGarageInsurance);
    Contract registerHouseHoldInsurance(User insurer, LocalDateTime startOfInsurance, LocalDateTime endOfInsurance, double amountOfInsuranceIndemnity, double monthlyPayment, PropertyType propertyType, Address address, double valueOfProperty, double valueOfHouseholdAppliances);

    Optional<Contract> findContractById(long idContract);
}
