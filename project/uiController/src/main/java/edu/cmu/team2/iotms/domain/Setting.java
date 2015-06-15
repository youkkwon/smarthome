package edu.cmu.team2.iotms.domain;

public class Setting {
	private String securitySettime;
	private String lightoffSettime;
	private String loggingDuration;
	
	public Setting() {
		securitySettime = "10";
		lightoffSettime = "5";
		loggingDuration = "21600";
	}
	
	public String getSecuritySettime() {
		return securitySettime;
	}

	public void setSecuritySettime(String securitySettime) {
		this.securitySettime = securitySettime;
	}

	public String getLightoffSettime() {
		return lightoffSettime;
	}

	public void setLightoffSettime(String lightoffSettime) {
		this.lightoffSettime = lightoffSettime;
	}

	public String getLoggingDuration() {
		return loggingDuration;
	}

	public void setLoggingDuration(String loggingDuration) {
		this.loggingDuration = loggingDuration;
	}
}
