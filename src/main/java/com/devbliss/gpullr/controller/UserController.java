package com.devbliss.gpullr.controller;

import com.devbliss.gpullr.controller.dto.UserConverter;
import com.devbliss.gpullr.controller.dto.UserDto;
import com.devbliss.gpullr.domain.User;
import com.devbliss.gpullr.service.OAuthService;
import com.devbliss.gpullr.service.UserService;
import com.devbliss.gpullr.service.dto.GithubOauthAccessToken;
import com.devbliss.gpullr.service.dto.GithubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller to manage users.
 *
 * @author Henning Schütz <henning.schuetz@devbliss.com>
 */
@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserConverter userConverter;

  @Autowired
  private OAuthService oAuthService;

  @RequestMapping(method = RequestMethod.GET)
  public List<UserDto> getAllOrgaMembers() {
    return userService
        .findAllOrgaMembers()
        .stream()
        .map(userConverter::toDto)
        .collect(Collectors.toList());
  }

  @RequestMapping(value = "/login/{id}", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public void login(@PathVariable("id") int id) {
    userService.login(id);
  }

  @RequestMapping(value = "/oauth/callback", method = RequestMethod.GET)
  public void oauthCallback(HttpServletResponse httpServletResponse, @RequestParam("code") String code,
      @RequestParam("state") String state) throws IOException {
    System.out.println("CODE: " + code);
    System.out.println("STATE: " + state);

    // TODO: oAuthService.validateState(state);
    final GithubOauthAccessToken oauthAccessToken = oAuthService.getAccessToken(code);
    final GithubUser githubUser = oAuthService.getUserByAccessToken(oauthAccessToken);

    userService.login(githubUser.id);

    final User currentUser = userService.getCurrentUserIfLoggedIn().get();
    currentUser.accessToken = oauthAccessToken.access_token;
    userService.insertOrUpdate(currentUser);

    httpServletResponse.sendRedirect("/");
  }

  @RequestMapping(
      value = "/me",
      method = RequestMethod.GET)
  public UserDto whoAmI() {
    User entity = userService.whoAmI();
    return userConverter.toDto(entity);
  }

}
