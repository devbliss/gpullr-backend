package com.devbliss.gpullr.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.devbliss.gpullr.Application;
import com.devbliss.gpullr.domain.PullRequest;
import com.devbliss.gpullr.domain.PullRequest.State;
import com.devbliss.gpullr.domain.Repo;
import com.devbliss.gpullr.domain.User;
import com.devbliss.gpullr.domain.UserSettings;
import com.devbliss.gpullr.exception.NotFoundException;
import com.devbliss.gpullr.repository.PullRequestRepository;
import com.devbliss.gpullr.repository.RepoRepository;
import com.devbliss.gpullr.repository.UserRepository;
import com.devbliss.gpullr.service.github.GithubApi;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Tests for {@link PullRequestService}
 *
 * @author Philipp Karstedt <philipp.karstedt@devbliss.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class PullRequestServiceTest {

  private static final int REPO_ID = 1000;

  private static final String REPO_NAME = "pr_test_repo";

  private static final String REPO_DESC = "pr_test_repo_description";

  private static final String USER_NAME = "pr_test_user";

  private static final String USER_NAME_2 = USER_NAME + "_2";

  private static final String AVATAR = "0815.jpg";

  private static final String AVATAR_2 = "what.ever.jpg";

  private static final int USER_ID = 1000;

  private static final int PR_ID = 1;
  
  private static final int OLD_PR_ID = 2;

  @Autowired
  private PullRequestRepository prRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RepoRepository repoRepository;

  @Autowired
  private UserService userService;

  private GithubApi githubApi;

  private PullRequestService prService;

  private PullRequest testPr;

  @Before
  public void setup() {
    githubApi = mock(GithubApi.class);
    prService = new PullRequestService(prRepository, userRepository, githubApi, userService);
    testPr = new PullRequest();
    testPr.id = PR_ID;
    testPr.author = initUser();
    testPr.repo = initRepo();
    testPr.state = PullRequest.State.OPEN;
    testPr.createdAt = ZonedDateTime.now();
  }

  @After
  public void teardown() {
    prRepository.deleteAll();
    repoRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void insertOrUpdatePullRequest() {
    // first of all check that no pullRequest exists
    List<PullRequest> prs = prService.findAll();
    assertEquals(0, prs.size());

    prService.insertOrUpdate(testPr);
    prs = prService.findAll();
    assertEquals(1, prs.size());
    int fetchedPrId = prs.get(0).id;
    int fetchedPrUserId = prs.get(0).author.id;
    int fetchedPrRepoId = prs.get(0).repo.id;
    State fetchedState = prs.get(0).state;

    assertEquals(PR_ID, fetchedPrId);
    assertEquals(USER_ID, fetchedPrUserId);
    assertEquals(REPO_ID, fetchedPrRepoId);
    assertEquals(testPr.state, fetchedState);
  }

  @Test
  public void findAllOpenPullRequests() {
    User user = initUser();
    userService.login(user.id);

    // store a pullRequest with state OPEN:
    prService.insertOrUpdate(testPr);

    // store another with state CLOSED:
    PullRequest pullRequest = new PullRequest();
    pullRequest.id = PR_ID + 1;
    pullRequest.repo = testPr.repo;
    pullRequest.author = testPr.author;
    pullRequest.state = State.CLOSED;
    pullRequest.createdAt = ZonedDateTime.now();
    prService.insertOrUpdate(pullRequest);

    // make sure only the open PR is returned:
    List<PullRequest> openPrs = prService.findAllOpen();
    assertEquals(1, openPrs.size());
    assertEquals(State.OPEN, openPrs.get(0).state);
    assertEquals(PR_ID, openPrs.get(0).id.intValue());
  }

  @Test
  public void findAllOpenPullRequestsRegardsUserOrderSettings() {
    User user = initUser();
    userService.login(user.id);

    user.userSettings = new UserSettings(UserSettings.OrderOption.DESC);
    userService.insertOrUpdate(user);

    // create pull request:
    prService.insertOrUpdate(testPr);

    // create second pull request that is OLDER:
    testPr.id = OLD_PR_ID;
    testPr.url = testPr.url + "_2";
    testPr.createdAt = testPr.createdAt.minus(1, ChronoUnit.HOURS);
    prService.insertOrUpdate(testPr);

    // since user has no user preferences yet, the newer pull request should be first in list
    // (default behavior):
    List<PullRequest> allOpen = prService.findAllOpen();
    assertEquals(PR_ID, allOpen.get(0).id.intValue());

    // after storing user preference that user wants pull requests in ascending order, the other one
    // should be first in list:
    user.userSettings.defaultPullRequestListOrdering = UserSettings.OrderOption.ASC;
    userService.updateUserSettings(user.id, user.userSettings);
    userService.updateUserSession(user);
    allOpen = prService.findAllOpen();
    assertEquals(OLD_PR_ID, allOpen.get(0).id.intValue());

    // after changing user preference to descneding order, the order changes again:
    user.userSettings.defaultPullRequestListOrdering = UserSettings.OrderOption.DESC;
    userService.updateUserSettings(user.id, user.userSettings);
    userService.updateUserSession(user);
    allOpen = prService.findAllOpen();
    assertEquals(PR_ID, allOpen.get(0).id.intValue());
  }

  @Test
  public void assignPullRequest() {
    // create new PR w/o owner:
    PullRequest pullRequest = new PullRequest();
    pullRequest.id = PR_ID + 1;
    pullRequest.repo = testPr.repo;
    pullRequest.state = State.OPEN;
    pullRequest.author = testPr.author;
    pullRequest.createdAt = ZonedDateTime.now();
    prService.insertOrUpdate(pullRequest);

    // assign to an existing user:
    User assignee = new User(USER_ID + 1, USER_NAME_2, AVATAR_2);
    userRepository.save(assignee);
    prService.assignPullRequest(assignee, pullRequest.id);

    // verify GitHub-API is called:
    verify(githubApi).assignUserToPullRequest(assignee, pullRequest);

    // verify the assignee is stored in our database as well:
    assertEquals(assignee, prService.findById(pullRequest.id).get().assignee);
  }

  @Test(expected = NotFoundException.class)
  public void assigningPullRequestToUnknownUserFails() {
    // create new PR w/o owner:
    PullRequest pullRequest = new PullRequest();
    pullRequest.id = PR_ID + 1;
    pullRequest.repo = testPr.repo;
    pullRequest.state = State.OPEN;
    pullRequest.author = testPr.author;
    pullRequest.createdAt = ZonedDateTime.now();
    prService.insertOrUpdate(pullRequest);

    // assign to a non existing user:
    User assignee = new User(USER_ID + 1, USER_NAME_2, AVATAR_2);
    prService.assignPullRequest(assignee, pullRequest.id);
  }

  @Test
  public void findById() {
    // create a pull request:
    PullRequest pullRequest = new PullRequest();
    pullRequest.id = PR_ID + 1;
    pullRequest.repo = testPr.repo;
    pullRequest.state = State.CLOSED;
    pullRequest.author = testPr.author;
    pullRequest.createdAt = ZonedDateTime.now();
    prService.insertOrUpdate(pullRequest);

    // verify it can be fetched by id:
    Optional<PullRequest> fetched = prService.findById(pullRequest.id);
    assertTrue(fetched.isPresent());
    assertEquals(pullRequest, fetched.get());
  }

  private Repo initRepo() {
    Repo repo = new Repo(REPO_ID, REPO_NAME, REPO_DESC);
    return repoRepository.save(repo);
  }

  private User initUser() {
    User prOwner = new User(USER_ID, USER_NAME, AVATAR);
    return userRepository.save(prOwner);
  }
}
