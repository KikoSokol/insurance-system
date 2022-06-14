package sk.stu.fei.uim.oop.insurancesystem.backend;

import org.springframework.stereotype.Service;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.service.BasicContractService;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.service.ContractService;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.service.InformationContractService;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.service.UpdateContractService;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.service.IdUserService;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.service.InformationUserService;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.service.UpdateUserService;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.service.UserService;

@Service
public class InsuranceSystem implements InsuranceSystemService
{
    private UserService userService;
    private InformationUserService informationUserService;
    private UpdateUserService updateUser;


    private ContractService contractService;
    private UpdateContractService updateContractService;
    private InformationContractService informationContractService;

    public InsuranceSystem()
    {
        IdUserService idUserService = new IdUserService();
        BasicContractService basicContractService = new BasicContractService();

        this.userService = idUserService;
        this.informationUserService = idUserService;
        this.updateUser = idUserService;

        this.contractService = basicContractService;
        this.informationContractService = basicContractService;
        this.updateContractService = basicContractService;
    }

    public InsuranceSystem(UserService userService, InformationUserService informationUserService, UpdateUserService updateUser, ContractService contractService, UpdateContractService updateContractService, InformationContractService informationContractService)
    {
        this.userService = userService;
        this.informationUserService = informationUserService;
        this.updateUser = updateUser;

        this.contractService = contractService;
        this.updateContractService = updateContractService;
        this.informationContractService = informationContractService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public InformationUserService getInformationUserService() {
        return informationUserService;
    }

    public void setInformationUserService(InformationUserService informationUserService) {
        this.informationUserService = informationUserService;
    }

    public UpdateUserService getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(UpdateUserService updateUser) {
        this.updateUser = updateUser;
    }

    public ContractService getContractService() {
        return contractService;
    }

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    public UpdateContractService getUpdateContractService() {
        return updateContractService;
    }

    public void setUpdateContractService(UpdateContractService updateContractService) {
        this.updateContractService = updateContractService;
    }

    public InformationContractService getInformationContractService() {
        return informationContractService;
    }

    public void setInformationContractService(InformationContractService informationContractService) {
        this.informationContractService = informationContractService;
    }
}
