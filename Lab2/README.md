
# Hospital Management App - Lab 2

This project is part of a series of labs to develop a Hospital Management App. The goal of Lab 1 was to create a user registration and login system with validation and navigation functionalities, and to implement a SQLite database for user credentials.

---

## **Tasks Completed in Lab 2**

### 1. **Create User Registration, Login, and Reset Password Interfaces**
- Designed user-friendly interfaces using `EditText`, `Buttons`, and `TextViews`.
- Developed the following screens:
  - Login Screen (`activity_main.xml`)
  - Registration Screen (`activity_register_page.xml`)
  - Reset Password Screen (`activity_reset_password.xml`)

### 2. **Implement SQLite Database**
- Created a SQLite database (`HospitalsDB.db`) for storing user credentials (`email` and `password`).
- Developed a `DBHelper` class to manage database operations.

### 3. **Develop Registration Functionality**
- Implemented functionality to store new users in the database through the `RegisterPage` activity.
- Added validation to check for existing users before registration.

### 4. **Develop Login Functionality**
- Implemented functionality to authenticate users by verifying credentials stored in the database.
- Developed the `MainActivity` for login.

### 5. **Develop Reset Password Functionality**
- Implemented a `ResetPassword` activity that allows users to reset their passwords.
- Added functionality to update user passwords in the SQLite database.
- Included validation for:
  - Existing email in the database.
  - Matching new password and confirm password fields.
  - Strong password requirements.

### 6. **Validate Input Data**
- Added validation to ensure:
  - No empty fields.
  - Valid email format.
  - Strong password requirements (at least 8 characters, including a capital letter and special character).
  - Passwords match during registration and reset password.

### 7. **Handle User Navigation**
- Enabled navigation between screens:
  - From Login to Registration.
  - From Login to Reset Password.
  - From Registration to Login.
  - After successful login, navigated to the Dashboard screen (`DashboardActivity`).

### 8. **Provide User Feedback**
- Used `Toast` messages to notify users about:
  - Successful login, registration, or password reset.
  - Input errors or missing fields.
  - Failed login attempts due to incorrect credentials.

### 9. **Push Project to GitHub**
- Initialized a Git repository and pushed the project to a GitHub repository.
- Configured `.gitignore` to exclude unnecessary files such as `build/` and `.idea/`.

---

## **How to Run the Project**

1. Clone the repository:
   ```bash
   git clone https://github.com/davefro/Hospital-Management-App.git
   cd HospitalManagementApp
   ```

2. Open the project in Android Studio.

3. Build and run the app on an emulator or physical device.

---

## **Features**

- User Registration with Validation
- Login with Database Authentication
- Reset Password Functionality
- SQLite Database for User Credentials
- Navigation Between Screens
- User Feedback via `Toast` Messages

---

## **Next Steps**

- Continue with Lab 3: Implement Patient Management System.
- Set up GitHub CI/CD pipelines for automated builds and security checks.

---


## **License**

This project is licensed under the MIT License.
