package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.domain.usecase.AddUserUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.UserLoginUseCase;
import com.iishanto.jobhunterbackend.web.dto.request.UserRegistrationDto;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AddUserUseCase addUserUseCase;
    private final UserLoginUseCase userLoginUseCase;
    @PostMapping("/google/registration")
    public ApiResponse registerUser(@RequestBody UserRegistrationDto userRegistrationDto){
        System.out.println("User Registered: "+userRegistrationDto.toString());
        Long userId = addUserUseCase.addUserFromGoogle(userRegistrationDto.toUserModel());
        if(userId!=null){
            String jwtToken = userLoginUseCase.loginByGoogleTokenAndEmail(userRegistrationDto.getEmail(),userRegistrationDto.getToken());
            System.out.println("User Registered: "+ jwtToken);
            return new ApiResponse(true,jwtToken,"Google authenticated Successfully");
        }else {
            return new ApiResponse(false,null,"Failed to register user");
        }
    }
}
