package com.senla.service.cab;

import com.senla.dto.cab.CabCreateDto;
import com.senla.dto.cab.CabFilterDto;
import com.senla.dto.cab.CabUpdateDto;
import com.senla.dto.pagination.PaginationRequest;
import com.senla.model.cab.Cab;
import com.senla.util.service.GenericService;

import java.util.List;

public interface CabService extends GenericService<Cab, Long> {
    Cab createCab(CabCreateDto dto);
    Cab updateCab(Long id, CabUpdateDto dto);
    List<Cab> getAll();

    List<Cab> getRegisteredCabs();
    List<Cab> getPossibleCabs();

    List<Cab> getAllWithFilterAndPagination(CabFilterDto cabFilterDto, PaginationRequest paginationRequest);
}
