# L3KS1K - REST API Repository

## Data flow diagram

![image](https://github.com/L3ks1k/L3KS1K-REST/assets/114097246/4cbbbb66-a905-48fe-9bba-c5d7adb10b31)

### Threat modeling - STRIDE

|         Threat         | Security Property | Which components                   | Identified Threats                                                                                                                                                                                                                   |
|:----------------------:|-------------------|------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Spoofing               | Authentication    | Processes, Interactors             | 1. Actor could possibly access data from other users. 2. Chat must be accessible for user, therefore authentication mechanism must be easy for use. However ease must not introduce which allows attacker to bypass login features. |
| Tampering              | Integrity         | Data Flows, Data Stores, Processes | 1. User information/messages can be edited. There is potential threat of other people, except legitimate user, tampering user data.                                                                                                  |
| Repudiation            | Non-repudiation   | Procesess, Interactors             | 1. User can send malicious message, hijack session and in the end deny taken actions.                                                                                                                                                |
| Information Disclosure | Confidentiality   | Data Flows, Data Stores, Processes | 1. Messages or user information are considered confidential, there is a threat of other users accessing that data. 2. Files metadata, are also considered confidential, there is  a threat of other users extracting metadata.       |
| Denial of Service      | Availability      | Data Flows, Data Stores, Processes | 1. Chat has high availability requirements, there is a threat of conducting DDoS attack by  malicious actors                                                                                                                         |
| Elevation of Privilege | Authorization     | Processes                          | 1. User can discover vulnerability which allows to get administrative access to application.                                                                                                                                         |
