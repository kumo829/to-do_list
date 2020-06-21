# TO-DO list

## Goal
TO-DO list and reminders for people that wants to archive its life goals.

As a second goal is to create and document a spring boot - ReactJS project using a microservices architecture.

## Architecture
The initial version of the architecture will include a lot of Spring Cloud components as:
 - **Config server**. Configuration will be in a GitHub private repo (sorry guys, but I'm planning to put very sensitive data there). I'll write what keys I'm putting on the repo, but no the actual values.
 - **Eureka**. Nothing more to say, a classic services registry.
 - **Zuul**. As API Gateway.
 
 On the first version I'm planning to use my own _Authorization Server_ but maybe will migrate to [KeyCloak](https://www.keycloak.org/) on some point.
 
 Also, I'll use [HashiCorp's Vault](https://www.vaultproject.io/) to store secret information like passwords and keys.
 
 ###  Diagram
 
Here I'll put the 