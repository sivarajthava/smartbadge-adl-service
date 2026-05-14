package com.smartbadge.adl.staff;

import com.smartbadge.adl.leave.LeaveDocumentDto;
import com.smartbadge.adl.leave.LeaveService;
import com.smartbadge.adl.shared.exception.DuplicateResourceException;
import com.smartbadge.adl.shared.exception.ResourceNotFoundException;
import com.smartbadge.adl.staff.dto.CreateStaffRequest;
import com.smartbadge.adl.staff.dto.UpdateStaffRequest;
import com.smartbadge.adl.team.TeamDocumentDto;
import com.smartbadge.adl.team.TeamService;


import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffService {

	private final StaffRepository staffRepository;
	private final LeaveService leaveService;
	private final TeamService teamService;

	// ── READ ─────────────────────────────────────────────────────────────────

	public Page<StaffDto> findAll(Pageable pageable) {
		return staffRepository.findAll(pageable).map(this::toDto);
	}

	public StaffDto findByStaffId(String staffId) {
		return toDto(getOrThrow(staffId));
	}

	public StaffProfileDto getFullProfile(String staffId) {
		Staff staff = getOrThrow(staffId);
		LeaveDocumentDto leaveDoc = leaveService.findByStaffId(staffId).orElse(null);
		TeamDocumentDto teamDoc = teamService.findByStaffId(staffId).orElse(null);
		return toProfileDto(staff, leaveDoc, teamDoc);
	}

	// ── CREATE ────────────────────────────────────────────────────────────────

	public StaffDto create(CreateStaffRequest req) {
		if (staffRepository.existsById(req.getStaffId())) {
			throw new DuplicateResourceException("Staff", req.getStaffId());
		}
		if (staffRepository.existsByEmail(req.getEmail())) {
			throw new DuplicateResourceException("Staff (email)", req.getEmail());
		}
		Staff staff = Staff.builder().staffId(req.getStaffId()).name(req.getName()).email(req.getEmail())
				.title(req.getTitle()).jobTitle(req.getJobTitle()).grade(req.getGrade())
				.businessCardType(req.getBusinessCardType())
				.addresses(req.getAddresses() != null
						? req.getAddresses().stream().map(this::toAddress).collect(Collectors.toList())
						: new ArrayList<>())
				.build();
		StaffDto dto = toDto(staffRepository.save(staff));
		log.info("Created staff: {}", staff.getStaffId());
		return dto;
	}

	// ── UPDATE ────────────────────────────────────────────────────────────────

	public StaffDto update(String staffId, UpdateStaffRequest req) {
		Staff staff = getOrThrow(staffId);
		if (req.getName() != null)
			staff.setName(req.getName());
		if (req.getEmail() != null) {
			if (!req.getEmail().equals(staff.getEmail()) && staffRepository.existsByEmail(req.getEmail())) {
				throw new DuplicateResourceException("Staff (email)", req.getEmail());
			}
			staff.setEmail(req.getEmail());
		}
		if (req.getTitle() != null)
			staff.setTitle(req.getTitle());
		if (req.getJobTitle() != null)
			staff.setJobTitle(req.getJobTitle());
		if (req.getGrade() != null)
			staff.setGrade(req.getGrade());
		if (req.getBusinessCardType() != null)
			staff.setBusinessCardType(req.getBusinessCardType());
		StaffDto dto = toDto(staffRepository.save(staff));
		log.info("Updated staff: {}", staffId);
		return dto;
	}

	// ── DELETE ────────────────────────────────────────────────────────────────

	public void delete(String staffId) {
		Staff staff = getOrThrow(staffId);
		staffRepository.delete(staff);
		log.info("Deleted staff: {}", staffId);
	}

	// ── ADDRESS MANAGEMENT ───────────────────────────────────────────────────

	public StaffDto addAddress(String staffId, AddressDto dto) {
		Staff staff = getOrThrow(staffId);
		Address address = toAddress(dto);
		staff.getAddresses().removeIf(a -> a.getAddressType() == address.getAddressType());
		staff.getAddresses().add(address);
		StaffDto staffDto = toDto(staffRepository.save(staff));
		log.info("Added or replaced {} address for staff: {}", address.getAddressType(), staffId);
		return staffDto;
	}

	public StaffDto updateAddress(String staffId, AddressType addressType, AddressDto dto) {
		Staff staff = getOrThrow(staffId);
		staff.getAddresses().removeIf(a -> a.getAddressType() == addressType);
		Address updated = toAddress(dto);
		updated.setAddressType(addressType);
		staff.getAddresses().add(updated);
		StaffDto staffDto = toDto(staffRepository.save(staff));
		log.info("Updated {} address for staff: {}", addressType, staffId);
		return staffDto;
	}

	public StaffDto removeAddress(String staffId, AddressType addressType) {
		Staff staff = getOrThrow(staffId);
		staff.getAddresses().removeIf(a -> a.getAddressType() == addressType);
		StaffDto staffDto = toDto(staffRepository.save(staff));
		log.info("Removed {} address for staff: {}", addressType, staffId);
		return staffDto;
	}

	// ── MAPPING ───────────────────────────────────────────────────────────────

	private Staff getOrThrow(String staffId) {
		return staffRepository.findById(staffId).orElseThrow(() -> new ResourceNotFoundException("Staff", staffId));
	}

	StaffDto toDto(Staff s) {
		return StaffDto.builder().staffId(s.getStaffId()).name(s.getName()).email(s.getEmail()).title(s.getTitle())
				.jobTitle(s.getJobTitle()).grade(s.getGrade()).businessCardType(s.getBusinessCardType())
				.address(s.getAddresses().stream().map(this::toAddressDto).collect(Collectors.toList())).build();
	}

	private StaffProfileDto toProfileDto(Staff s, LeaveDocumentDto leave, TeamDocumentDto team) {
		return StaffProfileDto.builder().staffId(s.getStaffId()).name(s.getName()).email(s.getEmail())
				.title(s.getTitle()).jobTitle(s.getJobTitle()).grade(s.getGrade())
				.businessCardType(s.getBusinessCardType())
				.address(s.getAddresses().stream().map(this::toAddressDto).collect(Collectors.toList()))
				.leaveDocumentDto(leave).teamMembers(team).build();
	}

	private AddressDto toAddressDto(Address a) {
		return AddressDto.builder().address(a.getAddress()).street(a.getStreet()).city(a.getCity())
				.country(a.getCountry()).addressType(a.getAddressType()).build();
	}

	private Address toAddress(AddressDto dto) {
		return Address.builder().address(dto.getAddress()).street(dto.getStreet()).city(dto.getCity())
				.country(dto.getCountry()).addressType(dto.getAddressType()).build();
	}
}
