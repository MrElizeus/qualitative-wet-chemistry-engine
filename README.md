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
  - Typed chemical species model (`ChemicalSpecies`)
  - Cation catalog and theoretical group metadata
- `qwcie-engine-core`
  - Generic state-machine runtime (`Node`, `NodeTransition`, `AnalysisEngine`, `TransitionOutcome`)
- `qwcie-workflow`
  - Workflow contracts and implementations (`WorkflowDefinition`, `WorkflowRegistry`, `ChlorideMainlineWorkflowDefinition`)
  - Declarative workflow specs (`WorkflowNodeSpec`, `WorkflowTransitionSpec`, `WorkflowGraphFactory`)
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
mvn -q verify
```

### Scripted CLI run

```bash
cd qwcie-engine
mvn -q install -DskipTests
cd qwcie-cli
mvn -q exec:java -Dexec.mainClass="io.github.mrelizeus.qwcie.cli.app.Main"
```

### Interactive step-by-step run (Chloride Mainline)

```bash
cd qwcie-engine
mvn -q install -DskipTests
cd qwcie-cli
mvn -q exec:java -Dexec.mainClass="io.github.mrelizeus.qwcie.cli.app.ChlorideMainlineInteractiveCli"
```

## Quality Gates

- Coverage: JaCoCo runs during `verify` and enforces a minimum line coverage ratio.
- Static analysis: Checkstyle runs during `verify` and fails the build if style/safety rules are violated.
- CI: GitHub Actions executes `mvn verify` for every push and pull request.

## Example Output

```text
Workflow: chloride-first-mainline
Starting node: MIXTURE_I_TO_III
Action: ADD_DILUTE_HCL_OR_NACL
Transitioned: true
Current node: CHLORIDE_PRECIPITATE_NODE
Observed signals: CHLORIDE_PRECIPITATE_FORMED
Message: Chloride precipitate formed after dilute HCl/NaCl addition.
```

## Current Milestone (Chloride-First Mainline)

Implemented nodes:

- `MIXTURE_I_TO_III`
- `CHLORIDE_PRECIPITATE_NODE`
- `POST_CHLORIDE_SPLIT`
- `POST_CHLORIDE_FILTRATE`
- `POST_CHLORIDE_PRECIPITATE_RESIDUE`
- `POST_ALKALINE_OXIDATIVE_SPLIT_POINT`
- `ALKALINE_PRECIPITATE_PHASE`
- `ACID_DISSOLVED_PRECIPITATE_PHASE`
- `POST_AMMONIACAL_SPLIT_POINT`
- `FE_MN_RESIDUE_PHASE`
- `NI_CU_CD_AMMINE_PHASE`
- `FE_MN_OXIDIZED_SOLUTION_PHASE`
- `FE_CONFIRMATION_PATH`
- `FE_PRUSSIAN_BLUE_CONFIRMED`
- `FE_THIOCYANATE_BLOOD_RED_CONFIRMED`
- `MN_CONFIRMATION_PATH`
- `MN_PERMANGANATE_CONFIRMED`
- `NI_CU_CD_ACIDIFIED_PHASE`
- `NI_CONFIRMATION_PATH`
- `NI_DMG_SCARLET_CONFIRMED`
- `CU_CD_SEPARATION_PATH`
- `CU_CD_FERROCYANIDE_STAGE`
- `POST_CU_CD_HCL_SPLIT`
- `CU_CONFIRMED_SOLID_FERROCYANIDE`
- `CD_CANDIDATE_PATH`
- `CD_WHITE_FERROCYANIDE_CONFIRMED`
- `ALKALINE_SOLUTION_PHASE`
- `POST_AMPHOTERIC_SPLIT_POINT`
- `SN_AL_HYDROXIDE_PRECIPITATE_PHASE`
- `ZN_AMMINE_PHASE`
- `POST_SN_AL_HCL_SPLIT`
- `SN_CHLORO_COMPLEX_PATH`
- `SN2_REDUCED_PATH`
- `SN_CONFIRMED_HG2CL2_WHITE`
- `AL3_CONFIRMATION_PATH`
- `AL_CONFIRMED_RED_AL_OH3`
- `ZN_ACIDIFIED_PATH`
- `ZN_CONFIRMED_WHITE_ZN2_FE_CN6`
- `POST_CONC_HCL_SPLIT`
- `CHLORIDE_RESIDUE_PATH_AG_PB_HG`
- `CHLORIDE_DISSOLVED_PATH_SB_PB_COMPLEX`
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
