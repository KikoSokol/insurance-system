package sk.stu.fei.uim.oop.insurancesystem.backend.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sk.stu.fei.uim.oop.insurancesystem.backend.InsuranceSystemService;
import sk.stu.fei.uim.oop.insurancesystem.backend.address.Address;
import sk.stu.fei.uim.oop.insurancesystem.backend.address.attribute.PostalCodeParseException;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.domain.User;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.domain.attribute.PersonalNumber;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.domain.attribute.PersonalNumberParseException;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.web.resource.UserResource;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    private final InsuranceSystemService insuranceSystemService;

    @Autowired
    public UserController(InsuranceSystemService insuranceSystemService) {
        this.insuranceSystemService = insuranceSystemService;
    }

    @GetMapping("/")
    public String all(Model model) {
        model.addAttribute("users", insuranceSystemService.getInformationUserService().getAllUsers());
        return "user/all";
    }

    @GetMapping("/id/{id}")
    public String byId(@PathVariable long id, Model model) {
        Optional<User> optionalUser = insuranceSystemService.getUserService().findUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute(user);
            model.addAttribute("contracts", insuranceSystemService.getInformationContractService().getAllContractOfInsurer(user.getContracts()));
            return "/user/one";
        }
        return "redirect:/user/";
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        model.addAttribute("newUser", new UserResource());
        return "/user/add";
    }

    @PostMapping("/add")
    public String submitNewUser(@ModelAttribute("newUser") @Valid UserResource userResource, BindingResult bindingResult)
    {
        if(!userResource.isCorespondenceCorrect())
        {
            bindingResult.rejectValue("corespondenceStreet","corespondenceStreet","Correspondence address is wrong. All fields of correspondence address must be fill or all fields of correspondence address can not be fill");
        }
        if(bindingResult.hasErrors())
            return "/user/add";
        try
        {
            String name = userResource.getName();
            String surname = userResource.getSurname();
            PersonalNumber personalNumber = new PersonalNumber(userResource.getPersonalNumber());
            String email = userResource.getEmail();
            Address permanentAddress = userResource.getPermanentAddress();
            Address corespondenceAddress = userResource.getCorespondenceAddress();
            this.insuranceSystemService.getUserService().registerUserWithCorespondenceAddress(name,surname,personalNumber,email,permanentAddress,corespondenceAddress);
        }
        catch (PersonalNumberParseException e)
        {
            bindingResult.rejectValue("personalNumber","personalNumber",e.getMessage());
        }
        catch (PostalCodeParseException e)
        {
            errorAddress(userResource,bindingResult);
        }
        if(bindingResult.hasErrors())
            return "/user/add";

        return "redirect:/user/";

    }

    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable long id,Model model)
    {
        Optional<User> optionalUser= this.insuranceSystemService.getUserService().findUserById(id);

        if(!optionalUser.isPresent())
            return "redirect:/user/";

        User user = optionalUser.get();
        model.addAttribute("updateUser",new UserResource(user));
        return "user/update";

    }

    @PostMapping("/update/submit/{id}")
    public String updateUserSubmit(@PathVariable long id, @ModelAttribute("updateUser") @Valid UserResource userResource, BindingResult bindingResult,Model model)
    {
        if(!userResource.isCorespondenceCorrect())
        {
            bindingResult.rejectValue("corespondenceStreet","corespondenceStreet","Correspondence address is wrong. All fields of correspondence address must be fill or all fields of correspondence address can not be fill");
        }
        userResource.setIdUser(id);
        if(bindingResult.hasErrors())
            return "user/update";

        System.out.println("Update user  " + id);
        User oldUser = this.insuranceSystemService.getUserService().findUserById(id).get();
        try
        {
            User user = userResource.toUser(oldUser.getIdUser(),oldUser.getContracts());
            this.insuranceSystemService.getUpdateUser().updateUser(user);
        }
        catch (PersonalNumberParseException e)
        {
            bindingResult.rejectValue("personalNumber","personalNumber",e.getMessage());
        }
        catch (PostalCodeParseException e)
        {
            errorAddress(userResource,bindingResult);
        }
        if(bindingResult.hasErrors())
            return "user/update";

        return "redirect:/user/";
    }

    private void errorAddress(UserResource userResource, BindingResult bindingResult)
    {
        try
        {
            Address permanentAddress = userResource.getPermanentAddress();
        }
        catch (PostalCodeParseException e)
        {
            bindingResult.rejectValue("permanentPostalCode","permanentPostalCode",e.getMessage());
        }
        try
        {
            Address correspondenceAddress = userResource.getCorespondenceAddress();
        }
        catch (PostalCodeParseException e)
        {
            bindingResult.rejectValue("corespondencePostalCode","corespondencePostalCode",e.getMessage());
        }

    }

}
