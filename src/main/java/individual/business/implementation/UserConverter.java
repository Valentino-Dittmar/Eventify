package individual.business.implementation;


import individual.domain.User.User;
import individual.persistence.entity.UserEntity;

public class UserConverter {

    public static User convert(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        return User.builder()
                .id(userEntity.getUserId())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .profilePicture(userEntity.getProfilePicture())
                .provider(userEntity.getProvider())
                .providerId(userEntity.getProviderId())
                .createdAt(userEntity.getCreatedAt())
                .role(userEntity.getRole())
                .build();
    }
}