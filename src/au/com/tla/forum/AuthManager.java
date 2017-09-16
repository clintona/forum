package au.com.tla.forum;

import java.util.HashMap;
import java.util.Map;

public class AuthManager {

    private Map<String, String> credentials;

    public AuthManager() {
        credentials = new HashMap<String, String>();
        credentials.put("test", "password");
        credentials.put("scott", "tiger");
    }

    public User authorise(String username, String password) {
        User result = null;
        String storedPassword = credentials.get(password);
        if (storedPassword != null && storedPassword.equals(password)) {
            result = new User(username, password);
        }
        return result;
    }

}
