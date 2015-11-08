package com.emmaprojects.creativehub.web.rest.dto;

import com.emmaprojects.creativehub.security.social.ExternalAccountProvider;
import org.joda.time.DateTime;

public class ExternalAccountDTO {

	private ExternalAccountProvider externalProvider;
	private String externalId;
	private boolean connected;
	private DateTime expireTime;

	public ExternalAccountDTO() {
	}

	public ExternalAccountDTO(ExternalAccountProvider externalProvider,
                              String externalId) {
		super();
		this.externalProvider = externalProvider;
		this.externalId = externalId;
	}

	public ExternalAccountDTO(ExternalAccountProvider externalProvider,
                              boolean connected, DateTime expireTime) {
		super();
		this.externalProvider = externalProvider;
		this.connected = connected;
		this.expireTime = expireTime;
	}

	public ExternalAccountProvider getExternalProvider() {
		return externalProvider;
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
        "externalProvider='" + externalProvider + '\'' +
        ", externalId=" + externalId +
        ", connected=" + connected +
        ", expireTime=" + expireTime +
        '}';
    }

}
