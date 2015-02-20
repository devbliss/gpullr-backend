package com.devbliss.gpullr.controller;

import com.devbliss.gpullr.domain.Pullrequest;
import com.devbliss.gpullr.domain.Pullrequest.State;
import com.devbliss.gpullr.domain.PullrequestEvent;
import com.devbliss.gpullr.domain.PullrequestEvent.Type;
import com.devbliss.gpullr.domain.Repo;
import com.devbliss.gpullr.service.PullrequestService;
import com.devbliss.gpullr.service.RepoService;
import com.devbliss.gpullr.service.github.GithubApi;
import com.devbliss.gpullr.service.github.GithubEventsResponse;
import com.devbliss.gpullr.util.Log;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * Fetches pull requests for all our repositories. Must be started once using {@link #startFetchEventsLoop()}.
 * Afterwards, it independently polls periodically again according to the poll interval returned by GitHub.
 *
 * @author Henning Schütz <henning.schuetz@devbliss.com>
 */
@Component
public class GithubEventFetcher {

  @Log
  private Logger logger;

  @Autowired
  private GithubApi githubApi;

  @Autowired
  private RepoService repoService;

  @Autowired
  private PullrequestService pullrequestService;

  private ThreadPoolTaskScheduler executor;

  public GithubEventFetcher() {
    executor = new ThreadPoolTaskScheduler();
    executor.initialize();
  }

  public void startFetchEventsLoop() {

    logger.info("Start fetching events from GitHub...");

    for (Repo repo : repoService.findAll()) {
      logger.debug("Fetch events for repo: " + repo.name);
      fetchEvents(repo, Optional.empty());
    }

    logger.info("Finished fetching events from GitHub.");
  }

  private void handleEventsResponse(GithubEventsResponse response, Repo repo) {
    response.pullrequestEvents.forEach(this::handlePullrequestEvent);
    logger.info("Fetched " + response.pullrequestEvents.size() + " new PRs for " + repo.name);
    Date start = Date.from(Instant.now().plusSeconds(response.nextRequestAfterSeconds));
    executor.schedule(() -> fetchEvents(repo, response.etagHeader), start);
  }

  private void fetchEvents(Repo repo, Optional<String> etagHeader) {
    handleEventsResponse(githubApi.fetchAllEvents(repo, etagHeader), repo);
  }

  private void handlePullrequestEvent(PullrequestEvent event) {
    Pullrequest pullRequest = event.pullrequest;

    if (event.type == Type.PULLREQUEST_CREATED) {
      pullRequest.state = State.OPEN;
    } else {
      pullRequest.state = State.CLOSED;
    }
    pullrequestService.insertOrUpdate(pullRequest);
  }
}
