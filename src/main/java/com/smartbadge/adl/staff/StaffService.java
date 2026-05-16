package com.smartbadge.adl.staff;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.smartbadge.adl.leave.LeaveDocumentDto;
import com.smartbadge.adl.leave.LeaveService;
import com.smartbadge.adl.shared.exception.DuplicateResourceException;
import com.smartbadge.adl.shared.exception.ResourceNotFoundException;
import com.smartbadge.adl.shared.exception.StaffAlreadyExistsException;
import com.smartbadge.adl.staff.dto.CreateStaffRequest;
import com.smartbadge.adl.staff.dto.UpdateStaffRequest;
import com.smartbadge.adl.team.TeamDocumentDto;
import com.smartbadge.adl.team.TeamService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffService {

	private final StaffRepository staffRepository;
	private final LeaveService leaveService;
	private final TeamService teamService;
	private final ModelMapper modelMapper;

	// ── READ ─────────────────────────────────────────────────────────────────

	public Page<StaffDto> findAll(Pageable pageable) {
		log.info("Finding staff page: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
		Page<StaffDto> staffPage = staffRepository.findAll(pageable).map(this::toDto);
		log.info("Found {} staff records on page {} of {}", staffPage.getNumberOfElements(), staffPage.getNumber(),
				staffPage.getTotalPages());
		return staffPage;
	}

	public StaffDto findByStaffId(String staffId) {
		log.info("Finding staff by staffId: {}", staffId);
		StaffDto dto = toDto(getOrThrow(staffId));
		log.info("Found staff by staffId: {}", staffId);
		LeaveDocumentDto leaveDoc = leaveService.findByStaffId(staffId).orElse(new LeaveDocumentDto());
		log.info("Fetcehd Leave Details by staffId: {}", staffId);
		log.trace("Mapping Leave to StaffDto: {}", staffId);
		dto.setLeaves(leaveDoc);
		TeamDocumentDto teamMembers = teamService.findByStaffId(staffId).orElse(null);
		log.info("Finding team document by staffId: {}", staffId);
		dto.setTeamMembers(teamMembers);
		return dto;
	}

	public StaffProfileDto getFullProfile(String staffId) {
		log.info("Building full staff profile for staffId: {}", staffId);
		Staff staff = getOrThrow(staffId);
		LeaveDocumentDto leaveDoc = leaveService.findByStaffId(staffId).orElse(null);
		TeamDocumentDto teamDoc = teamService.findByStaffId(staffId).orElse(null);
		StaffProfileDto profile = toProfileDto(staff, leaveDoc, teamDoc);
		log.info("Built full staff profile for staffId: {}, hasLeave={}, hasTeam={}", staffId, leaveDoc != null,
				teamDoc != null);
		return profile;
	}

	// ── CREATE ────────────────────────────────────────────────────────────────

	public StaffDto create(CreateStaffRequest req) {
		log.info("Creating staff: {}", req.getStaffId());
		if (staffRepository.existsById(req.getStaffId())) {
			log.warn("Create staff rejected because staffId already exists: {}", req.getStaffId());
			throw new StaffAlreadyExistsException();
		}
		if (staffRepository.existsByEmail(req.getEmail())) {
			log.warn("Create staff rejected because email already exists for staffId: {}", req.getStaffId());
			throw new StaffAlreadyExistsException();
		}
		Staff staff = modelMapper.map(req, Staff.class);
		if (staff.getAddresses() == null) {
			staff.setAddresses(new ArrayList<>());
		}
		StaffDto dto = toDto(staffRepository.save(staff));
		log.info("Created staff: {}", staff.getStaffId());
		return dto;
	}

	// ── UPDATE ────────────────────────────────────────────────────────────────

	public StaffDto update(String staffId, UpdateStaffRequest req) {
		log.info("Updating staff: {}", staffId);
		Staff staff = getOrThrow(staffId);
		if (req.getName() != null)
			staff.setName(req.getName());
		if (req.getEmail() != null) {
			if (!req.getEmail().equals(staff.getEmail()) && staffRepository.existsByEmail(req.getEmail())) {
				log.warn("Update staff rejected because email already exists for staffId: {}", staffId);
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
		log.info("Deleting staff: {}", staffId);
		Staff staff = getOrThrow(staffId);
		staffRepository.delete(staff);
		log.info("Deleted staff: {}", staffId);
	}

	// ── ADDRESS MANAGEMENT ───────────────────────────────────────────────────

	public StaffDto addAddress(String staffId, AddressDto dto) {
		log.info("Adding or replacing {} address for staff: {}", dto.getAddressType(), staffId);
		Staff staff = getOrThrow(staffId);
		Address address = modelMapper.map(dto, Address.class);
		staff.getAddresses().removeIf(a -> a.getAddressType() == address.getAddressType());
		staff.getAddresses().add(address);
		StaffDto staffDto = toDto(staffRepository.save(staff));
		log.info("Added or replaced {} address for staff: {}, addressCount={}", address.getAddressType(), staffId,
				staff.getAddresses().size());
		return staffDto;
	}

	public StaffDto updateAddress(String staffId, AddressType addressType, AddressDto dto) {
		log.info("Updating {} address for staff: {}", addressType, staffId);
		Staff staff = getOrThrow(staffId);
		staff.getAddresses().removeIf(a -> a.getAddressType() == addressType);
		Address updated = modelMapper.map(dto, Address.class);
		updated.setAddressType(addressType);
		staff.getAddresses().add(updated);
		StaffDto staffDto = toDto(staffRepository.save(staff));
		log.info("Updated {} address for staff: {}", addressType, staffId);
		return staffDto;
	}

	public StaffDto removeAddress(String staffId, AddressType addressType) {
		log.info("Removing {} address for staff: {}", addressType, staffId);
		Staff staff = getOrThrow(staffId);
		boolean removed = staff.getAddresses().removeIf(a -> a.getAddressType() == addressType);
		StaffDto staffDto = toDto(staffRepository.save(staff));
		log.info("Removed {} address for staff: {}, removed={}, addressCount={}", addressType, staffId, removed,
				staff.getAddresses().size());
		return staffDto;
	}

	// ── MAPPING ───────────────────────────────────────────────────────────────

	private Staff getOrThrow(String staffId) {
		log.info("Loading staff document: {}", staffId);
		return staffRepository.findByStaffId(staffId)
				.orElseThrow(() -> new ResourceNotFoundException("Staff", staffId));
	}

	private StaffDto toDto(Staff s) {
		log.trace("Mapping Staff to StaffDto: {}", s.getStaffId());
		return modelMapper.map(s, StaffDto.class);
	}

	private StaffProfileDto toProfileDto(Staff s, LeaveDocumentDto leave, TeamDocumentDto team) {
		log.trace("Mapping Staff to StaffProfileDto: {}", s.getStaffId());
		StaffProfileDto dto = modelMapper.map(s, StaffProfileDto.class);
		dto.setLeaveDocumentDto(leave);
		dto.setTeamMembers(team);
		return dto;
	}
}
