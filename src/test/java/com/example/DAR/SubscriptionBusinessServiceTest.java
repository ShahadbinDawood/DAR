package com.example.DAR;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.HomeDTOIn;
import com.example.DAR.DTO.Out.UserSubscriptionDtoOut;
import com.example.DAR.Enums.HomePropertyType;
import com.example.DAR.Enums.PaymentStatus;
import com.example.DAR.Enums.UserSubscriptionStatus;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.SubscriptionPlan;
import com.example.DAR.Model.User;
import com.example.DAR.Model.UserSubscription;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.SubscriptionPlanRepository;
import com.example.DAR.Repository.UserRepository;
import com.example.DAR.Repository.UserSubscriptionRepository;
import com.example.DAR.Service.HomeService;
import com.example.DAR.Service.NotificationService;
import com.example.DAR.Service.UserSubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionBusinessServiceTest {

    @Mock
    private HomeRepository homeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;

    @Mock
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Mock
    private NotificationService notificationService;

    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    public void addHome_freeUserCannotAddMoreThanOneHome() {
        HomeService homeService = new HomeService(homeRepository, userRepository, userSubscriptionRepository, modelMapper, notificationService);
        User user = createUser();

        when(userRepository.findUserById(1)).thenReturn(user);
        when(userSubscriptionRepository.findUserSubscriptionByUserIdAndStatus(1, UserSubscriptionStatus.ACTIVE)).thenReturn(null);
        when(homeRepository.findHomesByUserId(1)).thenReturn(List.of(new Home()));

        ApiException exception = assertThrows(ApiException.class, () -> homeService.addHome(1, createHomeDto()));

        assertEquals("You have reached the maximum number of homes for your plan", exception.getMessage());
        verify(homeRepository, never()).save(any(Home.class));
    }

    @Test
    public void createUserSubscription_createsPendingUnpaidSubscription() {
        UserSubscriptionService service = new UserSubscriptionService(
                userSubscriptionRepository,
                userRepository,
                subscriptionPlanRepository,
                modelMapper,
                notificationService
        );
        User user = createUser();
        SubscriptionPlan plan = createPlan();

        when(userRepository.findUserById(1)).thenReturn(user);
        when(userSubscriptionRepository.findByUserAndStatuses(eq(1), anyList())).thenReturn(List.of());
        when(subscriptionPlanRepository.findSubscriptionPlanById(2)).thenReturn(plan);
        when(userSubscriptionRepository.save(any(UserSubscription.class))).thenAnswer(invocation -> {
            UserSubscription subscription = invocation.getArgument(0);
            subscription.setId(10);
            return subscription;
        });

        UserSubscriptionDtoOut result = service.createUserSubscription(1, 2);

        ArgumentCaptor<UserSubscription> captor = ArgumentCaptor.forClass(UserSubscription.class);
        verify(userSubscriptionRepository).save(captor.capture());
        assertEquals(UserSubscriptionStatus.PENDING, captor.getValue().getStatus());
        assertEquals(PaymentStatus.UNPAID, captor.getValue().getPaymentStatus());
        assertNull(captor.getValue().getStartDate());
        assertNull(captor.getValue().getEndDate());
        assertEquals("Standard", result.getPlanName());
        verify(notificationService).sendSubscriptionPendingPaymentNotification(user, "Standard");
    }



    private User createUser() {
        User user = new User();
        user.setId(1);
        user.setName("Samya");
        user.setEmail("samya@dar.com");
        user.setUsername("samya");
        user.setPassword("password");
        user.setPhoneNumber("0500000000");
        user.setCreateAt(LocalDate.now());
        user.setAiCounter(0);
        return user;
    }

    private SubscriptionPlan createPlan() {
        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setId(2);
        plan.setName("Standard");
        plan.setSubtitle("Standard plan");
        plan.setPrice(50.0);
        plan.setIsPopular(false);
        plan.setMaxHomes(3);
        plan.setMaxItems(50);
        plan.setMaxSensors(20);
        plan.setMaxNotificationsPerMonth(50);
        plan.setMaxAiReportsPerMonth(20);
        plan.setWeatherReminderEnabled(true);
        plan.setDailyAIReminder(false);
        return plan;
    }

    private HomeDTOIn createHomeDto() {
        HomeDTOIn dto = new HomeDTOIn();
        dto.setName("Home");
        dto.setAddress("Riyadh");
        dto.setCity("Riyadh");
        dto.setLatitude(24.7136);
        dto.setLongitude(46.6753);
        dto.setBuildYear(2020);
        dto.setPropertyType(HomePropertyType.APARTMENT);
        return dto;
    }
}
