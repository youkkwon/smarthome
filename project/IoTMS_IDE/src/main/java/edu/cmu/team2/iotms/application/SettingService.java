package edu.cmu.team2.iotms.application;

import edu.cmu.team2.iotms.domain.Setting;

public interface SettingService {
	public Setting getSettings();
	public void setSettings(Setting settings);
}
