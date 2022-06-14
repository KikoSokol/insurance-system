package sk.stu.fei.uim.oop.insurancesystem.backend.user.service;

import org.springframework.stereotype.Service;
import sk.stu.fei.uim.oop.insurancesystem.backend.contract.domain.Contract;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.domain.User;

import java.util.Map;
import java.util.Set;

@Service
public interface InformationUserService
{
    Map<Long, User> getAllUsers();
    Set<Long> getAllContractsOfUserById(long idUser);
}
