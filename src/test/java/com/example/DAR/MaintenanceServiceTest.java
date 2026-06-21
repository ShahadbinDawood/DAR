package com.example.DAR;

import com.example.DAR.DTO.In.MaintenanceDTOIn;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.HomeItem;
import com.example.DAR.Model.Maintenance;
import com.example.DAR.Repository.HomeItemRepository;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.MaintenanceRepository;
import com.example.DAR.Service.MaintenanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MaintenanceServiceTest {

    @InjectMocks
    MaintenanceService maintenanceService;

    @Mock
    MaintenanceRepository maintenanceRepository;

    @Mock
    HomeRepository homeRepository;

    @Mock
    HomeItemRepository homeItemRepository;

    @Mock
    ModelMapper modelMapper;

    Home home;
    HomeItem homeItem;
    Maintenance maintenance;
    MaintenanceDTOIn maintenanceDTOIn;

    @BeforeEach
    void setUp() {

        home = new Home();
        home.setId(1);
        home.setName("My Home");
        home.setCity("Riyadh");

        homeItem = new HomeItem();
        homeItem.setId(1);
        homeItem.setName("AC");
        homeItem.setHome(home);

        maintenance = new Maintenance();
        maintenance.setId(1);
        maintenance.setTitle("AC Maintenance");
        maintenance.setDescription("Clean AC filter");
        maintenance.setMaintenanceDate(LocalDate.now().plusDays(5));
        maintenance.setCost(100.0);
        maintenance.setStatus("SCHEDULED");
        maintenance.setPriority("HIGH");
        maintenance.setNotes("Check filter");
        maintenance.setHome(home);
        maintenance.setHomeItem(homeItem);

        maintenanceDTOIn = new MaintenanceDTOIn();
        maintenanceDTOIn.setTitle("AC Maintenance");
        maintenanceDTOIn.setDescription("Clean AC filter");
        maintenanceDTOIn.setMaintenanceDate(LocalDate.now().plusDays(5));
        maintenanceDTOIn.setCost(100.0);
        maintenanceDTOIn.setStatus("SCHEDULED");
        maintenanceDTOIn.setPriority("HIGH");
        maintenanceDTOIn.setNotes("Check filter");
    }

    // Test #5: Add maintenance successfully
    @Test
    public void addMaintenanceTest() {

        when(homeRepository.findHomeById(home.getId()))
                .thenReturn(home);

        when(homeItemRepository.findHomeItemById(homeItem.getId()))
                .thenReturn(homeItem);

        maintenanceService.addMaintenance(
                home.getId(),
                homeItem.getId(),
                maintenanceDTOIn
        );

        verify(homeRepository, times(1))
                .findHomeById(home.getId());

        verify(homeItemRepository, times(1))
                .findHomeItemById(homeItem.getId());

        verify(maintenanceRepository, times(1))
                .save(any(Maintenance.class));
    }

    // Test #6: Mark maintenance as done
    @Test
    public void markMaintenanceAsDoneTest() {

        when(maintenanceRepository.findMaintenanceById(maintenance.getId()))
                .thenReturn(maintenance);

        maintenanceService.markMaintenanceAsDone(maintenance.getId());

        verify(maintenanceRepository, times(1))
                .findMaintenanceById(maintenance.getId());

        verify(maintenanceRepository, times(1))
                .save(maintenance);
    }
}