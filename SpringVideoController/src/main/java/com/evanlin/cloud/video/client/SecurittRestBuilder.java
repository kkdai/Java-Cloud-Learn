package com.evanlin.cloud.video.client;

import java.lang.annotation.Annotation;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.http.FormUrlEncoded;
import retrofit.mime.FormUrlEncodedTypedOutput;

public class SecurittRestBuilder extends RestAdapter.Builder {
	private  class OAuthHandler implements RequestInterceptor{

		private boolean loggedIn;
		private Client client_;
		private String tokenIssuingEndpoint_;
		private String username_;
		private String password_;
		private String clientId_;
		private String clientSecret_;
		private String accessToken_;

		public OAuthHandler(Client client, String tokenIssuingEndpoint, String username , String password, String clientId, String clientSecret) {
			super();
			this.client_ = client;
			this.tokenIssuingEndpoint_ =  tokenIssuingEndpoint;
			this.username_ = username;
			this.password_ = password;
			this.clientId_ = clientId;
			this.clientSecret_ = clientSecret;
		}
		
		@Override
		public void intercept(RequestFacade request) {
			if (loggedIn) {
				try {		
					FormUrlEncodedTypedOutput to = new FormUrlEncodedTypedOutput();
					to.addField("username", username_);
					to.addField("password", password_);
					to.addField("client_id", clientId_);
					to.addField("client_secret", clientSecret_);
					to.addField("grant_type", "password");
					
					String base64AuthString = BaseEncoding.base64().encode(new String(clientId_ + ":" + clientSecret_).getBytes());
	
					// Add the basic authorization header
					List<Header> headers = new ArrayList<Header>();
					headers.add(new Header("Authorization", "Basic " + base64AuthString));
	
					// Create the actual password grant request using the data above
					Request req = new Request("POST", tokenIssuingEndpoint_, headers, to);
					
					// Request the password grant.
					Response resp = client_.execute(req);
					
					// Make sure the server responded with 200 OK
					if (resp.getStatus() < 200 || resp.getStatus() > 299) {
						// If not, we probably have bad credentials
						throw new RuntimeException("Login failure: "
								+ resp.getStatus() + " - " + resp.getReason());
					} else {
						// Extract the string body from the response
				        String body = IOUtils.toString(resp.getBody().in());
						
						// Extract the access_token (bearer token) from the response so that we
				        // can add it to future requests.
						accessToken_ = new Gson().fromJson(body, JsonObject.class).get("access_token").getAsString();
						
						// Add the access_token to this request as the "Authorization"
						// header.
						request.addHeader("Authorization", "Bearer " + accessToken_);	
						
						// Let future calls know we've already fetched the access token
						loggedIn = true;
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}  else {
				// Add the access_token that we previously obtained to this request as 
				// the "Authorization" header.
				request.addHeader("Authorization", "Bearer " + accessToken_ );
			}
		}
	}
}
