package com.senla.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.senla.dto.matchrequest.MatchRequestResponseDto;
import com.senla.dto.pagination.PaginationRequest;
import com.senla.model.matchrequest.MatchRequest;
import com.senla.service.matchrequest.MatchRequestService;
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
@RequestMapping("/match-requests")
public class MatchRequestController {

    private final MatchRequestService matchRequestService;
    private final ModelMapper modelMapper;

    @Autowired
    public MatchRequestController(MatchRequestService matchRequestService, ModelMapper modelMapper) {
        this.matchRequestService = matchRequestService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority({'MR_READ_ALL'})")
    public ResponseEntity<List<MatchRequestResponseDto>> getAllMatchRequests(@Valid PaginationRequest paginationRequest) throws JsonProcessingException {
        List<MatchRequest> requests = matchRequestService.findAllMatches(paginationRequest);
        return new ResponseEntity<>(requests.stream().map(this::convertToResponseDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping("/{requestId}")
    @PreAuthorize("hasAuthority({'MR_PROCESS'})")
    public ResponseEntity<MatchRequestResponseDto> forceRequestDeactivation(@PathVariable("requestId") Long requestId){
        matchRequestService.deactivateRequest(requestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private MatchRequestResponseDto convertToResponseDto(MatchRequest matchRequest){
        return modelMapper.map(matchRequest, MatchRequestResponseDto.class);
    }
}
