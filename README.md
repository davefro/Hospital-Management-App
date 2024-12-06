
# Hospital Management App

This is a simple Hospital Management Application built for managing hospital operations such as adding, updating, and viewing appointments. It demonstrates secure data handling using encryption, efficient database management, and robust UI interactions with Android Studio. The project was developed in several parts, each focusing on the development of individual Labs. The first was user uuthentication (login, register, forgot password), the second patient panagement, the third doctor management and the fourth appointment management.

---

## Features
- User authentication with SHA256 password encryption.
- Secure storage of appointment reasons using AES encryption.
- Prevents duplicate appointments by checking for existing data before insertion.
- Full CRUD operations for patients, doctors, and appointments.
- User-friendly UI with validation and feedback using Material Design.
- Notifications for added appointments.

---

## Installation
1. Clone this repository:
   ```
   git clone https://github.com/davefro/Hospital-Management-App.git
   ```
2. Open the project in **Android Studio**.
3. Sync Gradle to install dependencies.
4. Run the application on an emulator or physical device.

---

## Project Structure
- `MainActivity`: Handles user login.
- `RegisterPage`: Manages user registration.
- `ResetPassword`: Allows users to reset their password.
- `PatientManagement`: Allows users to manage patients.
- `DoctorManagement`: Allows users to manage doctors.
- `AppointmentManagementActivity`: Main functionality for managing appointments.
- `DBHelper`: Handles database operations for users, patients, doctors, and appointments.
- `AESEncryptionHelper`: Provides encryption and decryption for sensitive data.
- `PasswordEncryption`: Provides encryption for password

---

## Dependencies
- **Material Design Components**: For modern UI and interactions.
- **SQLite**: For local database management.
- **Espresso & JUnit**: For testing UI elements and backend logic.

---

## Testing
- Unit tests with JUnit for encryption/decryption and database methods.
- UI tests with Espresso for validating form inputs and checking UI flow.

---

## Contribution
1. Fork this repository.
2. Create a new branch (`feature-branch`).
3. Commit your changes.
4. Push your branch.
5. Open a Pull Request.

---

## License
This project is licensed under the MIT License - see the LICENSE file for details.

---


## References
1. https://developer.android.com/guide
2. https://www.flaticon.com
3. https://www.stackoverflow.com
4. https://www.unsplash.com/

## Demonstration Videos

### User Authentication
[![Watch the video](https://img.youtube.com/vi/nSYfKazpP7c/0.jpg)](https://www.youtube.com/watch?v=nSYfKazpP7c)

### Patient/Doctor Management
[![Watch the video](https://img.youtube.com/vi/AOTNyrIDHPE/0.jpg)](https://youtube.com/shorts/AOTNyrIDHPE)

### Appointment Management
[![Watch the video](https://img.youtube.com/vi/c3HFNbG7bZ8/0.jpg)](https://youtube.com/shorts/c3HFNbG7bZ8)

### Encryption Data
[![Watch the video](https://img.youtube.com/vi/yehvMHbeSn8/0.jpg)](https://youtu.be/yehvMHbeSn8)