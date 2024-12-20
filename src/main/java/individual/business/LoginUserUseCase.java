package individual.business;

import individual.domain.User.UserLoginRequest;

public interface LoginUserUseCase {
    String login(UserLoginRequest request);
}
