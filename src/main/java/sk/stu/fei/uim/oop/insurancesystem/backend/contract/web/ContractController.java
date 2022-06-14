package sk.stu.fei.uim.oop.insurancesystem.backend.contract.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.stu.fei.uim.oop.insurancesystem.backend.InsuranceSystemService;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.*;
import java.util.Optional;

@Controller
@RequestMapping("/contract")
public class ContractController
{
    private final InsuranceSystemService insuranceSystemService;

    public ContractController(InsuranceSystemService insuranceSystemService) {
        this.insuranceSystemService = insuranceSystemService;
    }

    @GetMapping("/")
    public String all(Model model)
    {
        model.addAttribute("contracts",insuranceSystemService.getInformationContractService().getAllContract());
        return "/contract/all";
    }

    @GetMapping("/id/{id}")
    public String byId(@PathVariable long id)
    {
        Optional<Contract> optionalContract = insuranceSystemService.getContractService().findContractById(id);
        if(optionalContract.isPresent())
        {
            Contract contract = optionalContract.get();
            if(contract instanceof TravelInsurance)
            {
                return "redirect:/contract/travelInsurance/id/{id}/";
            }
            if(contract instanceof AccidentInsurance)
            {
                return "redirect:/contract/accidentInsurance/id/{id}/";
            }
            if(contract instanceof HouseFlatInsurance)
            {
                return "redirect:/contract/houseFlatInsurance/id/{id}";
            }
            if(contract instanceof HouseholdInsurance)
            {
               return "redirect:/contract/householdInsurance/id/{id}/";
            }
        }
        return "redirect:/contract/";
    }


    @GetMapping("/update/id/{id}")
    public String updateContract(@PathVariable long id)
    {
        Optional<Contract> optionalContract = this.insuranceSystemService.getContractService().findContractById(id);

        if(optionalContract.isPresent())
        {
            Contract contract = optionalContract.get();
            if(contract instanceof TravelInsurance)
            {
                return "redirect:/contract/travelInsurance/update/id/{id}";
            }
            else if(contract instanceof AccidentInsurance)
            {
                return "redirect:/contract/accidentInsurance/update/id/{id}";
            }
            else if(contract instanceof HouseFlatInsurance)
            {
                return "redirect:/contract/houseFlatInsurance/update/id/{id}";
            }
            else if(contract instanceof HouseholdInsurance)
            {
                return "redirect:/contract/householdInsurance/update/id/{id}";
            }
        }

        return "redirect:/contract/";

    }

}
