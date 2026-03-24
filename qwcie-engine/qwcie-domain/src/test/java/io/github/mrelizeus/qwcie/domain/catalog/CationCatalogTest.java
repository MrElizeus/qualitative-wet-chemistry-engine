package io.github.mrelizeus.qwcie.domain.catalog;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CationCatalogTest {

    @Test
    void containsAllIonsDefinedInDomainEnum() {
        assertEquals(Ion.values().length, CationCatalog.all().size());

        for (Ion ion : Ion.values()) {
            CationMetadata metadata = CationCatalog.byIon(ion);
            assertEquals(ion, metadata.ion());
        }
    }

    @Test
    void mapsRepresentativeIonsToExpectedTheoreticalGroups() {
        assertEquals(AnalyticalGroup.GROUP_I, CationCatalog.byIon(Ion.AG_PLUS).theoreticalGroup());
        assertEquals(AnalyticalGroup.GROUP_II, CationCatalog.byIon(Ion.CU_2_PLUS).theoreticalGroup());
        assertEquals(AnalyticalGroup.GROUP_III, CationCatalog.byIon(Ion.FE_3_PLUS).theoreticalGroup());
        assertEquals(AnalyticalGroup.GROUP_IV, CationCatalog.byIon(Ion.BA_2_PLUS).theoreticalGroup());
        assertEquals(AnalyticalGroup.GROUP_V, CationCatalog.byIon(Ion.K_PLUS).theoreticalGroup());
    }

    @Test
    void includesExpectedGroupIIMembers() {
        Set<Ion> expectedGroupII = EnumSet.of(
            Ion.CU_2_PLUS,
            Ion.CD_2_PLUS,
            Ion.SB_3_PLUS,
            Ion.SN_2_PLUS,
            Ion.SN_4_PLUS
        );

        Set<Ion> actualGroupII = CationCatalog
            .byGroup(AnalyticalGroup.GROUP_II)
            .stream()
            .map(CationMetadata::ion)
            .collect(java.util.stream.Collectors.toSet());

        assertEquals(expectedGroupII, actualGroupII);
    }

    @Test
    void groupTwoCriterionMentionsTheoreticalH2sReference() {
        String criterion = CationCatalog.byIon(Ion.CU_2_PLUS).theoreticalSeparationCriterion();
        assertTrue(criterion.contains("H2S"));
    }
}
