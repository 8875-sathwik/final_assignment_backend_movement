package io.swagger.api;

import io.swagger.entity.UserEntity;
import io.swagger.model.AddMoney;
import io.swagger.model.User;
import io.swagger.model.UserLoginBody;
import io.swagger.repository.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private final userRepo userRepo;


    @Autowired
    private SecurityApi securityApi;


    public UserService(userRepo userRepo) {
        this.userRepo = userRepo;
    }

    public UserEntity getUserbyUserId(String userid){
        return userRepo.findByUserId(userid);
    }
    public UserEntity getUserbyUserName(String username){
        return userRepo.findByUserName(username);
    }


    public void changeloginStatus(String userId , String loginStatus ){
        UserEntity user = userRepo.findByUserId(userId);
        user.setLoginStatus(loginStatus);

        userRepo.save(user);
    }

    public  boolean checkUserisAlreadyLoggedOut(String userId){
        UserEntity u = userRepo.findByUserId(userId);
        if(u.getLoginStatus() == "IN"){
            return false;
        }
        return true;
    }

    public boolean Authenticatelogin(UserLoginBody body){
        UserEntity user = userRepo.findByUserName(body.getUsername());
        return user.getUserName().equals(body.getUsername()) && user.getPassword().equals(body.getPassword());
    }

    public void createUserService(User body){
        UserEntity u = new UserEntity(body.getUserid(),body.getFirstName(),
                body.getLastName(), body.getEmail(),
                body.getPassword(), body.getPhone(),
                body.getDob(),"A",
                body.getUsername(), body.getWorkspaceId());
        u.setLoginStatus("OUT");
        u.setWalBalnce((float) 0);

        userRepo.save(u);

    }


    public void updateUserService(User body) {
        UserEntity existingUser = userRepo.findByUserId(body.getUserid());

        existingUser.setFirstName(body.getFirstName());
        existingUser.setLastName(body.getLastName());
        existingUser.setEmailId(body.getEmail());
        existingUser.setPassword(body.getPassword());
        existingUser.setMsisdn(body.getPhone());
        existingUser.setDob(body.getDob());
        existingUser.setUserName(body.getUsername());

        // Save the updated user entity
        userRepo.save(existingUser);

    }

    public void deleteUserService(String userid) {
        UserEntity u = userRepo.findByUserId(userid);
        u.setStatus("N");

        userRepo.save(u);

    }


    public void addmoney(AddMoney body) {

        UserEntity user = userRepo.findByUserId(body.getUserId());

        Float balance = user.getWalBalnce() + body.getAmmount();
        user.setWalBalnce(balance);
        userRepo.save(user);
    }
}
