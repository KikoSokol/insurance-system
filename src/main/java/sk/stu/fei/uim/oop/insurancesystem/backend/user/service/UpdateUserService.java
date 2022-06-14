package sk.stu.fei.uim.oop.insurancesystem.backend.user.service;

import org.springframework.stereotype.Service;
import sk.stu.fei.uim.oop.insurancesystem.backend.user.domain.User;

@Service
public interface UpdateUserService
{
    boolean updateUser(User user);
}
