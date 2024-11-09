# DocFinder: Doctor's Appointment App

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Demo Video](#demo-video)

## Overview
**DocFinder** is an Android application designed to streamline the patient experience for booking doctor appointments. It offers a multitude of benefits that simplify healthcare access for patients:

- **Doctor Search**: Patients can browse a comprehensive database of qualified doctors across various specialties, helping them find the perfect match for their needs.
- **Appointment Scheduling**: Patients can schedule appointments directly through the app, eliminating the need for phone calls or clinic visits.
- **Secure Payment**: A simplified, secure payment process allows patients to pay appointment fees within the app, saving time and hassle.
- **24/7 Accessibility**: DocFinder enables patients to book appointments anytime, anywhere, providing flexibility and convenience.

DocFinder aims to make healthcare access easier, faster, and more accessible for patients.

## Features

- **User-Centric Interface**: DocFinder prioritizes an intuitive, visually appealing interface built with Google's Material Design framework for enhanced user experience.

- **App Security & User Verification**: To prevent unauthorized access, DocFinder uses Google Firebase's Phone Authentication, requiring OTP verification for user registration and login.

- **Remote Database**: The app utilizes Google Firebase Firestore as a scalable, cloud-based NoSQL database for storing user information, doctor profiles, and appointment details, allowing seamless access across devices.

- **Data Redundancy**: To improve performance, DocFinder stores data in three locations:
  - **Local Database (SQLite)**: Frequently accessed data is stored locally for faster retrieval.
  - **Local Storage (SharedPreferences)**: User settings and preferences are saved to persist between sessions.
  - **Remote Database (Firebase Firestore)**: A full backup of user data is maintained in Firestore to ensure accessibility across devices.

- **Background Tasks with AsyncTask**: DocFinder uses AsyncTask to handle data-fetching tasks in the background, keeping the app responsive and lag-free for a smoother user experience.

## Tech Stack
- Backend: Java
- Frontend: MaterialUI
- Database: MySQL, Firestore
- Security: Firebase OTP
  
## Demo Video
https://github.com/user-attachments/assets/ee15457e-96c4-422c-83e4-66ffb11b2ba3




