package mobile.security.DTO;
import java.util.*;

import mobile.common.AppUserRole;
import mobile.model.Entity.Role;
import mobile.model.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
public class AppUserDetail implements UserDetails {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private ObjectId id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private Collection<? extends GrantedAuthority> authorities;
    private Collection<String> roles;
    private Boolean enable;
    public AppUserDetail(ObjectId id, String username, String email, String password,
                         Collection<? extends GrantedAuthority> authorities,Collection<String> roles,Boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.roles = roles;
        this.enable = active;
    }
    public static AppUserDetail build(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        Set<String> roleNames = new HashSet<>();

        for(Role role : user.getRoles()){
            roleNames.add(role.getName());
            for(AppUserRole item : AppUserRole.values()){
                if(role.getName().equals(item.name())){
                    authorities.addAll(item.getGrantedAuthorities());
                }
            }
        }


        return new AppUserDetail(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                roleNames,
                user.getActive());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    public ObjectId getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return enable;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AppUserDetail user = (AppUserDetail) o;
        return Objects.equals(id, user.id);
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }
}