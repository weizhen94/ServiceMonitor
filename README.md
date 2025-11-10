1. Quickstart instructions:

- For backend: (cd into service-monitor-backend)

* To run: ./mvnw spring-boot:run
* To build: ./mvnw -DskipTests package
* To run using build: java -jar target/service-monitor-backend-0.0.1-SNAPSHOT.jar
* To build using docker: docker build -t service-monitor-backend . 
* To run using docker build: docker run --name smb --rm -p 8080:8080 -v "$(pwd)/data:/app/data" service-monitor-backend

- For frontend: (cd into service-monitor-frontend)

* To run: ng serve
* To build using docker: docker build -t service-monitor-frontend . 
* To run using docker build: docker run --name smf --rm -p 4200:80 service-monitor-frontend

2. Design overview and trade-offs:

- Persistence:
* Usage of Json file to store results instead of a database is good for fast implementation or for quick POCs (no need to spin up and create sql tables or connect to database)
* However, database is better if archiving of entries in health.json is required and Service Monitor application is running daily, every few minutes. 
* Database could also be used to store the information currently in services.json. This is so that adding or deleting of services can be done through updating the database and won't require the json file to be changed through a deployment. 

- Backend: (a few improvements to make)
* Email notification using JavaMailSender
* Creating a docker-compose.yml file 
* Create mock services for testing 
* Create functional test cases
* Logging of error messages using Log4j for future troubleshooting
* Introduction of Spring security authentication features and JWT tokens for privilege access (if required)

- Frontend: 
* Improve aesthetics using bootstrap and cards
* Login page for privilege access

3. Infrastructure / deployment notes:

- Package & configure: 
* Containerize the app (Docker) so it runs the same everywhere.
* Keep settings (service URLs, intervals) outside the app using env vars or config files managed by platform (e.g., Kubernetes ConfigMaps/Secrets or a cloud parameter store).

- Deploy: 
* Kubernetes (preferred): use a Deployment (2+ replicas) and let the platform handle restarts and rolling updates.
* Alternative: run on a VM with a process manager (systemd/PM2) and a cron/timer for restarts.

- Monitoring: 
* Expose simple metrics the platform can read (such as working, latency)
* Use Prometheus to collect metrics and Grafana to visualize dashboards.
* If using cloud, use CloudWatch (AWS), Cloud Monitoring (GCP), or Azure Monitor.

- Logs
* Write structured logs to stdout.
* Centralize them with a stack like ELK/Opensearch, or cloud’s logging (CloudWatch Logs, GCP Logging).

- Alerts 
* Set alerts for when a service is down for a few minutes, repeated failures, or the report hasn’t updated recently.
* Route alerts to Slack, PagerDuty, or Email via Alertmanager or your cloud’s alerting.

- Security & reliability
* Store secrets in a Secrets manager (K8s Secrets, AWS Secrets Manager).
* Use rolling or blue/green deployments to release safely.
* Scan images/dependencies (e.g., Trivy, Dependabot).

- CI/CD
* Automate build → test → scan → deploy with GitHub Actions, GitLab CI, or cloud CI.
* Keep a small runbook: what to check when everything is red (logs, metrics, recent deploys, config changes).


4. Tool used: 
ChatGPT – ideation, debugging and testing guidance

No proprietary or confidential data was shared with AI tools. All final design decisions, code wiring, verifications and testing were all manually performed as AI suggestions can be outdated or inaccurate at times. 

Where AI helped:
* Architecture brainstorming
    * Assisted to brainstorm the approach for backend, frontend and choice for persistence:
        * Java backend: Load service definitions from a JSON config file. Runs a scheduled job every N seconds to ping all services. Persists the latest results to a JSON file
        * Angular frontend: Set up a service to call the backend. Create a dashboard component to expose results. 
        * Persistence: Persists the latest results to a JSON file 
* Generating boilerplate
    * Skeletons for services, configs, models, Dockerfile 
* Testing and Debugging 
    * Asked AI on error messages faced or when application did not work as intended. When the application was built, during each part, testing was done. When the intended functionality was not achieved for a part, AI serves as a good assistant to quickly brainstorm possible reasons.
