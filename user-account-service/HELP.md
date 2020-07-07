# User Registration Micro Service

### Introduction
This microservice is responsible to manage user's account, from account creation, validation, profile management, etc.

Uses **Feign Client** for call *users-service* and a custom **ApplicationEvent** to send the confirmation email to the client.