
# [LogEasy.io](https://www.github.com/amdsrinivas/logeasyio)
## A proof of concept for Client side log management service.
#### In Brief
> A complete client managed log ingestion service. Useful in keeping track of logs located in multiple places. The data of the client is ingested to Client's DB only by a service managed by the client itself. The backend is only used to schedule the log ingestion on the client side.

### Architecture
The application has three components that work together to ingest and maintain the logs in the Database.
- **Backend Service** : All the clients connect to Backend to register their hosts, aggregators and loggers(The applications which produce the logs that needs to be ingested).
- **Aggregator Service** : This is a client managed service which runs the jobs to load the data from log files to database based on the registration specifications.
- **Scheduler** : This is currently part of the backend which checks the heartbeat of the aggregators registered to backend and triggers the aggregator service at scheduled intervals.

### Technologies used
- Springboot
- RESTful services
- MongoDB
- Vaadin (for UI)

### Screenshots
*Coming soon*

### Next Steps
Contributors are welcome for:
- Backed Scheduler yet to be configured.
- Code sanitization
- DB access optimization
- Effective Exception handling
- UI tweaks
- Test cases