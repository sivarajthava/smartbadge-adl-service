package com.smartbadge.adl.preference;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.smartbadge.adl.preference.dto.AddQuickActionRequest;
import com.smartbadge.adl.preference.dto.AddWidgetRequest;
import com.smartbadge.adl.preference.dto.CreatePreferenceRequest;
import com.smartbadge.adl.preference.dto.UpdatePreferenceRequest;
import com.smartbadge.adl.preference.dto.UpdateQuickActionRequest;
import com.smartbadge.adl.preference.dto.UpdateSettingsRequest;
import com.smartbadge.adl.preference.dto.UpdateWidgetRequest;
import com.smartbadge.adl.shared.exception.DuplicateResourceException;
import com.smartbadge.adl.shared.exception.ResourceNotFoundException;
import com.smartbadge.adl.shared.exception.UserNotFoundException;
import com.smartbadge.adl.shared.util.DateUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPreferenceService {

	private final UserPreferenceRepository preferenceRepository;
	private final ModelMapper modelMapper;

	// ── READ ─────────────────────────────────────────────────────────────────

	public UserPreferenceResponseDto getByUserId(String userId) {
		log.info("Getting preference for userId: {}", userId);
		UserPreferenceResponseDto dto = toResponseDto(findDocOrThrow(userId));
		log.info("Got preference for userId: {}, widgetCount={}, quickActionCount={}", userId, dto.getWidgets().size(),
				dto.getQuickActions().size());
		return dto;
	}

	// ── CREATE ────────────────────────────────────────────────────────────────

	public UserPreferenceResponseDto create(String userId, CreatePreferenceRequest req) {
		log.info("Creating preference for userId: {}", userId);
		if (preferenceRepository.existsByUserId(userId)) {
			log.info("Create preference failed because preference already exists for userId: {}", userId);
			throw new DuplicateResourceException("User Preference", userId);
		}
		UserPreferenceDocument doc = modelMapper.map(req, UserPreferenceDocument.class);
		doc.setUserId(userId);
		ensureDefaults(doc);
		UserPreferenceResponseDto dto = toResponseDto(preferenceRepository.save(doc));
		log.info("Created preference for userId: {}, widgetCount={}, quickActionCount={}", userId,
				dto.getWidgets().size(), dto.getQuickActions().size());
		return dto;
	}

	// ── UPDATE ────────────────────────────────────────────────────────────────

	public UserPreferenceResponseDto update(String userId, UpdatePreferenceRequest req) {
		log.info("Updating preference for userId: {}", userId);
		UserPreferenceDocument doc = findDocOrThrow(userId);
		modelMapper.map(req, doc);
		ensureDefaults(doc);
		UserPreferenceResponseDto dto = toResponseDto(preferenceRepository.save(doc));
		log.info("Updated preference for userId: {}, widgetCount={}, quickActionCount={}", userId,
				dto.getWidgets().size(), dto.getQuickActions().size());
		return dto;
	}

	public UserPreferenceResponseDto updateSettings(String userId, UpdateSettingsRequest req) {
		log.info("Updating preference settings for userId: {}", userId);
		UserPreferenceDocument doc = findDocOrThrow(userId);
		UserPreferences prefs = doc.getPreferences() != null ? doc.getPreferences() : new UserPreferences();
		modelMapper.map(req, prefs);
		doc.setPreferences(prefs);
		UserPreferenceResponseDto dto = toResponseDto(preferenceRepository.save(doc));
		log.info("Updated preference settings for userId: {}", userId);
		return dto;
	}

	// ── DELETE ────────────────────────────────────────────────────────────────

	public void delete(String userId) {
		log.info("Deleting preference for userId: {}", userId);
		findDocOrThrow(userId);
		preferenceRepository.deleteByUserId(userId);
		log.info("Deleted preference for userId: {}", userId);
	}

	// ── WIDGET MANAGEMENT ─────────────────────────────────────────────────────

	public UserPreferenceResponseDto addWidget(String userId, AddWidgetRequest req) {
		log.info("Adding widget {} for userId: {}", req.getWidgetId(), userId);
		UserPreferenceDocument doc = preferenceRepository.findByUserId(userId)
				.orElse(UserPreferenceDocument.builder().userId(userId).widgets(new ArrayList<>())
						.quickActions(new ArrayList<>()).preferences(new UserPreferences()).build());
		boolean exists = doc.getWidgets().stream().anyMatch(w -> w.getWidgetId().equals(req.getWidgetId()));
		if (exists) {
			log.info("Add widget failed because widget already exists for userId: {}, widgetId: {}", userId,
					req.getWidgetId());
			throw new DuplicateResourceException("Widget", req.getWidgetId());
		}
		doc.getWidgets().add(modelMapper.map(req, Widget.class));
		UserPreferenceResponseDto dto = toResponseDto(preferenceRepository.save(doc));
		log.info("Added widget {} for userId: {}, widgetCount={}", req.getWidgetId(), userId, dto.getWidgets().size());
		return dto;
	}

	public UserPreferenceResponseDto updateWidget(String userId, String widgetId, UpdateWidgetRequest req) {
		log.info("Updating widget {} for userId: {}", widgetId, userId);
		UserPreferenceDocument doc = findDocOrThrow(userId);
		Widget w = doc.getWidgets().stream().filter(x -> x.getWidgetId().equals(widgetId)).findFirst()
				.orElseThrow(() -> {
					log.info("Update widget failed because widget was not found for userId: {}, widgetId: {}", userId,
							widgetId);
					return new ResourceNotFoundException("Widget", widgetId);
				});
		modelMapper.map(req, w);
		UserPreferenceResponseDto dto = toResponseDto(preferenceRepository.save(doc));
		log.info("Updated widget {} for userId: {}", widgetId, userId);
		return dto;
	}

	public UserPreferenceResponseDto removeWidget(String userId, String widgetId) {
		log.info("Removing widget {} for userId: {}", widgetId, userId);
		UserPreferenceDocument doc = findDocOrThrow(userId);
		if (!doc.getWidgets().removeIf(w -> w.getWidgetId().equals(widgetId))) {
			log.info("Remove widget failed because widget was not found for userId: {}, widgetId: {}", userId,
					widgetId);
			throw new ResourceNotFoundException("Widget", widgetId);
		}
		UserPreferenceResponseDto dto = toResponseDto(preferenceRepository.save(doc));
		log.info("Removed widget {} for userId: {}, widgetCount={}", widgetId, userId, dto.getWidgets().size());
		return dto;
	}

	// ── QUICK ACTION MANAGEMENT ───────────────────────────────────────────────

	public UserPreferenceResponseDto addQuickAction(String userId, AddQuickActionRequest req) {
		log.info("Adding quick action {} for userId: {}", req.getActionId(), userId);
		UserPreferenceDocument doc = preferenceRepository.findByUserId(userId)
				.orElse(UserPreferenceDocument.builder().userId(userId).widgets(new ArrayList<>())
						.quickActions(new ArrayList<>()).preferences(new UserPreferences()).build());
		boolean exists = doc.getQuickActions().stream().anyMatch(q -> q.getActionId().equals(req.getActionId()));
		if (exists) {
			log.info("Add quick action failed because action already exists for userId: {}, actionId: {}", userId,
					req.getActionId());
			throw new DuplicateResourceException("Quick Action", req.getActionId());
		}
		doc.getQuickActions().add(modelMapper.map(req, QuickAction.class));
		UserPreferenceResponseDto dto = toResponseDto(preferenceRepository.save(doc));
		log.info("Added quick action {} for userId: {}, quickActionCount={}", req.getActionId(), userId,
				dto.getQuickActions().size());
		return dto;
	}

	public UserPreferenceResponseDto updateQuickAction(String userId, String actionId, UpdateQuickActionRequest req) {
		log.info("Updating quick action {} for userId: {}", actionId, userId);
		UserPreferenceDocument doc = findDocOrThrow(userId);
		QuickAction q = doc.getQuickActions().stream().filter(x -> x.getActionId().equals(actionId)).findFirst()
				.orElseThrow(() -> {
					log.info("Update quick action failed because action was not found for userId: {}, actionId: {}",
							userId, actionId);
					return new ResourceNotFoundException("Quick Action", actionId);
				});
		modelMapper.map(req, q);
		UserPreferenceResponseDto dto = toResponseDto(preferenceRepository.save(doc));
		log.info("Updated quick action {} for userId: {}", actionId, userId);
		return dto;
	}

	public UserPreferenceResponseDto removeQuickAction(String userId, String actionId) {
		log.info("Removing quick action {} for userId: {}", actionId, userId);
		UserPreferenceDocument doc = findDocOrThrow(userId);
		if (!doc.getQuickActions().removeIf(q -> q.getActionId().equals(actionId))) {
			log.info("Remove quick action failed because action was not found for userId: {}, actionId: {}", userId,
					actionId);
			throw new ResourceNotFoundException("Quick Action", actionId);
		}
		UserPreferenceResponseDto dto = toResponseDto(preferenceRepository.save(doc));
		log.info("Removed quick action {} for userId: {}, quickActionCount={}", actionId, userId,
				dto.getQuickActions().size());
		return dto;
	}

	// ── Mapping ───────────────────────────────────────────────────────────────

	private UserPreferenceDocument findDocOrThrow(String userId) {
		log.info("Finding preference document for userId: {}", userId);
		return preferenceRepository.findByUserId(userId).orElseThrow(() -> {
			log.info("Preference document was not found for userId: {}", userId);
			return new UserNotFoundException(userId);
		});
	}

	UserPreferenceResponseDto toResponseDto(UserPreferenceDocument doc) {
		FacetDataDto facetData = FacetDataDto.builder()
				.createdAt(doc.getCreatedAt() != null ? DateUtils.ISO_FMT.format(doc.getCreatedAt()) : null)
				.updatedAt(doc.getUpdatedAt() != null ? DateUtils.ISO_FMT.format(doc.getUpdatedAt()) : null).build();
		UserPreferenceResponseDto dto = modelMapper.map(doc, UserPreferenceResponseDto.class);
		dto.setSuccess(true);
		dto.setFacetData(facetData);
		return dto;
	}

	private void ensureDefaults(UserPreferenceDocument doc) {
		if (doc.getWidgets() == null) {
			doc.setWidgets(new ArrayList<>());
		}
		if (doc.getQuickActions() == null) {
			doc.setQuickActions(new ArrayList<>());
		}
		if (doc.getPreferences() == null) {
			doc.setPreferences(new UserPreferences());
		}
	}
}
