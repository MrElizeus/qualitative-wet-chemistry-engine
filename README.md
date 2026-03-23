# Qualitative Wet Chemistry Inference Engine for Cations and Anions (QWCIE)

Rule-based Java backend for classical wet qualitative analysis of cation/anion identification workflows using a typed graph engine.

## Overview

QWCIE models analytical chemistry procedures as deterministic workflow transitions:

- Nodes represent laboratory states.
- Reaction actions represent reagent additions or branch selections.
- Observed signals represent lab evidence (precipitates, colors, complexes).

The current milestone implements a chloride-first mainline workflow and keeps the architecture ready for Groups II–V and anion workflows.

## Architecture

- **Typed Hybrid Engine**
  - Graph traversal with stable node IDs.
  - Typed transition triggers via `ReactionAction`.
  - Typed outcomes via `TransitionOutcome` and `ObservedSignal`.
- **Cation Domain Catalog**
  - Master ion catalog with oxidation-state-aware cations (`Ion`).
  - Theoretical analytical grouping (`AnalyticalGroup` I-V).
  - Classical separation criteria metadata in `CationCatalog` (for theory reference only).
- **Workflow Modules**
  - `WorkflowDefinition` contract.
  - `GroupIWorkflowDefinition` as first module.
  - `WorkflowRegistry` for multi-workflow expansion.

## Project Structure

```text
qwcie-engine/src/main/java/io/github/mrelizeus/qwcie/
├── cli/
├── domain/
├── engine/
└── workflow/
```

## Tech Stack

- Java 21
- Maven
- JUnit 5

## Run

```bash
cd qwcie-engine
mvn test
mvn exec:java -Dexec.mainClass="io.github.mrelizeus.qwcie.cli.Main"
```

### Interactive step-by-step run (Group I)

```bash
cd qwcie-engine
mvn exec:java -Dexec.mainClass="io.github.mrelizeus.qwcie.cli.GroupIInteractiveCli"
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
- `POST_CHLORIDE_FILTRATE`
- `POST_ALKALINE_OXIDATIVE_SPLIT_POINT`
- `ALKALINE_PRECIPITATE_PHASE`
- `ACID_DISSOLVED_PRECIPITATE_PHASE`
- `ALKALINE_SOLUTION_PHASE`
- `POST_CONC_HCL_SPLIT`
- `GROUP1_RESIDUE_PATH_AG_PB_HG`
- `GROUP1_DISSOLVED_PATH_SB_PB_COMPLEX`
- `HG_BLACK_RESIDUE_AND_AG_AMMINE`
- `AG_CONFIRMED_WHITE_AGCL`
- `PB_CONFIRMED_YELLOW_PBCRO4`
- `SB_CONFIRMED_LILAC_COMPLEX`

## Roadmap

- Add Group II workflow module.
- Add Groups III–V workflow modules.
- Add anion workflow modules.
- Externalize workflow rules (JSON/YAML) without engine rewrite.
- Expose REST API for frontend and integrations.

Author: Eliseo Huetagoyena
