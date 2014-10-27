package com.luisdelarosa.stackoverflow;

public class MockStackOverflowUser extends StackOverflowUser {
	public String getUsername() {
		return "louielouie";
	}

	@Override
	public String getReputation() {
		return "5,555";
	}
}
