package sk.stu.fei.uim.oop.insurancesystem.backend.contract.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sk.stu.fei.uim.oop.insurancesystem.backend.InsuranceSystemService;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.*;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.web.resource.AccidentInsuranceResource;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.domain.User;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/contract/accidentInsurance")
public class AccidentInsuranceController
{
    private final InsuranceSystemService insuranceSystemService;

    public AccidentInsuranceController(InsuranceSystemService insuranceSystemService) {
        this.insuranceSystemService = insuranceSystemService;
    }

    @GetMapping("/id/{id}")
    public String infoAccidentInsurance(@PathVariable long id, Model model)
    {
        Contract contract = this.insuranceSystemService.getContractService().findContractById(id).get();
        AccidentInsurance accidentInsurance = (AccidentInsurance) contract;
        model.addAttribute("accidentInsurance",accidentInsurance);
        User insurer = this.insuranceSystemService.getUserService().findUserById(accidentInsurance.getInsurer()).get();
        model.addAttribute("insurerAccidentInsurance",insurer);
        User insured = this.insuranceSystemService.getUserService().findUserById(accidentInsurance.getInsured()).get();
        model.addAttribute("insuredAccidentInsurance",insured);
        return "/contract/accident/accident";
    }

    @GetMapping("/add")
    public String addAccidentInsurance(Model model)
    {
        model.addAttribute("newAccidentInsurance",new AccidentInsuranceResource());
        model.addAttribute("userAccidentInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("territorialValidityInsurance", TerritorialValidity.values());
        return "/contract/accident/add";
    }

    @PostMapping("/add")
    public String addAccidentInsuranceSubmit(@ModelAttribute("newAccidentInsurance") @Valid AccidentInsuranceResource accidentInsuranceResource, BindingResult bindingResult, Model model)
    {
        model.addAttribute("userAccidentInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("territorialValidityInsurance", TerritorialValidity.values());
        checkDate(accidentInsuranceResource.getStartOfInsurance(), accidentInsuranceResource.getEndOfInsurance(),bindingResult);

        if(bindingResult.hasErrors())
        {
            return "/contract/accident/add";
        }

        addContract(accidentInsuranceResource);

        return "redirect:/contract/";

    }

    @GetMapping("/update/id/{id}")
    public String updateAccidentInsurance(@PathVariable long id, Model model)
    {
        Contract contract = this.insuranceSystemService.getContractService().findContractById(id).get();
        model.addAttribute("updateAccidentInsurance",new AccidentInsuranceResource((AccidentInsurance) contract));
        model.addAttribute("userAccidentInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("territorialValidityInsurance", TerritorialValidity.values());
        return "/contract/accident/update";
    }

    @PostMapping("/update/submit/id/{id}")
    public String updateAccidentInsuranceSubmit(@PathVariable long id, @ModelAttribute("updateAccidentInsurance") @Valid AccidentInsuranceResource accidentInsuranceResource, BindingResult bindingResult,Model model)
    {
        accidentInsuranceResource.setIdContract(id);
        model.addAttribute("userAccidentInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("territorialValidityInsurance", TerritorialValidity.values());
        checkDate(accidentInsuranceResource.getStartOfInsurance(),accidentInsuranceResource.getEndOfInsurance(),bindingResult);
        if(bindingResult.hasErrors())
        {
            return "/contract/accident/update";
        }

        Contract oldAccidentInsurance = this.insuranceSystemService.getContractService().findContractById(id).get();

        Contract updatedAccidentInsurance = accidentInsuranceResource.toAccidentInsurance(oldAccidentInsurance.getIdContract(), oldAccidentInsurance.getDateTimeOfFormation());
        User oldInsurer = this.insuranceSystemService.getUserService().findUserById(oldAccidentInsurance.getInsurer()).get();
        User newInsurer = this.insuranceSystemService.getUserService().findUserById(updatedAccidentInsurance.getInsurer()).get();
        this.insuranceSystemService.getUpdateContractService().updateContract(updatedAccidentInsurance,oldInsurer,newInsurer);

        return "redirect:/contract/";
    }


    @GetMapping("/add/toUser/{id}")
    public String addAccidentInsuranceForInsurer(@PathVariable long id, Model model)
    {
        User user = this.insuranceSystemService.getUserService().findUserById(id).get();
        AccidentInsuranceResource accidentInsuranceResource = new AccidentInsuranceResource();
        accidentInsuranceResource.setInsurer(user.getIdUser());
        model.addAttribute("newAccidentInsurance",accidentInsuranceResource);
        model.addAttribute("insurerToAccidentInsurance",user);
        model.addAttribute("userAccidentInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("territorialValidityInsurance", TerritorialValidity.values());
        return "/contract/accident/addForInsurer";
    }


    @PostMapping("/add/toUser/{id}")
    public String addAccidentInsuranceForInsurerSubmit(@PathVariable long id, @ModelAttribute("newAccidentInsurance") @Valid AccidentInsuranceResource accidentInsuranceResource, BindingResult bindingResult, Model model)
    {
        User user = this.insuranceSystemService.getUserService().findUserById(id).get();
        model.addAttribute("insurerToAccidentInsurance",user);
        model.addAttribute("userAccidentInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("territorialValidityInsurance", TerritorialValidity.values());
        checkDate(accidentInsuranceResource.getStartOfInsurance(), accidentInsuranceResource.getEndOfInsurance(),bindingResult);

        if(bindingResult.hasErrors())
        {
            return "/contract/accident/addForInsurer";
        }

        addContract(accidentInsuranceResource);

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

    private void addContract(AccidentInsuranceResource accidentInsuranceResource)
    {
        LocalDateTime start = accidentInsuranceResource.getStartOfInsurance();
        LocalDateTime end = accidentInsuranceResource.getEndOfInsurance();
        User insurer = this.insuranceSystemService.getUserService().findUserById(accidentInsuranceResource.getInsurer()).get();
        double amoutOfInsuranceIndemnity = accidentInsuranceResource.getAmountOfInsuranceIndemnity();
        double monthlyPayment = accidentInsuranceResource.getMonthlyPayment();
        User insured = this.insuranceSystemService.getUserService().findUserById(accidentInsuranceResource.getInsured()).get();
        double sumInsuredOfLastingConsequences = accidentInsuranceResource.getSumInsuredOfLastingConsequences();
        double sumInsuredOfDeath = accidentInsuranceResource.getSumInsuredOfDeath();
        double sumInsuredOfDailyCompensation = accidentInsuranceResource.getSumInsuredOfDailyCompensation();
        TerritorialValidity territorialValidity = accidentInsuranceResource.getTerritorialValidity();
        this.insuranceSystemService.getContractService().registerAccidentInsurance(insurer,start,end,amoutOfInsuranceIndemnity,monthlyPayment,insured,sumInsuredOfLastingConsequences,sumInsuredOfDeath,sumInsuredOfDailyCompensation,territorialValidity);
    }
}
