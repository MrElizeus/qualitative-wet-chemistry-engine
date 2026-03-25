package io.github.mrelizeus.qwcie.domain.catalog;

import io.github.mrelizeus.qwcie.domain.chemistry.ChemicalSpecies;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DomainValueObjectsTest {

    @Test
    void ionToStringMatchesItsFormula() {
        for (Ion ion : Ion.values()) {
            assertEquals(ion.formula(), ion.toString());
        }
    }

    @Test
    void chemicalSpeciesToStringMatchesNotation() {
        for (ChemicalSpecies species : ChemicalSpecies.values()) {
            assertEquals(species.notation(), species.toString());
        }
    }

    @Test
    void cationMetadataRejectsBlankTextFields() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new CationMetadata(Ion.AG_PLUS, AnalyticalGroup.GROUP_I, "   ", "valid note")
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new CationMetadata(Ion.AG_PLUS, AnalyticalGroup.GROUP_I, "valid criterion", "   ")
        );
    }

    @Test
    void cationMetadataRejectsNullCriticalFields() {
        assertThrows(
            NullPointerException.class,
            () -> new CationMetadata(null, AnalyticalGroup.GROUP_I, "criterion", "notes")
        );
        assertThrows(
            NullPointerException.class,
            () -> new CationMetadata(Ion.AG_PLUS, null, "criterion", "notes")
        );
        assertThrows(
            NullPointerException.class,
            () -> new CationMetadata(Ion.AG_PLUS, AnalyticalGroup.GROUP_I, null, "notes")
        );
        assertThrows(
            NullPointerException.class,
            () -> new CationMetadata(Ion.AG_PLUS, AnalyticalGroup.GROUP_I, "criterion", null)
        );
    }

    @Test
    void cationCatalogDefensiveGuardsRejectNullInputs() {
        assertThrows(NullPointerException.class, () -> CationCatalog.byIon(null));
        assertThrows(NullPointerException.class, () -> CationCatalog.byGroup(null));
    }
}
