package sk.stu.fei.uim.oop.insurancesystem.backend.contract.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sk.stu.fei.uim.oop.insurancesystem.backend.InsuranceSystemService;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.Contract;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.PurposeOfTrip;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.TravelInsurance;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.web.resource.TravelInsuranceResource;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.domain.User;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/contract/travelInsurance")
public class TravelInsuranceController
{
    private final InsuranceSystemService insuranceSystemService;

    public TravelInsuranceController(InsuranceSystemService insuranceSystemService) {
        this.insuranceSystemService = insuranceSystemService;
    }

    @GetMapping("/id/{id}")
    public String infoTravelInsurance(@PathVariable long id, Model model)
    {
        Contract contract = this.insuranceSystemService.getContractService().findContractById(id).get();
        TravelInsurance travelInsurance = (TravelInsurance) contract;
        model.addAttribute("travellInsurance",travelInsurance);
        User insurer = this.insuranceSystemService.getUserService().findUserById(travelInsurance.getInsurer()).get();
        model.addAttribute("insurerTravellInsurance",insurer);
        User insured = this.insuranceSystemService.getUserService().findUserById(travelInsurance.getInsured()).get();
        model.addAttribute("insuredTravellInsurance",insured);
        return "/contract/travell/travell";
    }

    @GetMapping("/add")
    public String addTravelInsurance(Model model)
    {
        model.addAttribute("newTravelInsurance",new TravelInsuranceResource());
        model.addAttribute("userTravelInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("purposeTravell", PurposeOfTrip.values());
        return "/contract/travell/add";
    }

    @PostMapping("/add")
    public String addTravelInsuranceSubmit(@ModelAttribute("newTravelInsurance") @Valid TravelInsuranceResource travelInsuranceResource, BindingResult bindingResult, Model model)
    {
        model.addAttribute("userTravelInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("purposeTravell",PurposeOfTrip.values());
        checkDate(travelInsuranceResource.getStartOfInsurance(),travelInsuranceResource.getEndOfInsurance(),bindingResult);
        if(bindingResult.hasErrors())
        {
            return "/contract/travell/add";
        }

        addContract(travelInsuranceResource);

        return "redirect:/contract/";
    }

    @GetMapping("/update/id/{id}")
    public String updateTravelInsurance(@PathVariable long id, Model model)
    {
        Contract contract = this.insuranceSystemService.getContractService().findContractById(id).get();
        model.addAttribute("updateTravelInsurance",new TravelInsuranceResource((TravelInsurance)contract));
        model.addAttribute("userTravelInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("purposeTravell", PurposeOfTrip.values());
        return "/contract/travell/update";
    }

    @PostMapping("/update/submit/id/{id}")
    public String updateTravelInsuranceSubmit(@PathVariable long id, @ModelAttribute("updateTravelInsurance") @Valid TravelInsuranceResource travelInsuranceResource, BindingResult bindingResult,Model model)
    {
        travelInsuranceResource.setIdContract(id);
        model.addAttribute("userTravelInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("purposeTravell",PurposeOfTrip.values());
        checkDate(travelInsuranceResource.getStartOfInsurance(),travelInsuranceResource.getEndOfInsurance(),bindingResult);
        if(bindingResult.hasErrors())
        {
            return "/contract/travell/update";
        }

        Contract oldTravelInsurance = this.insuranceSystemService.getContractService().findContractById(id).get();

        Contract updatedTravelInsurance = travelInsuranceResource.toTravelInsurance(oldTravelInsurance.getIdContract(), oldTravelInsurance.getDateTimeOfFormation());
        User oldInsurer = this.insuranceSystemService.getUserService().findUserById(oldTravelInsurance.getInsurer()).get();
        User newInsurer = this.insuranceSystemService.getUserService().findUserById(updatedTravelInsurance.getInsurer()).get();
        this.insuranceSystemService.getUpdateContractService().updateContract(updatedTravelInsurance,oldInsurer,newInsurer);

        return "redirect:/contract/";
    }


    @GetMapping("/add/toUser/{id}")
    public String addTravelInsuranceForInsurer(@PathVariable long id, Model model)
    {
        User user = this.insuranceSystemService.getUserService().findUserById(id).get();
        TravelInsuranceResource travelInsuranceResource = new TravelInsuranceResource();
        travelInsuranceResource.setInsurer(id);
        model.addAttribute("newTravelInsurance",travelInsuranceResource);
        model.addAttribute("insurerToTravelInsurance",user);
        model.addAttribute("userTravelInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("purposeTravell", PurposeOfTrip.values());
        return "/contract/travell/addForInsurer";
    }


    @PostMapping("/add/toUser/{id}")
    public String addTravelInsuranceSubmitForInsurer(@PathVariable long id,@ModelAttribute("newTravelInsurance") @Valid TravelInsuranceResource travelInsuranceResource, BindingResult bindingResult, Model model)
    {
        User user = this.insuranceSystemService.getUserService().findUserById(id).get();
        model.addAttribute("insurerToTravelInsurance",user);
        model.addAttribute("userTravelInsurance",this.insuranceSystemService.getInformationUserService().getAllUsers());
        model.addAttribute("purposeTravell", PurposeOfTrip.values());
        checkDate(travelInsuranceResource.getStartOfInsurance(),travelInsuranceResource.getEndOfInsurance(),bindingResult);
        if(bindingResult.hasErrors())
        {
            return "/contract/travell/addForInsurer";
        }

        addContract(travelInsuranceResource);

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

    private void addContract(TravelInsuranceResource travelInsuranceResource)
    {
        LocalDateTime start = travelInsuranceResource.getStartOfInsurance();
        LocalDateTime end = travelInsuranceResource.getEndOfInsurance();
        User insurer = this.insuranceSystemService.getUserService().findUserById(travelInsuranceResource.getInsurer()).get();
        double amoutOfInsuranceIndemnity = travelInsuranceResource.getAmountOfInsuranceIndemnity();
        double monthlyPayment = travelInsuranceResource.getMonthlyPayment();
        User insured = this.insuranceSystemService.getUserService().findUserById(travelInsuranceResource.getInsured()).get();
        boolean inEurope = travelInsuranceResource.isInEurope();
        PurposeOfTrip purposeOfTrip2 = travelInsuranceResource.getPurposeOfTrip();
        this.insuranceSystemService.getContractService().registerTravelInsurance(insurer,start,end,amoutOfInsuranceIndemnity,monthlyPayment,insured,inEurope,purposeOfTrip2);
    }
}
