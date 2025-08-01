
> 📘 **English Version**  
> This document is the English version of the original project README.  
> Looking for the Portuguese version? [Click here 🇵🇹](./README.pt.md)

DEMOCRACY 2.0 –> SpringBoot Reference Project

UC: Software Systems Construction

Authors:
	•	Alexandre Müller - fc56343
	•	Diogo Ramos - fc56308
	•	Francisco Henriques - fc56348

⸻

Description:

Due to the high abstention rate in recent elections (44% in 2015, 51% in 2019, and 49% in 2022), and general dissatisfaction with political representation, the National Elections Commission is exploring an alternative representational model. Your team was selected to develop the test platform.

In this proposed model, citizens will have the opportunity to vote directly on every law proposal that is currently voted on in Parliament. This allows each citizen to express their opinion on specific topics. Unlike the current model, where selecting a representative delegates all votes to them regardless of topic, the new model enables citizens to express a voting sequence not aligned with any single representative.

However, without representatives, citizens must be more involved in legislation. To mitigate this, individuals can volunteer as delegates (similar to current MPs, but without elections). Their explicit votes become public, and citizens can delegate their vote to a delegate either completely or by topic (e.g., health, education, infrastructure, immigration, etc).

The project involves building a software system to manage citizens, delegates, proposals, and votes. It must fulfill key requirements such as security, privacy, scalability (Portugal has 10 million residents), and integration with other government systems.

⸻

Functional Requirements (Use Cases):

Note: Three use cases related to integration with government authorization systems were dropped and will not be implemented.

	•	(F1) List Active Votes: List all law proposals currently open for voting.
	•	(F1) Submit a Law Proposal: Delegates can propose laws. Each proposal must include a title, description, PDF attachment, expiration date (max 1 year), topic, and the proposing delegate.
	•	(F1) Close Expired Proposals: All proposals past their expiration date must be closed and can no longer receive support.
	•	(F1) View Law Proposals: List and consult non-expired law proposals.
	•	(F1) Support Law Proposals: Citizens can support one proposal each. If a proposal receives 10,000 supports, a vote is created for it, expiring on the proposal’s expiration date (min 15 days, max 2 months). The proposing delegate automatically casts a favorable vote.
	•	(F1) Choose a Delegate: A citizen can select one delegate per topic (e.g., Health, Education). When a vote closes and the citizen hasn’t voted, a vote is cast automatically based on the most specific applicable delegate.
	•	(F1) Vote on a Proposal: Citizens view active votes, check their default vote (based on delegate), and vote (for or against; no blank/null votes). The system checks whether the citizen already voted but does not store their vote choice. If the voter is also a delegate, their vote is public.
	•	(F1) Close a Vote: When voting closes, delegate votes are applied to citizens who didn’t vote. Votes are counted. If over 50% are favorable, the proposal is approved; otherwise, it’s rejected.

⸻

Non-Functional Requirements:
	•	Votes from regular citizens must remain secret. The database should only record that a citizen voted, not their vote.
	•	All data must be stored in a relational database.
	•	The platform must be implemented using Spring Boot and Java for cost-effectiveness and hiring ease.
	•	The data layer must use JPA and Spring Data.
	•	The business logic layer must follow the Domain Model pattern, with metadata sufficient for JPA to handle database mapping.
	•	A REST API should be exposed for interaction via web or mobile clients.
	•	Only code meeting the team’s quality standards should be accepted via pre-commit.
	•	The project must run inside Docker, which will also be the deployment method.
	•	Participation control will be monitored via Git repository activity.

⸻

# How to run the project:

## First step

Run the script `setup.sh`.

## Second step

Run the script `run.sh`.

This command will start two containers:

* One with the application in this folder.
* An instance of a Postgres container.

## Third step

### If you want to use the WEB application:
    Open http://localhost:8080 ;

### If you want to use the Desktop application (which uses a REST API):
    Run the command: "mvn clean javafx:run"
