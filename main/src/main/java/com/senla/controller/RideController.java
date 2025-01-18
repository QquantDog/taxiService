package com.senla.controller;


import com.senla.dto.pagination.PaginationRequest;
import com.senla.dto.ride.*;
import com.senla.model.ride.Ride;
import com.senla.service.ride.RideService;
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
@RequestMapping("/rides")
public class RideController {

    private final RideService rideService;
    private final ModelMapper modelMapper;

    @Autowired
    public RideController(RideService rideService, ModelMapper modelMapper) {
        this.rideService = rideService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('RIDE_READ_ALL')")
    public ResponseEntity<List<RideFullResponseDto>> getAllRides(@Valid RideFilterDto filterDto, @Valid PaginationRequest paginationRequest) {
        List<Ride> rides = rideService.getAllRidesFullWithPaginationAndFiltering(filterDto, paginationRequest);
        return new ResponseEntity<>(rides.stream().map(this::convertToFullResponseDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/customer/my")
    @PreAuthorize("hasAuthority('RIDE_READ_CUSTOMER')")
    public ResponseEntity<List<RideCustomerResponseDto>> getCustomerRides(@Valid RideFilterDto filterDto, @Valid PaginationRequest paginationRequest) {
        List<Ride> rides = rideService.getCustomerRidesFullWithPaginationAndFiltering(filterDto, paginationRequest);
        return new ResponseEntity<>(rides.stream().map(this::convertToCustomerResponseDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/driver/my")
    @PreAuthorize("hasAuthority('RIDE_READ_DRIVER')")
    public ResponseEntity<List<RideDriverResponseDto>> getDriverRides(@Valid RideFilterDto filterDto, @Valid PaginationRequest paginationRequest) {
        List<Ride> rides = rideService.getDriverRidesFullWithPaginationAndFiltering(filterDto, paginationRequest);
        return new ResponseEntity<>(rides.stream().map(this::convertToDriverResponseDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('RIDE_READ_ALL')")
    public ResponseEntity<RideResponseDto> getRideById(@PathVariable("id") Long id) {
        Ride ride = rideService.findRideById(id);
        return new ResponseEntity<>(convertToResponseDto(ride), HttpStatus.OK);
    }

    @PostMapping("/init")
    @PreAuthorize("hasAuthority('RIDE_INIT')")
    public ResponseEntity<RideFullResponseDto> initializeRide(@Valid @RequestBody RideCreateDto rideCreateDto) {
        Ride ride = rideService.initializeRide(rideCreateDto);
        return new ResponseEntity<>(convertToFullResponseDto(ride), HttpStatus.CREATED);
    }

    @PostMapping("/match-confirm")
    @PreAuthorize("hasAuthority('RIDE_MATCH_PROCESS')")
    public ResponseEntity<RideResponseDto> matchRide(@Valid @RequestBody RideMatchDto rideMatchDto){
        Ride ride = rideService.matchConfirmRide(rideMatchDto);
        return new ResponseEntity<>(convertToResponseDto(ride), HttpStatus.CREATED);
    }

    @PostMapping("/match-decline")
    @PreAuthorize("hasAuthority('RIDE_MATCH_PROCESS')")
    public ResponseEntity<?> declineRide(@Valid @RequestBody RideMatchDto rideMatchDto){
        rideService.matchDeclineRide(rideMatchDto);
        return new ResponseEntity<>(ResponseEntity.ok().build(), HttpStatus.OK);
    }

    @PostMapping("/wait")
    @PreAuthorize("hasAuthority('RIDE_STATE_PROCESS')")
    public ResponseEntity<RideResponseDto> waitForClientRide(){
        Ride ride = rideService.waitForClient();
        return new ResponseEntity<>(convertToResponseDto(ride), HttpStatus.CREATED);
    }
    @PostMapping("/start")
    @PreAuthorize("hasAuthority('RIDE_STATE_PROCESS')")
    public ResponseEntity<RideResponseDto> startRide() {
        Ride ride = rideService.startRide();
        return new ResponseEntity<>(convertToResponseDto(ride), HttpStatus.CREATED);
    }
    @PostMapping("/end")
    @PreAuthorize("hasAuthority('RIDE_STATE_PROCESS')")
    public ResponseEntity<RideWithPaymentResponseDto> endRide(@Valid @RequestBody RideEndDto rideEndDto) {
        Ride ride = rideService.endRide(rideEndDto);
        return new ResponseEntity<>(convertToResponseWithPaymentDto(ride), HttpStatus.CREATED);
    }
    //    это для пассажира
    @PostMapping("/cancel")
    @PreAuthorize("hasAuthority('RIDE_OWN_CANCEL')")
    public ResponseEntity<RideResponseDto> cancelRide(@Valid @RequestBody RideCancelDto rideCancelDto){
        Ride ride = rideService.cancelRide(rideCancelDto);
        return new ResponseEntity<>(convertToResponseDto(ride), HttpStatus.CREATED);
    }

    @PostMapping("/promocodes/activate")
    @PreAuthorize("hasAuthority('RIDE_ACTIVATE_PROMO')")
    public ResponseEntity<RideWithPromocodeResponseDto> activatePromocode(@Valid @RequestBody RidePromocodeEnterDto ridePromocodeEnterDto){
        Ride ride = rideService.activatePromocodeOnRide(ridePromocodeEnterDto);
        return new ResponseEntity<>(convertToResponseWithPromocodeDto(ride), HttpStatus.CREATED);
    }

    @PostMapping("/tip")
    @PreAuthorize("hasAuthority('RIDE_TIP')")
    public ResponseEntity<RideResponseDto> tipDriver(@Valid @RequestBody RideTipDto rideTipDto) {
        Ride ride = rideService.rideTip(rideTipDto);
        return new ResponseEntity<>(convertToResponseDto(ride), HttpStatus.CREATED);
    }


    private RideFullResponseDto convertToFullResponseDto(Ride ride){
        return modelMapper.map(ride, RideFullResponseDto.class);
    }
    private RideCustomerResponseDto convertToCustomerResponseDto(Ride ride){
        return modelMapper.map(ride, RideCustomerResponseDto.class);
    }
    private RideDriverResponseDto convertToDriverResponseDto(Ride ride){
        return modelMapper.map(ride, RideDriverResponseDto.class);
    }

    private RideWithPromocodeResponseDto convertToResponseWithPromocodeDto(Ride ride){
        return modelMapper.map(ride, RideWithPromocodeResponseDto.class);
    }
    private RideWithPaymentResponseDto convertToResponseWithPaymentDto(Ride ride){
        return modelMapper.map(ride, RideWithPaymentResponseDto.class);
    }
    private RideResponseDto convertToResponseDto(Ride ride){
        return modelMapper.map(ride, RideResponseDto.class);
    }

}