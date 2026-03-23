package io.github.mrelizeus.qwcie.domain;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class CationCatalog {

    private static final String GROUP_I_CRITERION =
        "Theoretical Fresenius criterion: precipitate as insoluble chlorides with dilute HCl (~3M).";
    private static final String GROUP_II_CRITERION =
        "Theoretical Fresenius criterion: precipitate as sulfides with H2S in acidic medium (~0.3M HCl).";
    private static final String GROUP_III_CRITERION =
        "Theoretical Fresenius criterion: precipitate in ammoniacal medium (NH4OH/NH4Cl) as sulfides or hydroxides.";
    private static final String GROUP_IV_CRITERION =
        "Theoretical Fresenius criterion: precipitate as carbonates with (NH4)2CO3 in NH4OH/NH4Cl buffer.";
    private static final String GROUP_V_CRITERION =
        "No common group precipitant in the standard sequence; ions usually remain soluble at group-separation conditions.";

    private static final Map<Ion, CationMetadata> BY_ION = buildCatalog();

    private CationCatalog() {
    }

    public static CationMetadata byIon(Ion ion) {
        Objects.requireNonNull(ion, "ion cannot be null");

        CationMetadata metadata = BY_ION.get(ion);
        if (metadata == null) {
            throw new IllegalArgumentException("cation not found in catalog: " + ion);
        }
        return metadata;
    }

    public static List<CationMetadata> all() {
        return List.copyOf(BY_ION.values());
    }

    public static List<CationMetadata> byGroup(AnalyticalGroup group) {
        Objects.requireNonNull(group, "group cannot be null");
        return BY_ION
            .values()
            .stream()
            .filter(metadata -> metadata.theoreticalGroup() == group)
            .toList();
    }

    private static Map<Ion, CationMetadata> buildCatalog() {
        EnumMap<Ion, CationMetadata> catalog = new EnumMap<>(Ion.class);

        register(
            catalog,
            Ion.AG_PLUS,
            AnalyticalGroup.GROUP_I,
            GROUP_I_CRITERION,
            "Classical Group I chloride. Modeled in the chloride-first mainline workflow."
        );
        register(
            catalog,
            Ion.PB_2_PLUS,
            AnalyticalGroup.GROUP_I,
            GROUP_I_CRITERION,
            "Mainly separates in Group I as PbCl2; a residual fraction can continue in downstream logic."
        );
        register(
            catalog,
            Ion.HG2_2_PLUS,
            AnalyticalGroup.GROUP_I,
            GROUP_I_CRITERION,
            "Classical Group I chloride (Hg2Cl2)."
        );

        register(
            catalog,
            Ion.CU_2_PLUS,
            AnalyticalGroup.GROUP_II,
            GROUP_II_CRITERION,
            "Theoretical Group II sulfide cation."
        );
        register(
            catalog,
            Ion.CD_2_PLUS,
            AnalyticalGroup.GROUP_II,
            GROUP_II_CRITERION,
            "Theoretical Group II sulfide cation."
        );
        register(
            catalog,
            Ion.SB_3_PLUS,
            AnalyticalGroup.GROUP_II,
            GROUP_II_CRITERION,
            "Theoretical Group II sulfide cation. Depending on chloride/acidity conditions, oxychloride behavior can appear."
        );
        register(
            catalog,
            Ion.SN_2_PLUS,
            AnalyticalGroup.GROUP_II,
            GROUP_II_CRITERION,
            "Tin is tracked in both oxidation states for workflow realism."
        );
        register(
            catalog,
            Ion.SN_4_PLUS,
            AnalyticalGroup.GROUP_II,
            GROUP_II_CRITERION,
            "Tin is tracked in both oxidation states for workflow realism."
        );

        register(
            catalog,
            Ion.FE_3_PLUS,
            AnalyticalGroup.GROUP_III,
            GROUP_III_CRITERION,
            "Group III cation in theoretical sequence."
        );
        register(
            catalog,
            Ion.AL_3_PLUS,
            AnalyticalGroup.GROUP_III,
            GROUP_III_CRITERION,
            "Group III cation in theoretical sequence."
        );
        register(
            catalog,
            Ion.CR_3_PLUS,
            AnalyticalGroup.GROUP_III,
            GROUP_III_CRITERION,
            "Group III cation in theoretical sequence."
        );
        register(
            catalog,
            Ion.CO_2_PLUS,
            AnalyticalGroup.GROUP_III,
            GROUP_III_CRITERION,
            "Group III cation in theoretical sequence."
        );
        register(
            catalog,
            Ion.NI_2_PLUS,
            AnalyticalGroup.GROUP_III,
            GROUP_III_CRITERION,
            "Group III cation in theoretical sequence."
        );
        register(
            catalog,
            Ion.MN_2_PLUS,
            AnalyticalGroup.GROUP_III,
            GROUP_III_CRITERION,
            "Group III cation in theoretical sequence."
        );
        register(
            catalog,
            Ion.ZN_2_PLUS,
            AnalyticalGroup.GROUP_III,
            GROUP_III_CRITERION,
            "Group III cation in theoretical sequence."
        );

        register(
            catalog,
            Ion.BA_2_PLUS,
            AnalyticalGroup.GROUP_IV,
            GROUP_IV_CRITERION,
            "Group IV alkaline-earth cation in classical separation."
        );
        register(
            catalog,
            Ion.CA_2_PLUS,
            AnalyticalGroup.GROUP_IV,
            GROUP_IV_CRITERION,
            "Group IV alkaline-earth cation in classical separation."
        );

        register(
            catalog,
            Ion.MG_2_PLUS,
            AnalyticalGroup.GROUP_V,
            GROUP_V_CRITERION,
            "Theoretical Group V cation; can still precipitate as Mg(OH)2 at sufficiently high pH."
        );
        register(
            catalog,
            Ion.K_PLUS,
            AnalyticalGroup.GROUP_V,
            GROUP_V_CRITERION,
            "Theoretical Group V cation, typically confirmed by specific end-stage tests."
        );

        if (catalog.size() != Ion.values().length) {
            throw new IllegalStateException(
                "Catalog is incomplete: expected " + Ion.values().length + " ions but found " + catalog.size() + "."
            );
        }

        return Map.copyOf(catalog);
    }

    private static void register(
        Map<Ion, CationMetadata> catalog,
        Ion ion,
        AnalyticalGroup group,
        String criterion,
        String notes
    ) {
        Objects.requireNonNull(catalog, "catalog cannot be null");
        Objects.requireNonNull(ion, "ion cannot be null");

        if (catalog.containsKey(ion)) {
            throw new IllegalArgumentException("duplicate catalog entry for ion: " + ion);
        }

        catalog.put(ion, new CationMetadata(ion, group, criterion, notes));
    }
}
