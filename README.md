<h1 align="center">Hi 👋, I'm ThermoSense</h1>
<h3 align="center">Smart Thermal-Based Occupancy Detection and Intelligent Ventilation Control System</h3>

- 🔭 I’m currently working on [ThermoSense](https://github.com/nithishc16/ThermoSense-IoT)

- 🌱 I’m currently learning **Spring Boot & Microservices ,Embedded Systems with Arduino ,IoT Communication Protocols (MQTT, HTTP) ,Computer Vision & Edge AI**

- 👯 I’m looking to collaborate on [ThermoSense](https://github.com/nithishc16/ThermoSense-IoT)

- 🤝 I’m looking for help with [ThermoSense](https://github.com/nithishc16/ThermoSense-IoT)

- 👨‍💻 All of my projects are available at [https://github.com/nithishc16](https://github.com/nithishc16)

- 💬 Ask me about **IoT, Java, Spring Boot, Arduino, Smart Building Systems**

- 📫 How to reach me **nithishc67@gmail.com**

- 📄 Know about my experiences [www.linkedin.com/in/nithishc16](www.linkedin.com/in/nithishc16)

- ⚡ Fun fact **I enjoy turning real-world problems into smart automation solutions 😄**

<h3 align="left">Connect with me:</h3>
<p align="left">
<a href="https://linkedin.com/in/www.linkedin.com/in/nithishc16" target="blank"><img align="center" src="https://raw.githubusercontent.com/rahuldkjain/github-profile-readme-generator/master/src/images/icons/Social/linked-in-alt.svg" alt="www.linkedin.com/in/nithishc16" height="30" width="40" /></a>
</p>

<h3 align="left">Languages and Tools:</h3>
<p align="left"> <a href="https://getbootstrap.com" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/bootstrap/bootstrap-plain-wordmark.svg" alt="bootstrap" width="40" height="40"/> </a> <a href="https://www.w3schools.com/cpp/" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/cplusplus/cplusplus-original.svg" alt="cplusplus" width="40" height="40"/> </a> <a href="https://www.w3schools.com/css/" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/css3/css3-original-wordmark.svg" alt="css3" width="40" height="40"/> </a> <a href="https://www.w3.org/html/" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/html5/html5-original-wordmark.svg" alt="html5" width="40" height="40"/> </a> <a href="https://www.java.com" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="java" width="40" height="40"/> </a> <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/javascript/javascript-original.svg" alt="javascript" width="40" height="40"/> </a> <a href="https://www.postgresql.org" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/postgresql/postgresql-original-wordmark.svg" alt="postgresql" width="40" height="40"/> </a> <a href="https://postman.com" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/getpostman/getpostman-icon.svg" alt="postman" width="40" height="40"/> </a> <a href="https://www.python.org" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/python/python-original.svg" alt="python" width="40" height="40"/> </a> <a href="https://spring.io/" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/springio/springio-icon.svg" alt="spring" width="40" height="40"/> </a> <a href="https://www.tensorflow.org" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/tensorflow/tensorflow-icon.svg" alt="tensorflow" width="40" height="40"/> </a> </p>


✅ Prerequisites

- Java 11+ (Backend)
- Node.js & npm (Frontend development)
- PostgreSQL 12+
- Arduino IDE (IoT firmware)
- Modern Web Browser (Chrome / Edge recommended)



 ⚙️ Installation & Setup

1. Database Initialization
CrowdOracle uses PostgreSQL for persistent data storage.

Ensure PostgreSQL is running, then execute:

```sql
CREATE DATABASE CrowdOracle;


2. Backend Configuration
Navigate to Backend/src/main/resources/application.properties.
Update your database credentials:
```
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
```
Critical: Set the correct Serial Port for your Arduino (check Device Manager on Windows or /dev/tty* on Linux/Mac):
```
serial.port.name=COM3  # Change to your actual port (e.g., COM4, /dev/ttyUSB0)
serial.enabled=true
```

Critical: Set the correct Serial Port for your Arduino (check Device Manager on Windows or /dev/tty* on Linux/Mac):
```
serial.port.name=COM3  # Change to your actual port (e.g., COM4, /dev/ttyUSB0)
serial.enabled=true
```
3. IoT Hardware Setup
Circuit Connection:
DHT11 Signal: Pin 7
Motor Enable (PWM): Pin 9
Motor IN1: Pin 10
Motor IN2: Pin 11
LCD: I2C Pins (SDA/SCL)
Open Iot/AurdinoConfig.ino in Arduino IDE.
Install required libraries (DHT sensor library, Adafruit Unified Sensor, LiquidCrystal I2C).
Upload the code to your Arduino.
Keep the Arduino connected via USB to the computer running the Backend.
4. Running the Application
Start the Backend:
```
cd Backend
./mvnw spring-boot:run
```
Wait for the log: "Serial port listener started" & "Started CrowdOracleApplication".

Launch the Dashboard:

Simply open ```frontend/index.html``` in a modern web browser.
Allow Camera permissions when prompted.
Click "Initialize Feed" to start AI detection.




