package com.github.iweinzierl.moviedatabase.search.config.security;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    @Value("${authentication.users.file.path}")
    private String userFilePath;

    private Map<String, UserDetails> userDetails;

    @PostConstruct
    public void init() throws FileNotFoundException {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<UserDetailsImpl>>() {
        }.getType();

        List<UserDetailsImpl> userDetails = gson.fromJson(new FileReader(userFilePath), listType);
        LOG.info("Found {} users in security configuration", userDetails.size());

        Map<String, UserDetails> userDetailsMap = new HashMap<>();

        for (UserDetailsImpl userDetail : userDetails) {
            userDetailsMap.put(userDetail.getUsername(), userDetail);
        }

        setUserDetails(userDetailsMap);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserDetails().get(username);
    }

    private synchronized void setUserDetails(Map<String, UserDetails> userDetails) {
        this.userDetails = userDetails;
    }

    private synchronized Map<String, UserDetails> getUserDetails() {
        return userDetails;
    }
}
