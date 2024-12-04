//package individual.business.implementation;
//
//import individual.business.RegisterUserUseCase;
//import individual.domain.User.CreateOAuthRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//
//@Service
//@RequiredArgsConstructor
//public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//
//    private final RegisterUserUseCase registerUserUseCase;
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
//
//        String email = oAuth2User.getAttribute("email");
//        String name = oAuth2User.getAttribute("name");
//        String picture = oAuth2User.getAttribute("picture");
//        String providerId = oAuth2User.getAttribute("sub");
//
//        CreateOAuthRequest request = CreateOAuthRequest.builder()
//                .email(email)
//                .name(name)
//                .profilePicture(picture)
//                .providerId(providerId)
//                .build();
//
//        registerUserUseCase.processGoogleLogin("string");
//
//        return new DefaultOAuth2User(
//                Collections.singleton(new SimpleGrantedAuthority("ROLE_CUSTOMER")),
//                oAuth2User.getAttributes(),
//                "email"
//        );
//    }
//}
