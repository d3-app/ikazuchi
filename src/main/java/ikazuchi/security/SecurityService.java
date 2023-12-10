package ikazuchi.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.vaadin.flow.spring.security.AuthenticationContext;

@Component
public class SecurityService {

  private final AuthenticationContext authenticationContext;

  public SecurityService(AuthenticationContext authenticationContext) {
    this.authenticationContext = authenticationContext;
  }

  public UserDetails getAuthenticatedUser() {
    return authenticationContext.getAuthenticatedUser(UserDetails.class).get();
  }

  public CustomUser getCustomUser() {
    return authenticationContext.getAuthenticatedUser(CustomUser.class).get();
  }

  public void logout() {
    authenticationContext.logout();
  }
  
  public boolean hasRole(String roleName) {
    return getCustomUser().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(roleName));
  }
}
