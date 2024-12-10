package com.example.courseregistration.service;

import com.example.courseregistration.model.User;

interface UserFactoryAPI {
	public abstract User createUser(String csvData);
}
