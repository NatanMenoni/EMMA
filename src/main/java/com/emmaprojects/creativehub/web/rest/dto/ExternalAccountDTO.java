package com.emmaprojects.creativehub.web.rest.dto;

import org.joda.time.DateTime;

public class ExternalAccountDTO {

	private String externalId;
	private boolean connected;
	private DateTime expireTime;

	public ExternalAccountDTO() {
	}

	public ExternalAccountDTO(String externalId) {
		super();
		this.externalId = externalId;
	}

    public ExternalAccountDTO(boolean connected, DateTime expireTime) {
        super();
        this.connected = connected;
        this.expireTime = expireTime;
    }

	public String getExternalId() {
		return externalId;
	}

    public boolean isConnected() {
		return connected;
	}

	public DateTime getExpireTime() {
		return expireTime;
	}

	@Override
    public String toString() {
        return "ExternalAccountDTO{" +
        ", externalId=" + externalId +
        ", connected=" + connected +
        ", expireTime=" + expireTime +
        '}';
    }

}
