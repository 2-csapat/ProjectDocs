package hu.elte.bankapp.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityHelper {

    public static boolean isAdmin() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAuthorities().contains(Role.ROLE_ADMIN);
    }

    public static String getUsername() {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();

    }

}