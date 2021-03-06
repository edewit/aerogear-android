/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.android.authentication;

import java.net.URI;
import java.net.URL;
import java.util.Map;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.http.HeaderAndBody;
import org.jboss.aerogear.android.http.HttpException;
import org.jboss.aerogear.android.http.HttpProvider;
import org.jboss.aerogear.android.pipeline.Pipe;

/**
 * A module which can authenticate a user. It also provides the necessary tools
 * to log a user in, out, and modify requests from a {@link Pipe} so they are
 * seen as authenticated.
 */
public interface AuthenticationModule {

    public URL getBaseURL();

    public String getLoginEndpoint();

    public String getLogoutEndpoint();

    public String getEnrollEndpoint();

    /**
     * Will try to register a user with a service using userData.
     * <p/>
     * It will call the callbacks onSuccess with a parameter of a Map of the
     * values returned by the enroll service or onFailure if there is an error
     * 
     * @param userData
     * @param callback
     */
    public void enroll(Map<String, String> userData,
            Callback<HeaderAndBody> callback);

    /**
     * Will try to log in a user using username and password.
     * <p/>
     * It will call the callbacks onSuccess with a parameter of a Map of the
     * values returned by the enroll service or onFailure if there is an error
     * 
     * @param username
     * @param password
     * @param callback
     */
    public void login(String username, String password,
            Callback<HeaderAndBody> callback);

    /**
     * Performs a logout of the current user.
     * <p/>
     * It will call callback.onSuccess with no value on success and
     * callback.onFailure if there is an error.
     * 
     * @param callback
     */
    public void logout(Callback<Void> callback);

    /**
     * @return true if the module has been logged in successfully
     */
    public boolean isLoggedIn();

    /**
     * This method is called be {@link Pipe} implementations when they need
     * security applied to their {@link HttpProvider}. The headers/data/query
     * parameters returned should be applied to the Url and HttpProvider
     * directly before a call.
     * 
     * @return the current AuthorizationFields for security
     *
     * @deprecated This method has been superseded by {@link  AuthenticationModule#getAuthorizationFields(java.net.URI, String, byte[]) }
     * 
     */
    @Deprecated
    public AuthorizationFields getAuthorizationFields();

    /**
     * This method is called be {@link Pipe} implementations when they need
     * security applied to their {@link HttpProvider}. The headers/data/query
     * parameters returned should be applied to the Url and HttpProvider
     * directly before a call.
     * 
     * @param requestUri the Request-Line URI.
     * @param method the HTTP method being used
     * @param requestBody the body of the request.  This method promises to not 
     * modify the body.
     * 
     * @return the current AuthorizationFields for security
     * 
     */
    public AuthorizationFields getAuthorizationFields(URI requestUri, String method, byte[] requestBody);

    
    /**
     * Some authorization schemes (HTTP Digest, OAUTH) have a mechanism for retrying 
     * a login to fetch fresh credentials after the credentials expire.
     * 
     * In the event of a 401 error, this method should retry the login.  If the 
     * system does not support retrying or if the retry fails then it will return false.
     * 
     * Otherwise it returns true and refreshes the various credentials.
     * 
     * This method is blocking and it should NOT be called by the user directly.
     * 
     * @return whether or not retrying the login succeeded.
     */
    public boolean retryLogin() throws HttpException;
    
}
