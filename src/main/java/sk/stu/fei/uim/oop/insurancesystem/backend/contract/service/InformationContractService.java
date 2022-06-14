package sk.stu.fei.uim.oop.insurancesystem.backend.contract.service;

import org.springframework.stereotype.Service;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.Contract;

import java.util.Map;
import java.util.Set;

@Service
public interface InformationContractService
{
    Map<Long, Contract> getAllContract();
    Set<Contract> getAllContractOfInsurer(Set<Long> contractsOfInsurer);
}
