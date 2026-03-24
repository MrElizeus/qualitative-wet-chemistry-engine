# Qualitative Wet Chemistry Inference Engine for Cations and Anions (QWCIE)

Rule-based Java backend for classical wet qualitative analysis of cation and anion identification workflows using a typed graph engine.

## Overview

QWCIE models analytical chemistry procedures as deterministic workflow transitions.

Key concepts:
- Nodes represent laboratory states.
- Reaction actions represent reagent additions or branch selections.
- Observed signals represent laboratory evidence (precipitates, colors, complexes).

The current milestone implements a chloride-first mainline workflow and keeps the architecture ready for Groups II–V and anion workflows.

## Architecture

The project is now a multi-module Maven build:

- `qwcie-domain`
  - Protocol contracts (`ReactionAction`, `ObservedSignal`)
  - Cation catalog and theoretical group metadata
- `qwcie-engine-core`
  - Generic state-machine runtime (`Node`, `NodeTransition`, `AnalysisEngine`, `TransitionOutcome`)
- `qwcie-workflow`
  - Workflow contracts and implementations (`WorkflowDefinition`, `WorkflowRegistry`, `GroupIWorkflowDefinition`)
- `qwcie-cli`
  - Command-line entry points for scripted and interactive runs

## Project Structure

```text
qwcie-engine/
├── pom.xml
├── qwcie-domain/
├── qwcie-engine-core/
├── qwcie-workflow/
└── qwcie-cli/
```

## Tech Stack

- Java 21
- Maven
- JUnit 5

## Run

```bash
cd qwcie-engine
mvn -q test
```

### Scripted CLI run

```bash
cd qwcie-engine
mvn -q install -DskipTests
cd qwcie-cli
mvn -q exec:java -Dexec.mainClass="io.github.mrelizeus.qwcie.cli.app.Main"
```

### Interactive step-by-step run (Group I)

```bash
cd qwcie-engine
mvn -q install -DskipTests
cd qwcie-cli
mvn -q exec:java -Dexec.mainClass="io.github.mrelizeus.qwcie.cli.app.GroupIInteractiveCli"
```

## Example Output

```text
Workflow: chloride-first-mainline
Starting node: MIXTURE_I_TO_III
Action: ADD_DILUTE_HCL_OR_NACL
Transitioned: true
Current node: GROUP1_CHLORIDE_PRECIPITATE
Observed signals: GROUP1_CHLORIDES_PRECIPITATED
Message: Group I chlorides precipitated after dilute HCl/NaCl addition.
```

## Current Milestone (Chloride-First Mainline)

Implemented nodes:

- `MIXTURE_I_TO_III`
- `GROUP1_CHLORIDE_PRECIPITATE`
- `POST_CHLORIDE_SPLIT`
- `POST_CHLORIDE_FILTRATE`
- `POST_CHLORIDE_PRECIPITATE_RESIDUE`
- `POST_ALKALINE_OXIDATIVE_SPLIT_POINT`
- `ALKALINE_PRECIPITATE_PHASE`
- `ACID_DISSOLVED_PRECIPITATE_PHASE`
- `ALKALINE_SOLUTION_PHASE`
- `POST_CONC_HCL_SPLIT`
- `GROUP1_RESIDUE_PATH_AG_PB_HG`
- `GROUP1_DISSOLVED_PATH_SB_PB_COMPLEX`
- `POST_K2CRO4_PB_SPLIT`
- `SB_CANDIDATE_AFTER_PB_NEGATIVE`
- `POST_NH4OH_HG_SPLIT`
- `HG_CONFIRMED_BLACK_RESIDUE`
- `AG_AMMINE_PATH`
- `POST_HNO3_SILVER_SPLIT`
- `AG_CONFIRMED_WHITE_AGCL`
- `AG_NOT_CONFIRMED_AFTER_HNO3`
- `PB_CONFIRMED_YELLOW_PBCRO4`
- `SB_CONFIRMED_LILAC_COMPLEX`

## Roadmap

- Add Group II workflow module.
- Add Groups III–V workflow modules.
- Add anion workflow modules.
- Externalize workflow rules (JSON/YAML) without engine rewrite.
- Expose REST API for frontend and integrations.

Author: Eliseo Huetagoyena
