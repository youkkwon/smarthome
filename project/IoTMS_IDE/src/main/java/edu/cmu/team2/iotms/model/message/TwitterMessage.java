package edu.cmu.team2.iotms.model.message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import Database.LoggerDao;
import edu.cmu.team2.iotms.application.UserServiceImpl;
import edu.cmu.team2.iotms.conf.SpringAppConfig;
import edu.cmu.team2.iotms.domain.UserInfo;
import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterMessage extends IoTMSMessage {
	private UserServiceImpl userService;
	
	private String consumerKeyStr;
	private String consumerSecretStr;
	private String accessTokenStr;
	private String accessTokenSecretStr;

	public TwitterMessage() {
		super();
		
		consumerKeyStr = "5AshEt485tLe3KIDosz5tvkC9";
		consumerSecretStr = "HqdJGOuTK12IFI6gX8A51siH1uvaB8uCnSV6kaxwfKVBMKPcs5";
		accessTokenStr = "3325797479-ZNcQCIQWsHDph4qhCpZwSMa5DBYTfhN0TL5Xwds";
		accessTokenSecretStr = "GWnNRz9EVz3exeH3W9NKvMHEGcCCzi8KavheSylcBTFrS";
		
		SpringAppConfig sac = new SpringAppConfig();
		userService = (UserServiceImpl) sac.userService();
	}
	
	private List<UserInfo> getReceiver() {
		return userService.getUsers();
	}

	@Override
	protected void sendConfirmMessage(String desc) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void sendEmergencyMessage(String desc) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		List<UserInfo> users = getReceiver();
		for(UserInfo user:users) {
			String recipientId = user.getUserTwitter();
			if(recipientId != null && recipientId.compareTo("")!=0) {
				directMessage(recipientId, dateFormat.format(cal.getTime())+" Emergency "+desc);
				LoggerDao.getInstance().addMessageHistory("Send emergency message("+desc+") to "+recipientId);
			}
		}
	}

	@Override
	protected void sendMalFunctionMessage(String desc) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		List<UserInfo> users = getReceiver();
		for(UserInfo user:users) {
			String recipientId = user.getUserTwitter();
			if(recipientId != null && recipientId.compareTo("")!=0) {
				directMessage(recipientId, dateFormat.format(cal.getTime())+" Emerge mulfunction on system. "+desc);
				LoggerDao.getInstance().addMessageHistory("Send mulfunction message("+desc+") to "+recipientId);
			}
		}
	}

	@Override
	protected void sendPostMessage(String desc) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		List<UserInfo> users = getReceiver();
		for(UserInfo user:users) {
			String recipientId = user.getUserTwitter();
			if(recipientId != null && recipientId.compareTo("")!=0) {
				directMessage(recipientId, dateFormat.format(cal.getTime())+" Check your mailbox in your house. "+desc);
				LoggerDao.getInstance().addMessageHistory("Send check mailbox message("+desc+") to "+recipientId);
			}
		}
	}
	
	// directMessage doesn't work, it will be fixed.
	private void directMessage(String recipientId, String message) {
		try {
			Twitter twitter = new TwitterFactory().getInstance();

			twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
			AccessToken accessToken = new AccessToken(accessTokenStr,
					accessTokenSecretStr);
			twitter.setOAuthAccessToken(accessToken);
			
			DirectMessage dm = twitter.sendDirectMessage(recipientId, message);
			System.out.println("Sent: " + dm.getText() + " to " + dm.getRecipientScreenName());
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
/*
	private void tweet(String message) {
		getReceiver();

		try {
			Twitter twitter = new TwitterFactory().getInstance();

			twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
			AccessToken accessToken = new AccessToken(accessTokenStr,
					accessTokenSecretStr);
			twitter.setOAuthAccessToken(accessToken); 

			twitter.updateStatus(message);

		} catch (TwitterException te) {
			te.printStackTrace();
		}
	}
*/
}
