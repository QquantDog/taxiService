package com.senla.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.senla.dto.pagination.PaginationRequest;
import com.senla.dto.user.UserFilterDto;
import com.senla.dto.user.UserResponseWithRoleDto;
import com.senla.exception.DaoException;
import com.senla.model.user.User;
import com.senla.dto.user.UserResponseDto;
import com.senla.dto.user.UserUpdateDto;
import com.senla.service.user.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    @PreAuthorize("hasAuthority({'USER_READ_ALL'})")
    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(@Valid UserFilterDto filterDto, @Valid PaginationRequest paginationRequest){
        List<User> users = userService.findAllWithSearchAndPagination(filterDto, paginationRequest);
        return new ResponseEntity<>(users.stream().map(this::convertToResponseDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority({'USER_READ_ALL'})")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long id) {
        User user = this.userService.findById(id).orElseThrow(()->new DaoException("User not found"));
        return new ResponseEntity<>(convertToResponseDto(user), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseWithRoleDto> getCurrentUser()  {
        User user = userService.findMe();
        return new ResponseEntity<>(convertToResponseWithRoleDto(user), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto)  {
        return new ResponseEntity<>(convertToResponseDto(userService.updateUser(userUpdateDto)), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority({'USER_DEL'})")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private UserResponseDto convertToResponseDto(User user){
        return modelMapper.map(user, UserResponseDto.class);
    }
    private UserResponseWithRoleDto convertToResponseWithRoleDto(User user){
        return modelMapper.map(user, UserResponseWithRoleDto.class);
    }

}
