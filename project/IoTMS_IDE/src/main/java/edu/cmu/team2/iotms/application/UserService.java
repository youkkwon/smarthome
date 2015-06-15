package edu.cmu.team2.iotms.application;

import java.util.List;

import edu.cmu.team2.iotms.domain.UserInfo;

public interface UserService {
	public List<UserInfo> getUsers();
	public void createUser(UserInfo user);
	public void removeUser(UserInfo user);
	public void updateUser(UserInfo user);
}
