/*
 * Copyright 2017 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package plugin.go.nuget.unit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import plugin.go.nuget.ConnectionHandler;
import plugin.go.nuget.RepositoryConfigHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static utils.Constants.REPOSITORY_CONFIGURATION;

public class RepositoryConfigHandlerTest {
    RepositoryConfigHandler repositoryConfigHandler;
    ConnectionHandler connectionHandler;

    @Before
    public void setUp() {
        connectionHandler = mock(ConnectionHandler.class);
        repositoryConfigHandler = new RepositoryConfigHandler(connectionHandler);
    }

    @Test
    public void shouldErrorWhenInvalidRepositoryConfiguration() {
        Map invalidBody = createRequestBodyWithCompleteMetadata("", "", "");

        List errorList = repositoryConfigHandler.handleValidateConfiguration(invalidBody);

        Assert.assertFalse(errorList.isEmpty());
    }

    @Test
    public void shouldErrorOutWhenRepoUrlIsNull() {
        Map invalidBody = createRequestBodyWithCompleteMetadata(null, "", "");

        List errorList = repositoryConfigHandler.handleValidateConfiguration(invalidBody);

        Assert.assertFalse(errorList.isEmpty());
    }

    @Test
    public void shouldReturnEmptyErrorListWhenValidRepositoryConfigurations() {
        Map validBody = createRequestBodyWithCompleteMetadata("http://testsite.com", "", "");

        List errorList = repositoryConfigHandler.handleValidateConfiguration(validBody);

        Assert.assertTrue(errorList.isEmpty());
    }

    @Test
    public void shouldUseConnectionHandlerToCheckConnectionWithMetadata() {
        String SOME_URL = "http://www.nuget.com/";
        String SOME_USERNAME = "SomeUsername";
        String SOME_PASSWORD = "somePassword";

        repositoryConfigHandler.handleCheckRepositoryConnection(createRequestBodyWithCompleteMetadata(SOME_URL, SOME_USERNAME, SOME_PASSWORD));

        verify(connectionHandler).checkConnectionToUrlWithMetadata(SOME_URL, SOME_USERNAME, SOME_PASSWORD);
    }

    @Test
    public void shouldHandleCheckConnectionWhenOptionalMetadataIsNotProvided() {
        String SOME_URL = "http://www.nuget.com/";

        repositoryConfigHandler.handleCheckRepositoryConnection(createUrlRequestBody(SOME_URL));

        verify(connectionHandler).checkConnectionToUrlWithMetadata(SOME_URL, null, null);
    }

    private Map createUrlRequestBody(String url) {
        Map urlMap = new HashMap();
        urlMap.put("value", url);
        Map fieldsMap = new HashMap();
        fieldsMap.put("REPO_URL", urlMap);
        Map bodyMap = new HashMap();
        bodyMap.put(REPOSITORY_CONFIGURATION, fieldsMap);
        return bodyMap;
    }

    private Map createRequestBodyWithCompleteMetadata(String url, String username, String password) {
        Map urlMap = new HashMap();
        urlMap.put("value", url);
        Map fieldsMap = new HashMap();
        fieldsMap.put("REPO_URL", urlMap);
        Map usernameMap = new HashMap();
        usernameMap.put("value", username);
        fieldsMap.put("USERNAME", usernameMap);
        Map passwordMap = new HashMap();
        passwordMap.put("value", password);
        fieldsMap.put("PASSWORD", passwordMap);
        Map bodyMap = new HashMap();
        bodyMap.put(REPOSITORY_CONFIGURATION, fieldsMap);
        return bodyMap;
    }


}