package sk.stu.fei.uim.oop.insurancesystem.backend.contract.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sk.stu.fei.uim.oop.insurancesystem.backend.InsuranceSystemService;
import sk.stu.fei.uim.oop.insurancesystem.backend.address.Address;
import sk.stu.fei.uim.oop.insurancesystem.backend.address.attribute.PostalCodeParseException;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.*;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.web.resource.HouseFlatInsuranceResource;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.domain.User;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/contract/houseFlatInsurance")
public class HouseFlatInsuranceController
{
    private final InsuranceSystemService insuranceSystemService;

    public HouseFlatInsuranceController(InsuranceSystemService insuranceSystemService) {
        this.insuranceSystemService = insuranceSystemService;
    }

    @GetMapping("/id/{id}")
    public String infoHouseFlatInsurance(@PathVariable long id, Model model)
    {
        Contract contract = this.insuranceSystemService.getContractService().findContractById(id).get();
        HouseFlatInsurance houseFlatInsurance = (HouseFlatInsurance) contract;
        model.addAttribute("houseFlatInsurance",houseFlatInsurance);
        User insurer = this.insuranceSystemService.getUserService().findUserById(houseFlatInsurance.getInsurer()).get();
        model.addAttribute("insurerHouseFlatInsurance",insurer);
        return "/contract/houseFlat/houseFlat";
    }

    @GetMapping("/add")
    public String addHouseFlatInsurance(Model model)
    {
        model.addAttribute("newHouseFlatInsurance",new HouseFlatInsuranceResource());
        model.addAttribute("userHouseFlatInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("propertyTypeInsurance", PropertyType.values());
        return "/contract/houseFlat/add";
    }

    @PostMapping("/add")
    public String addHouseFlatInsuranceSubmit(@ModelAttribute("newHouseFlatInsurance") @Valid HouseFlatInsuranceResource houseFlatInsuranceResource, BindingResult bindingResult, Model model)
    {
        model.addAttribute("userHouseFlatInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("propertyTypeInsurance", PropertyType.values());
        checkDate(houseFlatInsuranceResource.getStartOfInsurance(),houseFlatInsuranceResource.getEndOfInsurance(),bindingResult);
        if(bindingResult.hasErrors())
        {
            return "/contract/houseFlat/add";
        }

        addContract(houseFlatInsuranceResource,bindingResult);

        if(bindingResult.hasErrors())
        {
            return "/contract/houseFlat/add";
        }

        return "redirect:/contract/";
    }

    @GetMapping("/update/id/{id}")
    public String updateHouseFlatInsurance(@PathVariable long id, Model model)
    {
        Contract contract = this.insuranceSystemService.getContractService().findContractById(id).get();
        model.addAttribute("updateHouseFlatInsurance",new HouseFlatInsuranceResource((HouseFlatInsurance)contract));
        model.addAttribute("userHouseFlatInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("propertyTypeInsurance", PropertyType.values());
        return "/contract/houseFlat/update";
    }

    @PostMapping("/update/submit/id/{id}")
    public String updateHouseFlatInsuranceSubmit(@PathVariable long id, @ModelAttribute("updateHouseFlatInsurance") @Valid HouseFlatInsuranceResource houseFlatInsuranceResource, BindingResult bindingResult,Model model)
    {
        houseFlatInsuranceResource.setIdContract(id);
        model.addAttribute("userHouseFlatInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("propertyTypeInsurance", PropertyType.values());
        checkDate(houseFlatInsuranceResource.getStartOfInsurance(),houseFlatInsuranceResource.getEndOfInsurance(),bindingResult);
        if(bindingResult.hasErrors())
        {
            return "/contract/houseFlat/update";
        }

        Contract oldHouseFlatInsurance = this.insuranceSystemService.getContractService().findContractById(id).get();
        try
        {
            Contract updatedHouseFlatInsurance = houseFlatInsuranceResource.toHouseFlatInsurance(oldHouseFlatInsurance.getIdContract(), oldHouseFlatInsurance.getDateTimeOfFormation());
            User oldInsurer = this.insuranceSystemService.getUserService().findUserById(oldHouseFlatInsurance .getInsurer()).get();
            User newInsurer = this.insuranceSystemService.getUserService().findUserById(updatedHouseFlatInsurance.getInsurer()).get();
            this.insuranceSystemService.getUpdateContractService().updateContract(updatedHouseFlatInsurance,oldInsurer,newInsurer);
        }
        catch (PostalCodeParseException e)
        {
            bindingResult.rejectValue("postalCode","postalCode",e.getMessage());
        }

        if(bindingResult.hasErrors())
        {
            return "/contract/houseFlat/update";
        }

        return "redirect:/contract/";
    }


    @GetMapping("/add/toUser/{id}")
    public String addHouseFlatInsuranceForInsurer(@PathVariable long id, Model model)
    {
        User user = this.insuranceSystemService.getUserService().findUserById(id).get();
        HouseFlatInsuranceResource houseFlatInsuranceResource = new HouseFlatInsuranceResource();
        houseFlatInsuranceResource.setInsurer(id);
        model.addAttribute("newHouseFlatInsurance",houseFlatInsuranceResource);
        model.addAttribute("insurerToHouseFlatInsurance",user);
        model.addAttribute("userHouseFlatInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("propertyTypeInsurance", PropertyType.values());
        return "/contract/houseFlat/addForInsurer";
    }

    @PostMapping("/add/toUser/{id}")
    public String addHouseFlatInsuranceForInsurerSubmit(@PathVariable long id, @ModelAttribute("newHouseFlatInsurance") @Valid HouseFlatInsuranceResource houseFlatInsuranceResource, BindingResult bindingResult, Model model)
    {
        User user = this.insuranceSystemService.getUserService().findUserById(id).get();
        model.addAttribute("insurerToHouseFlatInsurance",user);
        model.addAttribute("userHouseFlatInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("propertyTypeInsurance", PropertyType.values());
        checkDate(houseFlatInsuranceResource.getStartOfInsurance(),houseFlatInsuranceResource.getEndOfInsurance(),bindingResult);
        if(bindingResult.hasErrors())
        {
            return "/contract/houseFlat/addForInsurer";
        }

        addContract(houseFlatInsuranceResource,bindingResult);

        if(bindingResult.hasErrors())
        {
            return "/contract/houseFlat/addForInsurer";
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

    private void addContract(HouseFlatInsuranceResource houseFlatInsuranceResource, BindingResult bindingResult)
    {
        try
        {
            LocalDateTime start = houseFlatInsuranceResource.getStartOfInsurance();
            LocalDateTime end = houseFlatInsuranceResource.getEndOfInsurance();
            User insurer = this.insuranceSystemService.getUserService().findUserById(houseFlatInsuranceResource.getInsurer()).get();
            double amoutOfInsuranceIndemnity = houseFlatInsuranceResource.getAmountOfInsuranceIndemnity();
            double monthlyPayment = houseFlatInsuranceResource.getMonthlyPayment();
            PropertyType propertyType = houseFlatInsuranceResource.getPropertyType();
            Address address = houseFlatInsuranceResource.getAddress();
            double valueOfProperty = houseFlatInsuranceResource.getValueOfProperty();
            boolean idGarageInsurance = houseFlatInsuranceResource.isGarageInsurance();
            this.insuranceSystemService.getContractService().registerHouseFlatInsurance(insurer,start,end,amoutOfInsuranceIndemnity,monthlyPayment,propertyType,address,valueOfProperty,idGarageInsurance);
        }
        catch (PostalCodeParseException e)
        {
            bindingResult.rejectValue("postalCode","postalCode",e.getMessage());
        }
    }
}
