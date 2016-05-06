package com.timedancing.easyfirewall.event;

/**
 * Created by zengzheying on 16/1/29.
 */
public class HostUpdateEvent {

	private Status mStatus;

	public HostUpdateEvent(Status status) {
		mStatus = status;
	}

	public Status getStatus() {
		return mStatus;
	}

	public enum Status {
		Updating,
		UpdateFinished,
	}
}
