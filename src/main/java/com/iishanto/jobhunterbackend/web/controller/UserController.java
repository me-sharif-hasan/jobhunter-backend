package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.domain.usecase.AddUserUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.GetUserUseCase;
import com.iishanto.jobhunterbackend.web.dto.request.UserRegistrationDto;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import com.iishanto.jobhunterbackend.web.dto.response.user.SafeUserResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private GetUserUseCase getUserUseCase;
    private AddUserUseCase addUserUseCase;
    @PostMapping
    public void createUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto){
        System.out.println("User Created"+userRegistrationDto.toString());
        addUserUseCase.addUser(userRegistrationDto.toUserModel());
    }

    @GetMapping
    public ApiResponse getUsers(){
        SafeUserResponseDto safeUserResponseDto=SafeUserResponseDto.from(getUserUseCase.getCurrentUser());
        if(safeUserResponseDto.getId()!=null){
            return new ApiResponse(true,safeUserResponseDto,"Users fetched successfully");
        }else {
            return new ApiResponse(false,null,"User not authenticated");
        }
    }
}
