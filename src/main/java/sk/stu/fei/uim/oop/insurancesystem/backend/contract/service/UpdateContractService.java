package sk.stu.fei.uim.oop.insurancesystem.backend.contract.service;

import org.springframework.stereotype.Service;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.Contract;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.domain.User;

@Service
public interface UpdateContractService
{
    boolean updateContract(Contract contract, User oldInsurer, User newInsurer);
}
