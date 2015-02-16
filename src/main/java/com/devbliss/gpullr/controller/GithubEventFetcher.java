package com.devbliss.gpullr.controller;

import java.util.Date;

import java.time.Instant;
import com.devbliss.gpullr.domain.Repo;
import com.devbliss.gpullr.service.PullrequestService;
import com.devbliss.gpullr.service.RepoService;
import com.devbliss.gpullr.service.github.GithubApi;
import com.devbliss.gpullr.service.github.GithubEventsResponse;
import com.devbliss.gpullr.util.Log;
import java.util.Optional;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * Periodically fetches 
 * 
 * @author Henning Schütz <henning.schuetz@devbliss.com>
 *
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
    response.pullrequestEvents.forEach(ev -> pullrequestService.insertOrUpdate(ev.pullrequest));
    logger.info("Fetched " + response.pullrequestEvents.size() + " new PRs for " + repo.name);
    Date start = Date.from(Instant.now().plusSeconds(response.nextRequestAfterSeconds));
    executor.schedule(() -> fetchEvents(repo, Optional.of(response.etagHeader)), start);
  }

  private void fetchEvents(Repo repo, Optional<String> etagHeader) {
    handleEventsResponse(githubApi.fetchAllEvents(repo, etagHeader), repo);
  }

}
