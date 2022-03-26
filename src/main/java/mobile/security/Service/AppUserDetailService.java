package mobile.security.Service;

import mobile.model.Entity.User;
import mobile.repository.UserRepository;
import mobile.security.DTO.AppUserDetail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserDetailService implements UserDetailsService {
    private static final Logger LOGGER = LogManager.getLogger(AppUserDetailService.class);
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userEntity = userRepository.findByUsername(username);
        if(userEntity.isEmpty())
        {
            throw new UsernameNotFoundException("User not found");
        }
        LOGGER.info(userEntity.get().getEmail());
        return AppUserDetail.build(userEntity.get());
    }
}
