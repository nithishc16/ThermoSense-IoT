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



🛠️ Project Overview: ThermoSense
ThermoSense is a privacy-preserving thermal crowd detection and smart ventilation system designed to bridge the gap between human comfort and energy efficiency. By utilizing low-resolution thermal sensors, the system estimates real-time occupancy without compromising individual privacy through high-definition imagery.

The platform intelligently synchronizes hardware and software to dynamically control HVAC systems and natural ventilation, responding instantly to fluctuations in crowd density and environmental conditions.

Environment Preparation
Ensure the following services and devices are available before installation:
- PostgreSQL database service (running)
- Java Runtime Environment (21 or later)
- Arduino board connected via USB
- USB webcam connected
- Arduino IDE installed

Installation & Setup
1. Database Initialization
CrowdOracle uses PostgreSQL for persistent data storage.

sql
CREATE DATABASE CrowdOracle;
Ensure your PostgreSQL service is running before proceeding.

2. Backend Configuration
Navigate to: Backend/src/main/resources/application.properties

Update Database Credentials:

properties
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
Serial Communication Configuration:
Identify your Arduino port:

Windows: Check Device Manager (typically COM3, COM4, etc.)

Linux/Mac: Run ls /dev/tty* (typically /dev/ttyUSB0 or /dev/ttyACM0)

Update the configuration:

properties
serial.port.name=COM3  # Replace with your actual port
serial.enabled=true
3. IoT Hardware Configuration
Circuit Pinout:

Component	Arduino Pin	Notes
DHT11 Signal	Pin 7	Temperature & Humidity Sensor
Motor Enable (PWM)	Pin 9	Speed Control
Motor IN1 / IN2	Pin 10 / 11	Directional Control
LCD (I2C)	SDA / SCL	Data/Clock lines
Firmware Installation:

Open Iot/AurdinoConfig.ino in Arduino IDE

Install required libraries via Library Manager:

DHT sensor library

Adafruit Unified Sensor

LiquidCrystal I2C

Connect Arduino via USB and flash the code

Keep USB connected to host computer

4. Execution
Start the Backend:

bash
cd Backend
./mvnw spring-boot:run
Verification: Look for Serial port listener started and Started CrowdOracleApplication in logs.

Launch the Dashboard:

Open frontend/index.html in Chrome or Edge

Grant camera access when prompted

Click "Initialize Feed" to start AI detection and hardware sync

📁 Project Structure
text
CrowdOracle/
├── Backend/                 # Spring Boot Application
│   ├── src/main/java/      # Java source code
│   ├── src/main/resources/ # Configuration files
│   └── pom.xml            # Maven dependencies
├── frontend/               # Web Interface
│   ├── index.html         # Main dashboard
│   ├── styles/           # CSS files
│   └── scripts/          # JavaScript modules
└── Iot/                   # Hardware firmware
    └── AurdinoConfig.ino # Arduino sketch
🔧 Troubleshooting
Issue	Solution
Serial port not found	Check device connection and port name in application.properties
Camera access denied	Enable camera permissions in browser settings
Database connection failed	Verify PostgreSQL is running and credentials are correct
Arduino not detected	Reinstall USB drivers or try different USB port
📊 Features
Real-time People Counting: AI-powered object detection

Environmental Monitoring: Temperature and humidity tracking

Dynamic Alert System: Automated fan control based on thresholds

Live Dashboard: Real-time visualization of metrics

Data Persistence: Historical analytics storage
