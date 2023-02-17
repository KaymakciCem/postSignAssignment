package com.signavio;

import java.time.OffsetDateTime;
import java.util.Objects;

public class EventlogRow {
	
	String caseId;
	String eventName;
	OffsetDateTime timestamp;

	public EventlogRow(String eventName, OffsetDateTime timestamp) {
		this.eventName = eventName;
		this.timestamp = timestamp;
	}

	public EventlogRow(String caseId, String eventName, OffsetDateTime timestamp) {
		this.caseId = caseId;
		this.eventName = eventName;
		this.timestamp = timestamp;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public OffsetDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(OffsetDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EventlogRow that = (EventlogRow) o;
		return caseId.equals(that.caseId) && eventName.equals(that.eventName) && timestamp.equals(that.timestamp);
	}

	@Override
	public int hashCode() {
		return Objects.hash(caseId, eventName, timestamp);
	}
}
