package com.devbliss.gpullr.controller;

import com.devbliss.gpullr.domain.Repo;
import com.devbliss.gpullr.service.RepoService;
import com.devbliss.gpullr.service.github.GithubApi;
import com.devbliss.gpullr.util.Log;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Since the events endpoint of GitHub does *not* deliver the event that a new repository has been created
 * (unlike the documentation tells), this class fetches the full list of repositories and store
 * changes in our persistence layer.
 *
 * @author Henning Schütz <henning.schuetz@devbliss.com>
 */
@Component
public class GithubReposFetcher {

  @Log
  private Logger logger;

  @Autowired
  private GithubApi githubApi;

  @Autowired
  private RepoService repoService;

  public void fetchRepos() {
    logger.info("Fetching repos from GitHub...");

//    List<Repo> repos = githubApi.fetchAllGithubRepos();
    List<Repo> repos = temporaryRepoList();

    repos.forEach(r -> {
          logger.info(String.format("fetched repo: [%d][%s]", r.id, r.name));
          repoService.insertOrUpdate(r);
        }
    );

    logger.info("Finished fetching repos from GitHub.");
  }

  private List<Repo> temporaryRepoList() {
    return Arrays.asList(
        new Repo(10028272, "blissdoc-doc-builder", "Std. DocBuilder Plugins for blissdoc."),
        new Repo(10053706, "konvektomat", "Debian Package Repository Server"),
        new Repo(10053874, "konvektor", "Konvektomat Repository Server API Client"),
        new Repo(10102187, "template.responsive", "phpDocumentor templates for blissdoc"),
        new Repo(10103215, "docopt", "Pythonic command line arguments parser, that will make you smile"),
        new Repo(10191564, "debifier", "Take external stuff, and package it."),
        new Repo(10332361, "konduktor", "Devbliss Authorisation Services"),
        new Repo(10404103, "import2cis", "This service fetches new deliveries from HGV and uploads the to CIS (laika)"),
        new Repo(11469875, "upstream", "Upstream Packages"),
        new Repo(11656363, "macports", "Port files for devbliss tools"),
        new Repo(11680256, "jura-backend", "Backend for jura (symfony project)"),
        new Repo(11739481, "manuals", "Manuals and Standards"),
        new Repo(11926285, "bingo", "Operating system level virtualization and provisioning"),
        new Repo(11951207, "arran-math", "Bundle for mathematical functions of Arran."),
        new Repo(12247665, "developer_notebooks", "Repository where developers can put on their intern notes for" +
            " each project."),
        new Repo(12455076, "jura-frontend-selenium", "Selenium Tests for the JURA Frontend and the Showmaster"),
        new Repo(12611543, "scripts", "Useful bash-scripts like git-devbliss finish/release hook and similar stuff"),
        new Repo(12731461, "serial", "Run processes one instance at a time"),
        new Repo(12753678, "Interface_ProductContent", "JURA compatible ProductContent for all Interface Levels"),
        new Repo(12753809, "AllElements_ProductContent", "Jura compatible ProductContent for AllElements TestProject"),
        new Repo(12754016, "JuraProductHierarchy", "Product Hierarchy .xsd for Project Jura"),
        new Repo(12891475, "errno", "[ON HOLD] Enormous Register of Records, Notifications, and Other stuff"),
        new Repo(13093217, "Beyond_ProductContent", "ProductContent for Beyond"),
        new Repo(13093231, "Mind_ProductContent_US", "ProductContent for Mind"),
        new Repo(13093279, "InCompany_ProductContent", "ProductContent for In Company"),
        new Repo(13267264, "bingo-local", "Vagrant environment for running a Bingo host on OS X"),
        new Repo(13268129, "swift", "Swift is a web application for creating, managing and publishing digital " +
            "learning content."),
        new Repo(13556552, "islay", "An Isle near Jura a.k.a. the Activity-Player."),
        new Repo(13646721, "jura-vagrant", "OUTDATED!!! development environment for jura project"),
        new Repo(13734835, "qm-backup", "Backup for arran-qm-backend. Can be deleted after handover."),
        new Repo(13734908, "ae-backup", "Backup for arran-ae-backend. Can be deleted after handover."),
        new Repo(14232628, "Mind_ProductContent_UK", "Mind ProductContent (UK version)"),
        new Repo(14360685, "juanitor-4j", ""),
        new Repo(14496237, "juanitor-demo", "Juanitor Installation for Digital Science and Education"),
        new Repo(14586283, "swift_ec2", "The Swift Fabric and Puppet Scripts for provisioning the EC2 Environments."),
        new Repo(14717262, "reprise", "Reprise is an easy to use Debian repository and package management tool. " +
            "With Reprise you can create, synchronize and delete your package repository and easily add packages to " +
            "a repository."),
        new Repo(15871569, "mongojack-testproject", ""),
        new Repo(16314220, "Jura-HtmlUnit", "Simple automated click tests for the Jura live platform"),
        new Repo(16319271, "checkstyle-checks", "Custom checks for the maven-checkstyle-plugin."),
        new Repo(16373881, "jura-activity-answer-types", "Documentation for the answer types in activities that " +
            "jura supports. "),
        new Repo(17216372, "errai", "Errai"),
        new Repo(17431622, "Gateway_ProductContent_Plus", "ProductContent for the \"Gateway Plus\" product"),
        new Repo(17431707, "Test_ProductContent", "Product Content Repository for a Test Product to provide testing " +
            "data for development"),
        new Repo(17517516, "swift_git_hooks", "A Repository to manage the git hooks used in the swift project"),
        new Repo(17897129, "gulliver-doc", "General documentation about the Gulliver project. Reading this doc will" +
            " help you understand what are the requirements of the project and get into special specification" +
            " points."),
        new Repo(18807316, "gulliver-backend", "Gulliver learning platform as part of the ecosystem."),
        new Repo(18831346, "bingo-workshop", "Example files for the Bingo Workshop (16. Apr. 2014)"),
        new Repo(18832687, "gulliver-frontend-angularjs", "Deprecated: One possible frontend for the gulliver project" +
            " built with AngularJs"),
        new Repo(19061616, "gulliver-provisioning", "Gulliver server orchestration, virtualization and provisioning"),
        new Repo(19071260, "puppet_component_modules", "Puppet modules with basic components"),
        new Repo(19071310, "puppet_profiles", "Contains puppet profiles"),
        new Repo(19276695, "gulliver-cds", "The Content Distribution Service is able to consume products from the" +
            " Swift project and provides this to Gulliver instances."),
        new Repo(19453054, "jura-deployment", "Jura Deployment with Bingo"),
        new Repo(19932531, "jura-appdynamics", "Debian packages for the App-Dynamics bins."),
        new Repo(20015279, "jobs", "Be part of the mission"),
        new Repo(20052914, "workshops", "Documentation of the devbliss internal workshops"),
        new Repo(20055004, "HiFive_ProductContent", "ProductContent for HiFive"),
        new Repo(20064055, "jura_static_pages", "Contains static pages like FAQ, Help, etc."),
        new Repo(20184815, "gulliver-frontend", "Frontend for the Gulliver project built with Errai"),
        new Repo(20259455, "Beyond_ProductTESTContent_TEST_ACDC", ""),
        new Repo(20395966, "acd", "Jura Automated Content Deployment"),
        new Repo(20436749, "balnibarbi", "Internal Repository of the incredible team Balnibarbi"),
        new Repo(20802808, "git-workshop", ""),
        new Repo(20912079, "gulliver-vagrant", "Virtual machine for gulliver local development."),
        new Repo(21033641, "jura-loadtest", "lua scripts for loadtesting"),
        new Repo(21276573, "GoBeyond_ProductContent", "ProductContent for the \"GoBeyond\" product"),
        new Repo(21276611, "Gateway_ProductContent", "ProductContent for the \"Gateway\" product"),
        new Repo(21644280, "QTI-Tools", "A GWT player for QTI documents"),
        new Repo(21739861, "qtitest", "Gulliver QTI research "),
        new Repo(21894702, "qtiworks", "IMS QTI 2.1 assessment delivery engine and Java development library (JQTI+)." +
            " Supports the MathAssess extensions. Replacement for QTIEngine/JQTI and MathAssessEngine/JQTI."),
        new Repo(22209502, "jura-backend-silex", "JURA Backend with Silex Framework"),
        new Repo(22384028, "mongofill", "Pure PHP implementation of MongoDB driver, with aim to be a drop-in " +
            "replacement of the official extension, usable under HHVM runtime."),
        new Repo(22470670, "interview-material", "Codesnippets and other stuff usefull for an interview"),
        new Repo(22643531, "gulliver-lmsproxy", "Service for Assessment of User (SAU) in the Ecosystem."),
        new Repo(22678448, "jura-sla", "Jura performance monitoring and reporting"),
        new Repo(23221236, "gradliss-gradle-plugin", "Devbliss Gradle plugins"),
        new Repo(23354635, "hackhaton", "Devbliss repo for hackhathon projects"),
        new Repo(23611339, "VanDAM", "Vantastic Digital Asset Management Service is part of Macmillan ecosystem " +
            "for authoring, managing and publishing digital learning contents. It provides a REST API for managing" +
            " assets and is based on JCR and Spring Boot."),
        new Repo(24105188, "dementity", "entity management service for macmillan ecosystem"),
        new Repo(24135745, "dementity-doctests", ""),
        new Repo(24333461, "salt-states", "SaltStack recipies"),
        new Repo(24404685, "doctest-gradle-plugin", "Gradle Plugin to run doctest"),
        new Repo(24451609, "changelog-gradle-plugin", "A plugin for the gradle build system to write changelogs" +
            " painless"),
        new Repo(24454813, "doms", "Digital Online Messaging Service"),
        new Repo(24489862, "jums", "a poc for a simple message service for JURA written in the golang"),
        new Repo(24504902, "ecosystem", "Global information about the ecosystem project."),
        new Repo(24714950, "sms", "A POC of a simple message service for JURA based on Finatra and elasticsearch" +
            " written in Scala"),
        new Repo(24937563, "y2y", "Message Service for JURA"),
        new Repo(24938319, "jura-sms-sailsJs", ""),
        new Repo(24948847, "jsms", ""),
        new Repo(24976871, "mkdocs", "Project documentation with Markdown."),
        new Repo(24989258, "docbliss", "doc tool for devbliss"),
        new Repo(25194852, "finagle-ecosytem-cluster", ""),
        new Repo(25204532, "netflix-ecosystem-cluster", ""),
        new Repo(25210037, "vertx-prototype-ecosystem", "Prototyp for a service orchestration \"task - layer\"" +
            " implemented with vert.x "),
        new Repo(25586222, "bingo-salt", "salt for bingo"),
        new Repo(25644941, "osw-devbliss", "Website for devbliss open source projects"),
        new Repo(25867011, "team_dashboard", "Dashboard for teams. With an overview for upcoming meetings," +
            " codecoverage, burndown chart and more."),
        new Repo(25912916, "bingo-puppet", "puppet files for bingo"),
        new Repo(25963895, "homepage_devbliss", "the refactoring of our homepage"),
        new Repo(26227869, "freebsd", "FreeBSD stuff"),
        new Repo(26278503, "eureka-cluster", "Playground for a Netflix cluster coordinated by an Eureka server"),
        new Repo(26316180, "salt", "Salt for Operations "),
        new Repo(26479501, "ecosystem-host-vm", "Vagrant host VM that runs all docker container of the ecosystem" +
            " services to allow service development without manual starting of depending services."),
        new Repo(26489752, "ecosystem-course", ""),
        new Repo(26491377, "ecosystem-infinispan", "Infinispan server setup for ecosystem cluster."),
        new Repo(26524071, "ecosystem-eureka", "Eureka service registry project"),
        new Repo(26578737, "ecosystem-learning-edge-service", "Learning Edge Service for the Ecosystem"),
        new Repo(26594281, "ecosystem-user-service", ""),
        new Repo(26631667, "ecosystem-learning-and-teaching-frontend", "The frontend for the learning and teaching" +
            " platform based on angular and less"),
        new Repo(26644963, "homepage", "Devbliss Corporate Homepage"),
        new Repo(26764062, "infinispan-eurekaclient", "Wrapper for infinispan announcing itself via eureka"),
        new Repo(26766238, "docker-gradle-plugin", "Gradle Plugin for projects that use docker. It add Tasks to" +
            " handle multiple containers with one task. It will be used for ecosystem to start depending containers" +
            " transitive."),
        new Repo(26806798, "docker-base-images", ""),
        new Repo(26856569, "edison-frontend", "Editorial Frontend"),
        new Repo(26863890, "ecosystem-course-aggregation", "Ecosystem edge service for course management"),
        new Repo(26912866, "ecosystem-zuul", "Zuul reverse proxy app for the ecosystem cluster"),
        new Repo(26917396, "ecosystem-main-frontend", "main include front end module "),
        new Repo(27128704, "pepper-frontend", "preparation frontend"),
        new Repo(27171920, "jura-integration-casper", "for Integration test of Jura project"),
        new Repo(27256910, "jura-integration-selenium", "testing selenium for project jura integration testing"),
        new Repo(27259558, "ecosystem-deployment", "Ecosystem docker deployment."),
        new Repo(27372876, "ecosystem-turbine", "Ecosystem component that gathers backend endpoint information" +
            " and showing it in a graphical dashboard. It runs in its own ecosystem Docker container."),
        new Repo(27539198, "mysql-formula", "Install the MySQL client and/or server"),
        new Repo(28144414, "ecosystem-grunt-plugin", "global ruleset for the ecosystem"),
        new Repo(28232995, "ecosystem_performance", ""),
        new Repo(28339465, "ecosystem-gatling", "Load & Stress tests for the ecosystem application"),
        new Repo(29021679, "modeshape", "ModeShape is a distributed, hierarchical, transactional, and consistent" +
            " data store with support for queries, full-text search, events, versioning, references, and flexible" +
            " and dynamic schemas. It is very fast, highly available, extremely scalable, and it is 100% open source" +
            " and written in Java. Clients use the JSR-283 standard Java API for content repositories (aka, JCR) or" +
            " ModeShape's REST API, and can query content through JDBC and SQL."),
        new Repo(30253123, "gpullr-frontend", "pullrequest administration tool - frontend"),
        new Repo(30253513, "gpullr-backend", "pullrequest administration tool - backend"),
        new Repo(30290705, "QTI-Player", "Devbliss QTI-Player"),
        new Repo(30648335, "gpullrtestrepo", ""),
        new Repo(30650113, "gpullrtestrepo2", ""),
        new Repo(30957499, "spotty", "Spotlight finger food"),
        new Repo(30967650, "conf_notes", "Notes of the conference where Devbliss was present"),
        new Repo(4444047, "coporate_design", "Various devbliss logos"),
        new Repo(4445366, "paulparser", "Paul the Parser - scientific meta tag parser"),
        new Repo(4595809, "GWT-XDM", "Cross Domain Messaging for GWT."),
        new Repo(4750178, "devblissdemoproject", "just a quick proof of concept integrating doctests, docs, buildout," +
            " gwt and backend in a simple maven setup :)"),
        new Repo(4882827, "idpool", "buildout project for idpool (use this to start with the project)"),
        new Repo(4902413, "apitester", "Apitester is an Apache HttpClient wrapper to make the most common tasks for" +
            " testing a REST API more convenient. You can do HTTP Request (GET, POST PUT or DELETE) and handle" +
            " cookies and response object (headers, payload) in an easy way."),
        new Repo(4920616, "doctest", "Doctest helps you to test your REST Api and generates an HTML documentation" +
            " for it. The docs will be only generated if the tests pass so you can be sure that the documentation" +
            " is up to date."),
        new Repo(4986999, "idpool-domainmodels", "shared domainmodels for idpool"),
        new Repo(5002894, "idpool-mailr", "A ruby daemon who subscribes to an AMQP queue and generates emails from" +
            " AMQP messages and ERB templates"),
        new Repo(5162765, "idpool-service-pernod", "Persistent Notification Daemon"),
        new Repo(5259451, "risotto", "RISotto is a lightweight parser for RIS (Research Information System) format" +
            " files."),
        new Repo(5328358, "idpool-united", "Python Library for use in idpool-projects - AMQP-event-system-client," +
            " configuration-mixin, json-schema and -validation"),
        new Repo(5438375, "gwtbliss", "Gwtbliss is a utility library which allows you to easily create your GWT" +
            " project based on GWTP, RestyGwt, Gin and maven and reuse some new features (HTML5 elements, RestyGwt" +
            " callbacks)"),
        new Repo(5781154, "devbliss-vagrant", "Devbliss flavored vagrant basebox and puppet modules"),
        new Repo(5839276, "timesheet-ios", ""),
        new Repo(5853583, "juanitor", "Reverse Proxy / Session Store for SOA"),
        new Repo(5883997, "united", "python library used in different devbliss projects"),
        new Repo(5884811, "scoreboot", "gamification service"),
        new Repo(5885034, "pernod", "realtime notification service"),
        new Repo(5886550, "devtools", "tools for python egg releases"),
        new Repo(5886624, "eggproxy", "host python eggs"),
        new Repo(5886692, "ws", "python library used in projects are part of SOAs"),
        new Repo(5887836, "rating", "rating service (thumb up / down)"),
        new Repo(5950071, "timesheet-backend", ""),
        new Repo(5967700, "timesheet-android", ""),
        new Repo(6257567, "blissdoc", "Documentation System from and for devbliss"),
        new Repo(6260097, "juanitor_dashboard", "Monitoring frontend for juanitor services"),
        new Repo(6351982, "backstage-agent", ""),
        new Repo(6703843, "domainmodels", "Abstract Domain Model generator"),
        new Repo(6777863, "fail", "Implementation of a python testrunner for python doctests."),
        new Repo(6813973, "packaging", "[DEPRECATED] Scripts for full automatic Debian package generation"),
        new Repo(6978618, "pastebin", "Devbliss Pastebin"),
        new Repo(7001372, "laika", "HGV-RCDP"),
        new Repo(7021793, "epubli-autorencockpit", "A Python / JavaScript Statistics Application for epubli" +
            " customers."),
        new Repo(7223834, "gwtlibAndSample", "[Deprecated] gwt sample project using the common lib"),
        new Repo(7255682, "jura-frontend", "[Jura] Frontend of the JURA project. jura-frontend is a GWT project" +
            " based on gwtbliss (maven, GWTP) which communicates with JSON based AJAX-Requests to the server" +
            " jura-backend."),
        new Repo(7423621, "git-devbliss", "Tools and documentation for development workflow, versioning, and" +
            " quality control at devbliss"),
        new Repo(7684107, "rcf", "[Jura] Responsive Content Framework from MacMillan"),
        new Repo(7797308, "service_demo", "Demo for testing: Juanitor, Pernod and Scoreboot"),
        new Repo(7815860, "dotfiles", "configuration files for local development"),
        new Repo(7896381, "vz-gwt-frontend", "[Deprecated] New frontend for VZ networks (copied from github.vz.net)"),
        new Repo(8159219, "meta", "[DEPRECATED] Miscellaneous Documentation"),
        new Repo(8269770, "symfony-bundles", "Devbliss specific Symfony Bundles"),
        new Repo(8354250, "juanitor-dashboard-backend", "The backend from juanitor monitoring"),
        new Repo(8441427, "mm-showmaster", "[Jura] Javascript project rendering the activities generated by the RCF"),
        new Repo(8556619, "brick-top", "[Deprecated] Project for Nature based on the full stack Ninja framework"),
        new Repo(8705029, "mongojack", "Mongojack maps Java objects to MongoDB documents. Based on the Jackson" +
            " JSON mapper, Mongojack allows you to easily handle your mongo objects as POJOs (insert, search by" +
            " id or by any other field, update)."),
        new Repo(8705498, "styleguide", ""),
        new Repo(8778953, "companypage", "The new page of devbliss"),
        new Repo(8899600, "boxy", "[DEPRECATED - see devbliss/bingo] LXC Container Management for Developer Machines"),
        new Repo(9165931, "freezoc-chat-ios", "XMPP client for chatting and gaming"),
        new Repo(9197108, "logparser", "Generic Log Parser and Monitor"),
        new Repo(9643477, "blissdoc-frontend", "Frontend plugin for blissdoc"),
        new Repo(9743139, "log2stats", "Parse log files and send stats to Ganglia Monitoring"),
        new Repo(9929303, "blissdoc-repository-handler", "Repository Handler Implementations for Blissdoc")

    );
  }
}
