# EduTrack Design & Architecture Documentation Index

This directory contains formal software engineering diagrams, architectural descriptions, and schema designs representing the **actual implementation** of the EduTrack Academic Analytics CLI system.

---

## 1. Diagram Index & Visual References

| Diagram | Description | PNG Image File | Diagram Source File |
| :--- | :--- | :--- | :--- |
| **System Architecture** | 5-Layer architecture showing Presentation, Service, Strategy, Domain, and File-Backed Persistence layers. | [`architecture-diagram.png`](architecture-diagram.png) | [`diagrams/architecture.mmd`](diagrams/architecture.mmd) / [`diagrams/architecture.puml`](diagrams/architecture.puml) |
| **Operational Workflow** | End-to-end user workflow from startup, authentication, menu dispatch, validation, risk calculation to CSV durability. | [`workflow-diagram.png`](workflow-diagram.png) | [`diagrams/workflow.mmd`](diagrams/workflow.mmd) / [`diagrams/workflow.puml`](diagrams/workflow.puml) |
| **UML Use Case** | System boundary, 3 actual user actors (Faculty, Admin, Student) and 12 core functional use cases. | [`use-case-diagram.png`](use-case-diagram.png) | [`diagrams/use-case.mmd`](diagrams/use-case.mmd) / [`diagrams/use-case.puml`](diagrams/use-case.puml) |
| **UML Class Diagram** | Comprehensive class hierarchy showing inheritance, interfaces (`StudentRepository`, `InterventionStrategy`), domain entities, and services. | [`class-diagram.png`](class-diagram.png) | [`diagrams/class.mmd`](diagrams/class.mmd) / [`diagrams/class.puml`](diagrams/class.puml) |
| **UML Sequence Diagram** | End-to-end execution flow of assessment mark entry, algorithmic risk calculation, and strategy-driven intervention generation. | [`sequence-diagram.png`](sequence-diagram.png) | [`diagrams/sequence.mmd`](diagrams/sequence.mmd) / [`diagrams/sequence.puml`](diagrams/sequence.puml) |
| **Storage / ER Schema** | True data schema mapping the flat-file CSV storage (`students_db.csv`) to in-memory domain entities and credential registries. | [`er-diagram.png`](er-diagram.png) | [`diagrams/er.mmd`](diagrams/er.mmd) / [`diagrams/er.puml`](diagrams/er.puml) |

---

## 2. Supporting Documents in this Directory

- [`requirements.md`](requirements.md): Dedicated functional requirements (FR-01 to FR-11) and measurable non-functional requirements.
- [`report-outline.md`](report-outline.md): Complete 15-section project report structure aligned with VITyarthi submission format.
- [`vityarthi-compliance.md`](vityarthi-compliance.md): Formal compliance audit table demonstrating 100% adherence to the evaluation rubric.
- [`screenshots/README.md`](screenshots/README.md): Detailed terminal transcripts and screenshot capture guidance for evaluation.
- [`design_artefacts.md`](design_artefacts.md): Technical narrative and embedded Mermaid diagram specifications.
- [`PROJECT_REPORT.md`](PROJECT_REPORT.md): Complete, ready-to-read academic project report.
- [`PROJECT_REPORT.pdf`](PROJECT_REPORT.pdf): Pre-compiled print-ready PDF project report.
