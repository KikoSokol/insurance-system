package sk.stu.fei.uim.oop.insurancesystem.backend.contract.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sk.stu.fei.uim.oop.insurancesystem.backend.InsuranceSystemService;
import sk.stu.fei.uim.oop.insurancesystem.backend.address.Address;
import sk.stu.fei.uim.oop.insurancesystem.backend.address.attribute.PostalCodeParseException;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.Contract;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.HouseFlatInsurance;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.HouseholdInsurance;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.PropertyType;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.web.resource.HouseFlatInsuranceResource;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.web.resource.HouseholdInsuranceResource;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.domain.User;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/contract/householdInsurance")
public class HouseholdInsuranceController
{
    private final InsuranceSystemService insuranceSystemService;

    public HouseholdInsuranceController(InsuranceSystemService insuranceSystemService) {
        this.insuranceSystemService = insuranceSystemService;
    }

    @GetMapping("/id/{id}")
    public String infoHouseholdInsurance(@PathVariable long id, Model model)
    {
        Contract contract = this.insuranceSystemService.getContractService().findContractById(id).get();
        HouseholdInsurance householdInsurance = (HouseholdInsurance) contract;
        model.addAttribute("houseHoldInsurance",householdInsurance);
        User insurer = this.insuranceSystemService.getUserService().findUserById(householdInsurance.getInsurer()).get();
        model.addAttribute("insurerHouseHoldInsurance",insurer);
        return "/contract/household/household";
    }

