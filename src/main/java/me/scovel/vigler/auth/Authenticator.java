/*
 * MIT License
 * 
 * Copyright (c) 2017 Calder Young (LAX1DUDE)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */

package me.scovel.vigler.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class Authenticator {
	
	private String clientToken;
	private String accessToken;
	private String userID;
	private List<UserProfile> availableProfiles;
	private UserProfile selectedProfile;
	private Map<String,String> userProperties;
	private Game game;
	
	public Authenticator(String clientToken, Game game) {
		this.clientToken = clientToken;
		this.game = game;
	}
	
	public Authenticator(JSONObject json, Game game) {
		this.game = game;
		this.load(json);
	}
	
	/**
	 * Minecraft will default
	 */
	public Authenticator(String clientToken) {
		this.clientToken = clientToken;
		this.game = Game.MINECRAFTv1;
	}
	
	/**
	 * Minecraft will default
	 */
	public Authenticator(JSONObject json) {
		this.game = Game.MINECRAFTv1;
		this.load(json);
	}
	
	private void load(JSONObject json) {
		this.username          = json.getString("username");
		this.clientToken       = json.getString("clientToken");
		this.accessToken       = json.getString("accessToken");
		this.selectedProfile   = new UserProfile(json.getJSONObject("selectedProfile"));
		this.userID            = json.getString("userId");
		this.availableProfiles = new ArrayList();
		this.userProperties    = new HashMap();
		json.getJSONArray("availableProfiles").forEach((ooooo) -> this.availableProfiles.add(new UserProfile((JSONObject)ooooo)));
		json.getJSONObject("userProperties").toMap().forEach((dee, vile) -> this.userProperties.put(dee, (String) vile));
	}

	private String username = null;
	private String password = null;

	public void setUsernamePassword(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public void setUsernamePasswordAndLogin(String username, String password) throws AuthenticationException {
		this.username = username;
		this.password = password;
		this.loginWithPassword();
	}

	public void loginWithPassword() throws AuthenticationException {
		
	}
	
	public void loginWithToken() throws AuthenticationException {
		
	}
	
	public void isTokenValid() {
		
	}
	
	public void setUsernamePasswordAndLogout(String username, String password) throws AuthenticationException {
		this.username = username;
		this.password = password;
		this.logout();
	}
	
	public void logout() throws AuthenticationException {
		
	}
	
	public void invalidateToken() {
		
	}
}
