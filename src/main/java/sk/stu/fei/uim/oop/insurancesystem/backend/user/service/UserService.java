package sk.stu.fei.uim.oop.insurancesystem.backend.user.service;

import org.springframework.stereotype.Service;
import sk.stu.fei.uim.oop.insurancesystem.backend.address.Address;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.domain.User;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.domain.attribute.PersonalNumber;

import java.util.Optional;

@Service
public interface UserService
{
    User registerUserWithCorespondenceAddress(String name, String surname, PersonalNumber personalNumber, String email, Address addressOfPermanentResidence, Address addressOfCorespondence);
    User registerUserWithoutCorespondenceAddress(String name, String surname, PersonalNumber personalNumber, String email, Address addressOfPermanentResidence);
    Optional<User> findUserById(long id);
}

