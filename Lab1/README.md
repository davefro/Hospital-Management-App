# Lab 1: Epics and User Stories


## Introduction
This document outlines the core functionalities of the Hospital Management App, broken into epics and their corresponding user stories. Each user story includes acceptance criteria to guide functional development. Additionally, security-focused evil user stories are provided to anticipate potential vulnerabilities, along with mitigation strategies to address them.

---

## Epics and User Stories

### Epic 1: User Authentication
- **User Story 1:**
    - *As a hospital staff member,* I want to securely log into the app so that I can access the features appropriate to my role.
    - **Acceptance Criteria:**
        1. The login page has fields for username and password with input validation.
        2. Authentication is role-based, showing different dashboards for doctors, nurses, and admin staff.
        3. Passwords must meet security criteria: minimum 8 characters, at least one number, and one special character.

- **Evil User Story 1:**
    - *As a malicious user,* I want to brute force login credentials so that I can gain unauthorized access.
    - **Mitigation Strategies:**
        1. Limit failed login attempts to five before locking the account.
        2. Implement CAPTCHA after three failed attempts.
        3. Log failed login attempts for security monitoring.

---

### Epic 2: Patient Management
- **User Story 1:**
    - *As a doctor,* I want to add patient records so that I can track their medical history.
    - **Acceptance Criteria:**
        1. Patient records must include fields for name, DOB, contact info, and medical history.
        2. Records are encrypted at rest and in transit.
        3. Only authorized roles can add or modify patient records.

- **User Story 2:**
    - *As a staff member,* I want to update patient records so that I can keep information accurate.
    - **Acceptance Criteria:**
        1. Update functionality is available through the patient detail page.
        2. Changes are logged with timestamps and user ID.

- **Evil User Story 1:**
    - *As a malicious user,* I want to modify patient records without leaving a trace so that I can tamper with medical outcomes.
    - **Mitigation Strategies:**
        1. Implement immutable audit logs.
        2. Notify administrators of unauthorized or suspicious access attempts.

---

### Epic 3: Appointment Management
- **User Story 1:**
    - *As a patient,* I want to schedule an appointment with a doctor so that I can receive timely medical care.
    - **Acceptance Criteria:**
        1. Appointment requests include fields for date, time, and reason.
        2. Patients can view a confirmation of their scheduled appointments.

- **User Story 2:**
    - *As a staff member,* I want to update or cancel appointments so that the schedule reflects current availability.
    - **Acceptance Criteria:**
        1. Only authorized roles can update or cancel appointments.
        2. Patients receive notifications for updates or cancellations.

- **Evil User Story 1:**
    - *As a malicious user,* I want to delete appointment records to disrupt hospital operations.
    - **Mitigation Strategies:**
        1. Use soft delete, flagging records as deleted without permanent removal.
        2. Require administrative approval for deletions.
        3. Log all appointment-related actions.

---

### Epic 4: Security and Compliance
- **User Story 1:**
    - *As an admin,* I want to monitor user activities so that I can ensure the system is used securely and appropriately.
    - **Acceptance Criteria:**
        1. User actions are logged with timestamps, roles, and activity descriptions.
        2. Logs are encrypted and accessible only to admin roles.

- **Evil User Story 1:**
    - *As a malicious user,* I want to inject malicious code to access or manipulate sensitive data.
    - **Mitigation Strategies:**
        1. Sanitize and validate all user inputs.
        2. Use parameterized queries to prevent SQL injection.
        3. Perform penetration testing regularly.

---

## Conclusion
The Hospital Management App requirements are defined through structured epics and user stories, ensuring functional and security goals are met. Evil user stories highlight potential risks, and corresponding mitigation strategies are designed to protect the system from vulnerabilities. These requirements form the foundation for secure, efficient, and user-friendly development.
