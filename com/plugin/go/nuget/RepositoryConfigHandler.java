package plugin.go.nuget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositoryConfigHandler extends PluginConfigHandler {

    private ConnectionHandler connectionHandler;

    public RepositoryConfigHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public Map handleConfiguration() {
        Map repositoryConfig = new HashMap();

        repositoryConfig.put("REPOSITORY_URL", createConfigurationField("Repository Url", "0", false, true, true));
        repositoryConfig.put("USERNAME", createConfigurationField("Username", "1", false, false, false));
        repositoryConfig.put("PASSWORD", createConfigurationField("Password", "2", true, false, false));

        return repositoryConfig;
    }

    public List handleValidateConfiguration(Map request) {
        List validationList = new ArrayList();

        Map configMap = (Map) request.get("repository-configuration");
        Map urlMap = (Map) configMap.get("REPOSITORY_URL");

        if (urlMap.get("value").equals("")) {
            Map errors = new HashMap();
            errors.put("key", "REPOSITORY_URL");
            errors.put("message", "Url cannot be empty");
            validationList.add(errors);
        }

        return validationList;
    }

    public Map handleCheckRepositoryConnection(Map request) {
        Map configMap = (Map) request.get("repository-configuration");

        String repoUrl = parseValueFromEmbeddedMap(configMap, "REPOSITORY_URL");
        String username = parseValueFromEmbeddedMap(configMap, "USERNAME");
        String password = parseValueFromEmbeddedMap(configMap, "PASSWORD");

        return connectionHandler.checkConnectionToUrlWithMetadata(repoUrl, username, password);
    }

    private String parseValueFromEmbeddedMap(Map configMap, String fieldName) {
        Map fieldMap = (Map) configMap.get(fieldName);
        String value = (String) fieldMap.get("value");
        return value;
    }


}
