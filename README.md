# Securing Data in Multi-Cloud Storage using Improved Threshold Secret Sharing

[![Degree: MSc](https://img.shields.io/badge/Degree-MSc-blue.svg)](https://orcid.org/0000-0001-6959-8352)
[![Field: Cryptography](https://img.shields.io/badge/Field-Cryptography-green.svg)](https://orcid.org/0000-0001-6959-8352)
[![Implementation: Java](https://img.shields.io/badge/Implementation-Java-orange.svg)](https://github.com/cloudbus/cloudsim)

This repository contains the simulation framework and cryptographic implementation for the research:  
**"Securing Data in Multi-Cloud Storage Using an Improved Threshold Secret Sharing Algorithm"**.

This project investigates advanced secret sharing primitives to ensure data availability and confidentiality across a distributed multi-cloud architecture.

##  Research Contributions

* **Optimized Data Dispersal:** Evaluated and improved **Threshold Secret Sharing (TSS)** foundations, specifically comparing **Lagrange interpolation** and **FFT-based techniques** against linear algebraic approaches like **Rabin’s Information Dispersal Algorithm (IDA)**.
* **Performance vs. Security Analysis:** Conducted a study of the trade-offs between computational complexity (polynomial evaluation vs. matrix operations) and information-theoretic security bounds.
* **Fault-Resilient Storage:** Designed protocols to ensure data remains secure and retrievable even if a subset of cloud service providers is compromised.



##  Evaluation Environment

The research is implemented and validated using **CloudSim 3.0**, a specialized framework for modeling cloud computing infrastructures.

* **Simulation Metrics:** Analysis of encryption/encoding overhead, shares generation latency, and data reconstruction time across distributed nodes.
* **Scalability:** Validated the performance of the proposed storage protocols in simulated high-traffic multi-cloud environments.

##  Getting Started

### Prerequisites
* **Java JDK 8** or higher.
* **Apache Ant** (for building via `build.xml`).
* **flanagan.jar**: Scientific library required for the mathematical modeling and interpolation used in the simulation.