    @GetMapping("/add")
    public String addHouseholdInsurance(Model model)
    {
        model.addAttribute("newHouseholdInsurance",new HouseholdInsuranceResource());
        model.addAttribute("userHouseholdInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("propertyHouseHoldTypeInsurance", PropertyType.values());
        return "/contract/household/add";
    }

    @PostMapping("/add")
    public String addHouseholdInsuranceSubmit(@ModelAttribute("newHouseholdInsurance") @Valid HouseholdInsuranceResource householdInsuranceResource, BindingResult bindingResult,Model model)
    {
        model.addAttribute("userHouseholdInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("propertyHouseHoldTypeInsurance", PropertyType.values());
        checkDate(householdInsuranceResource.getStartOfInsurance(),householdInsuranceResource.getEndOfInsurance(),bindingResult);
        if(bindingResult.hasErrors())
        {
            return "/contract/household/add";
        }

        addContract(householdInsuranceResource,bindingResult);

        if(bindingResult.hasErrors())
        {
            return "/contract/household/add";
        }

        return "redirect:/contract/";
    }


    @GetMapping("/update/id/{id}")
    public String updateHouseholdInsurance(@PathVariable long id, Model model)
    {
        Contract contract = this.insuranceSystemService.getContractService().findContractById(id).get();
        model.addAttribute("updateHouseholdInsurance",new HouseholdInsuranceResource((HouseholdInsurance)contract));
        model.addAttribute("userHouseholdInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("propertyHouseHoldTypeInsurance", PropertyType.values());
        return "/contract/household/update";
    }

    @PostMapping("/update/submit/id/{id}")
    public String updateHouseholdInsuranceSubmit(@PathVariable long id, @ModelAttribute("updateHouseholdInsurance") @Valid HouseholdInsuranceResource householdInsuranceResource,BindingResult bindingResult,Model model)
    {
        householdInsuranceResource.setIdContract(id);
        model.addAttribute("userHouseholdInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("propertyHouseHoldTypeInsurance", PropertyType.values());
        checkDate(householdInsuranceResource.getStartOfInsurance(),householdInsuranceResource.getEndOfInsurance(),bindingResult);
        if(bindingResult.hasErrors())
        {
            return "/contract/household/update";
        }

        Contract oldHouseholdInsurance = this.insuranceSystemService.getContractService().findContractById(id).get();

        try
        {
            Contract updatedHouseholdInsurance = householdInsuranceResource.toHouseholdInsurance(oldHouseholdInsurance.getIdContract(), oldHouseholdInsurance.getDateTimeOfFormation());
            User oldInsurer = this.insuranceSystemService.getUserService().findUserById(oldHouseholdInsurance .getInsurer()).get();
            User newInsurer = this.insuranceSystemService.getUserService().findUserById(updatedHouseholdInsurance.getInsurer()).get();
            this.insuranceSystemService.getUpdateContractService().updateContract(updatedHouseholdInsurance,oldInsurer,newInsurer);
        }
        catch (PostalCodeParseException e)
        {
            bindingResult.rejectValue("postalCode","postalCode",e.getMessage());
        }

        if(bindingResult.hasErrors())
        {
            return "/contract/household/update";
        }
        return "redirect:/contract/";
    }

    @GetMapping("/add/toUser/{id}")
    public String addHouseholdInsuranceForInsurer(@PathVariable long id, Model model)
    {
        User user = this.insuranceSystemService.getUserService().findUserById(id).get();
        HouseholdInsuranceResource householdInsuranceResource = new HouseholdInsuranceResource();
        householdInsuranceResource.setInsurer(id);
        model.addAttribute("newHouseholdInsurance",householdInsuranceResource);
        model.addAttribute("insurerToHouseholdInsurance",user);
        model.addAttribute("userHouseholdInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("propertyHouseHoldTypeInsurance", PropertyType.values());
        return "/contract/household/addForInsurer";
    }


    @PostMapping("/add/toUser/{id}")
    public String addHouseholdInsuranceForInsurerSubmit(@PathVariable long id, @ModelAttribute("newHouseholdInsurance") @Valid HouseholdInsuranceResource householdInsuranceResource, BindingResult bindingResult,Model model)
    {
        User user = this.insuranceSystemService.getUserService().findUserById(id).get();
        model.addAttribute("insurerToHouseholdInsurance",user);
        model.addAttribute("userHouseholdInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("propertyHouseHoldTypeInsurance", PropertyType.values());
        checkDate(householdInsuranceResource.getStartOfInsurance(),householdInsuranceResource.getEndOfInsurance(),bindingResult);
        if(bindingResult.hasErrors())
        {
            return "/contract/household/addForInsurer";
        }

        addContract(householdInsuranceResource,bindingResult);

        if(bindingResult.hasErrors())
        {
            return "/contract/household/addForInsurer";
        }

        return "redirect:/contract/";
    }


    private void checkDate(LocalDateTime start, LocalDateTime end, BindingResult bindingResult)
    {
        if(start != null && end != null && start.isAfter(end))
        {
            bindingResult.rejectValue("startOfInsurance","startOfInsurance","Date of start insurance must be before date of end insurance");
            bindingResult.rejectValue("endOfInsurance","endOfInsurance","Date of end insurance must be after date of start insurance");
        }
    }

    private void addContract(HouseholdInsuranceResource householdInsuranceResource,BindingResult bindingResult)
    {
        try
        {
            LocalDateTime start = householdInsuranceResource.getStartOfInsurance();
            LocalDateTime end = householdInsuranceResource.getEndOfInsurance();
            User insurer = this.insuranceSystemService.getUserService().findUserById(householdInsuranceResource.getInsurer()).get();
            double amoutOfInsuranceIndemnity = householdInsuranceResource.getAmountOfInsuranceIndemnity();
            double monthlyPayment = householdInsuranceResource.getMonthlyPayment();
            PropertyType propertyType = householdInsuranceResource.getPropertyType();
            Address address = householdInsuranceResource.getAddress();
            double valueOfProperty = householdInsuranceResource.getValueOfProperty();
            double valueOfHouseholdAppliances = householdInsuranceResource.getValueOfHouseholdAppliances();
            this.insuranceSystemService.getContractService().registerHouseHoldInsurance(insurer,start,end,amoutOfInsuranceIndemnity,monthlyPayment,propertyType,address,valueOfProperty,valueOfHouseholdAppliances);
        }
        catch (PostalCodeParseException e)
        {
            bindingResult.rejectValue("postalCode","postalCode",e.getMessage());
        }
    }
}
