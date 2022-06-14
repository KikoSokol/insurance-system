package sk.stu.fei.uim.oop.insurancesystem.backend;

import sk.stu.fei.uim.oop.insurancesystem.backend.contract.service.ContractService;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.service.InformationContractService;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.service.UpdateContractService;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.service.InformationUserService;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.service.UpdateUserService;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.service.UserService;

public interface InsuranceSystemService
{
    UserService getUserService();
    void setUserService(UserService userService);
    InformationUserService getInformationUserService();
    void setInformationUserService(InformationUserService informationUserService);
    UpdateUserService getUpdateUser();
    void setUpdateUser(UpdateUserService updateUser);
    ContractService getContractService();
    void setContractService(ContractService contractService);
    UpdateContractService getUpdateContractService();
    void setUpdateContractService(UpdateContractService updateContractService);
    InformationContractService getInformationContractService();
    void setInformationContractService(InformationContractService informationContractService);
}
