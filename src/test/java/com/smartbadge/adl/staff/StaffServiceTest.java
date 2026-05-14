//package com.smartbadge.adl.staff;
//
//import com.smartbadge.adl.leave.LeaveService;
//import com.smartbadge.adl.shared.exception.DuplicateResourceException;
//import com.smartbadge.adl.shared.exception.ResourceNotFoundException;
//import com.smartbadge.adl.staff.dto.CreateStaffRequest;
//import com.smartbadge.adl.team.TeamService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(MockitoExtension.class)
//class StaffServiceTest {
//
//    @Mock
//    private StaffRepository staffRepository;
//    @Mock
//    private LeaveService leaveService;
//    @Mock
//    private TeamService teamService;
//    @InjectMocks
//    private StaffService staffService;
//
//    private CreateStaffRequest validRequest;
//
//    @BeforeEach
//    void setUp() {
//        validRequest = CreateStaffRequest.builder()
//                .staffId("S4861934")
//                .name("Sivaraj Thavamani")
//                .email("stsivaraj@gmail.com")
//                .title(Title.MR)
//                .jobTitle("Senior Software Engineer")
//                .grade("IT.07")
//                .businessCardType(BusinessCardType.SMARTBADGE)
//                .build();
//    }
//
//    @Test
//    void create_shouldSaveAndReturnDto() {
//        given(staffRepository.existsById("S4861934")).willReturn(false);
//        given(staffRepository.existsByEmail("stsivaraj@gmail.com")).willReturn(false);
//        given(staffRepository.save(any(Staff.class))).willAnswer(i -> i.getArgument(0));
//
//        StaffDto result = staffService.create(validRequest);
//
//        assertThat(result.getStaffId()).isEqualTo("S4861934");
//        assertThat(result.getName()).isEqualTo("Sivaraj Thavamani");
//        verify(staffRepository).save(any(Staff.class));
//    }
//
//    @Test
//    void create_shouldThrowWhenIdAlreadyExists() {
//        given(staffRepository.existsById("S4861934")).willReturn(true);
//
//        assertThatThrownBy(() -> staffService.create(validRequest))
//                .isInstanceOf(DuplicateResourceException.class);
//    }
//
//    @Test
//    void findByStaffId_shouldThrowWhenNotFound() {
//        given(staffRepository.findById("UNKNOWN")).willReturn(Optional.empty());
//
//        assertThatThrownBy(() -> staffService.findByStaffId("UNKNOWN"))
//                .isInstanceOf(ResourceNotFoundException.class);
//    }
//}
