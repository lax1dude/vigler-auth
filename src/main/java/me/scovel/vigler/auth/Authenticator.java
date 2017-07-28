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

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class Authenticator {
	
	private String clientToken;
	private String accessToken;
	private String userID;
	private UserProfile selectedProfile;
	private Map<String,String> userProperties;
	private Game game;
	
	public String getClientToken() {
		return clientToken;
	}

	public String getAccessToken() {
		return accessToken;
	}
	
	/**
	 * this is not what you pass to minecraft, the uuid to pass to minecraft is in .
	 */
	public String getUserID() {
		return userID;
	}

	public Map<String, String> getUserProperties() {
		return userProperties;
	}

	public Game getGame() {
		return game;
	}

	public String getUsername() {
		return username;
	}

	void setClientToken(String clientToken) {
		this.clientToken = clientToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	void setUserID(String userID) {
		this.userID = userID;
	}

	void setSelectedProfile(UserProfile selectedProfile) {
		this.selectedProfile = selectedProfile;
	}

	void setUserProperties(Map<String, String> userProperties) {
		this.userProperties = userProperties;
	}

	void setGame(Game game) {
		this.game = game;
	}

	public Authenticator(String clientToken, Game game) {
		this.clientToken = clientToken;
		this.game = game;
		this.userProperties = new HashMap();
	}
	
	public Authenticator(JSONObject json, Game game) {
		this.game = game;
		this.userProperties = new HashMap();
		this.load(json);
	}
	
	/**
	 * Minecraft will default
	 */
	public Authenticator(String clientToken) {
		this.clientToken = clientToken;
		this.userProperties = new HashMap();
		this.game = Game.MINECRAFTv1;
	}
	
	/**
	 * Minecraft will default
	 */
	public Authenticator(JSONObject json) {
		this.game = Game.MINECRAFTv1;
		this.userProperties = new HashMap();
		this.load(json);
	}
	
	private void load(JSONObject json) {
		this.username          = json.optString("username");
		this.clientToken       = json.optString("clientToken");
		this.accessToken       = json.optString("accessToken");
		this.selectedProfile   = json.has("selectedProfile") ? new UserProfile(json.getJSONObject("selectedProfile")) : null;
		this.userID            = json.optString("userId");
		if(json.has("selectedProfile")) {
			this.userProperties.clear();
			json.getJSONObject("userProperties").toMap().forEach((dee, vile) -> this.userProperties.put(dee, (String) vile));
		}
	}
	
	public JSONObject saveToJSON() {
		return (new JSONObject())
				.put("username", this.username)
				.put("clientToken", this.clientToken)
				.put("accessToken", this.accessToken)
				.put("selectedProfile", this.selectedProfile == null ? null : this.selectedProfile.json())
				.put("userId", this.userID)
				.put("userProperties", this.userProperties);
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
		try {
			HttpResponse<JsonNode> response = Requestler.startRequest("authenticate")
					.body(new JSONObject()
							.put("agent", this.game.agent)
							.put("username", this.username)
							.put("password", this.password)
							.put("clientToken", this.clientToken)
							.put("requestUser", true)).asJson();
			Requestler.handleResponseCode(response);
			
			JSONObject jsonObject = response.getBody().getObject();
			
			this.accessToken = jsonObject.getString("accessToken");
			this.clientToken = jsonObject.getString("clientToken");
			this.selectedProfile = new UserProfile(jsonObject.getJSONObject("selectedProfile"));
			JSONObject userJson = jsonObject.getJSONObject("user");
			this.userID = userJson.getString("id");
			if(userJson.has("properties")) {
				this.userProperties.clear();
				userJson.getJSONArray("properties").forEach((yeeeee) -> {JSONObject j = (JSONObject)yeeeee; this.userProperties.put(j.getString("name"), j.getString("value"));});
			}
		} catch (Throwable t) {
			throw new AuthenticationException("Could not preform request!", t);
		}
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
	
	public UserProfile getSelectedProfile() {
		return this.selectedProfile;
	}
}
