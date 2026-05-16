package com.smartbadge.adl.shared.config;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcFilter extends OncePerRequestFilter {

	private static final String TRACE_ID = "traceId";
	private static final String SPAN_ID = "spanId";
	private static final String STAFF_ID = "staffId";
	private static final String TRACE_PARENT = "traceparent";
	private static final String TRACE_ID_HEADER = "X-Trace-Id";
	private static final String REQUEST_ID_HEADER = "X-Request-Id";
	private static final String SPAN_ID_HEADER = "X-Span-Id";
	private static final String STAFF_ID_HEADER = "X-Staff-Id";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String traceParent = request.getHeader(TRACE_PARENT);
		String traceId = firstNonBlank(traceIdFromTraceParent(traceParent), request.getHeader(TRACE_ID_HEADER),
				request.getHeader(REQUEST_ID_HEADER), newTraceId());
		String spanId = firstNonBlank(spanIdFromTraceParent(traceParent), request.getHeader(SPAN_ID_HEADER),
				newSpanId());
		String staffId = firstNonBlank(request.getHeader(STAFF_ID_HEADER), request.getParameter(STAFF_ID),
				extractStaffIdFromPath(request.getRequestURI()));

		try {
			MDC.put(TRACE_ID, traceId);
			MDC.put(SPAN_ID, spanId);
			if (staffId != null) {
				MDC.put(STAFF_ID, staffId);
			}
			response.setHeader(TRACE_ID_HEADER, traceId);
			response.setHeader(SPAN_ID_HEADER, spanId);
			filterChain.doFilter(request, response);
		} finally {
			MDC.remove(TRACE_ID);
			MDC.remove(SPAN_ID);
			MDC.remove(STAFF_ID);
		}
	}

	private String traceIdFromTraceParent(String traceParent) {
		String[] parts = splitTraceParent(traceParent);
		return parts != null ? parts[1] : null;
	}

	private String spanIdFromTraceParent(String traceParent) {
		String[] parts = splitTraceParent(traceParent);
		return parts != null ? parts[2] : null;
	}

	private String[] splitTraceParent(String traceParent) {
		if (traceParent == null || traceParent.isBlank()) {
			return null;
		}
		String[] parts = traceParent.split("-");
		if (parts.length == 4 && parts[1].length() == 32 && parts[2].length() == 16) {
			return parts;
		}
		return null;
	}

	private String extractStaffIdFromPath(String path) {
		if (path == null || path.isBlank()) {
			return null;
		}
		String[] parts = path.split("/");
		for (int i = 0; i < parts.length - 1; i++) {
			if ("staff".equals(parts[i]) || "team".equals(parts[i])) {
				return blankToNull(parts[i + 1]);
			}
			if ("leave".equals(parts[i])) {
				int staffIndex = i + 1;
				if (staffIndex < parts.length && "admin".equals(parts[staffIndex])) {
					staffIndex++;
				}
				if (staffIndex < parts.length) {
					return blankToNull(parts[staffIndex]);
				}
			}
		}
		return null;
	}

	private String firstNonBlank(String... values) {
		for (String value : values) {
			String normalized = blankToNull(value);
			if (normalized != null) {
				return normalized;
			}
		}
		return null;
	}

	private String blankToNull(String value) {
		return value == null || value.isBlank() ? null : value;
	}

	private String newTraceId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	private String newSpanId() {
		return newTraceId().substring(0, 16);
	}
}
