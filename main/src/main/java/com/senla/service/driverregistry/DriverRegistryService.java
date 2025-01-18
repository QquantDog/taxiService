package com.senla.service.driverregistry;

import com.senla.dto.driverregistry.DriverRegistryCreateDto;
import com.senla.dto.pagination.PaginationRequest;
import com.senla.model.driverregistry.DriverRegistry;
import com.senla.util.service.GenericService;

import java.util.List;

public interface DriverRegistryService extends GenericService<DriverRegistry, Long> {
    DriverRegistry registerDriverId(DriverRegistryCreateDto driverRegistryCreateDto);
    List<DriverRegistry> findAllEntries(PaginationRequest paginationRequest);
    List<DriverRegistry> findDriverCompanies();
//    DriverRegistry updateRegistration(DriverRegistryUpdateDto dto);
    void deleteDriverRegistryEntry(Long companyId);
}
