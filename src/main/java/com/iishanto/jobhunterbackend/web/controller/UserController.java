package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.domain.usecase.AddUserUseCase;
import com.iishanto.jobhunterbackend.web.dto.request.UserRegistrationDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private AddUserUseCase addUserUseCase;
    @PostMapping
    public void createUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto){
        System.out.println("User Created"+userRegistrationDto.toString());
        addUserUseCase.addUser(userRegistrationDto.toUserModel());
    }
}
