# Qualitative Wet Chemistry Inference Engine for Cations and Anions (QWCIE)

Rule-based Java engine for classical wet qualitative analysis of cation/anion identification reactions using a decision graph/state machine.

## Overview

QWCIE models wet analytical chemistry workflows as graph transitions:

- States -> analysis nodes
- Observations -> experimental outcomes
- Transitions -> next decision step

This engine is designed to:

- Simulate qualitative analysis routes
- Standardize wet-lab decision logic
- Support interactive tools (CLI/Web/API)
- Scale to multiple analytical methods

## Technical Approach

- Decision Graph
- State Machine

## Project Structure

```
acie-engine/src/main/java/io/github/mrelizeus/qwcie/
├── cli/
├── domain/
└── engine/
```

## Core Components

- `domain`: `Observation`, `Ion`
- `engine`: `Node`, `AnalysisEngine`
- `cli`: `Main`

## Tech Stack

- Java 21
- Maven
- JUnit 5

## Run

```bash
mvn compile
mvn exec:java -Dexec.mainClass="io.github.mrelizeus.qwcie.cli.Main"
```

## Example Output

```text
Estado inicial: Inicio
Ahora estas en: Grupo I precipitado
```

## Roadmap

- Phase 1: base structure and inference core
- Phase 2: Group I, Group II and anion routes
- Phase 3: external rule loading, API and frontend integration

Author: Eliseo Huetagoyena
