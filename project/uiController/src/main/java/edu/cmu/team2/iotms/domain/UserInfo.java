package edu.cmu.team2.iotms.domain;

public class UserInfo {
	private String userId;
	private String userName;
	private String userPasswd;
	private String userMail;
	private String userTwitter;
	
	public UserInfo() {
		userId = "";
		userName = "";
		userPasswd = "";
		userMail = "";
		userTwitter = "";
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPasswd() {
		return userPasswd;
	}
	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}
	public String getUserMail() {
		return userMail;
	}
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	public String getUserTwitter() {
		return userTwitter;
	}
	public void setUserTwitter(String userTwitter) {
		this.userTwitter = userTwitter;
	}
}
