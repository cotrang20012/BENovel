package mobile.common;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;


public enum AppUserRole {
    USER(Sets.newHashSet(UserPermission.USER_READ, UserPermission.USER_WRITE)),
    ADMIN(Sets.newHashSet(UserPermission.ADMIN_READ, UserPermission.ADMIN_WRITE, UserPermission.USER_READ, UserPermission.USER_WRITE));

    private final Set<UserPermission> permissions;

    AppUserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        //permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}