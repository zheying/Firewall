package com.timedancing.easyfirewall.event;

/**
 * Created by zengzheying on 16/1/6.
 */
public class VPNEvent {
	private Status mStatus;

	public VPNEvent(Status status) {
		mStatus = status;
	}

	public boolean isEstablished() {
		return mStatus == Status.ESTABLISHED;
	}

	public Status getStatus() {
		return mStatus;
	}

	public enum Status {
		STARTING,
		ESTABLISHED,
		UNESTABLISHED,
	}
}
