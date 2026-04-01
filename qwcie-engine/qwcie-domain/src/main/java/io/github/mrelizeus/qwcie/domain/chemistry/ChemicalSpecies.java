package io.github.mrelizeus.qwcie.domain.chemistry;

import java.util.Objects;

public enum ChemicalSpecies {
    AG_PLUS("Ag+"),
    PB_2_PLUS("Pb2+"),
    HG2_2_PLUS("Hg2^2+"),
    CU_2_PLUS("Cu2+"),
    CD_2_PLUS("Cd2+"),
    SB_3_PLUS("Sb3+"),
    SN_2_PLUS("Sn2+"),
    SN_4_PLUS("Sn4+"),
    FE_3_PLUS("Fe3+"),
    AL_3_PLUS("Al3+"),
    CR_3_PLUS("Cr3+"),
    CO_2_PLUS("Co2+"),
    NI_2_PLUS("Ni2+"),
    MN_2_PLUS("Mn2+"),
    MN_4_PLUS("Mn4+"),
    ZN_2_PLUS("Zn2+"),
    BA_2_PLUS("Ba2+"),
    CA_2_PLUS("Ca2+"),
    MG_2_PLUS("Mg2+"),
    K_PLUS("K+"),
    AGCL_SOLID("AgCl(s)"),
    PBCL2_SOLID("PbCl2(s)"),
    HG2CL2_SOLID("Hg2Cl2(s)"),
    SBOCL_SOLID("SbOCl(s)"),
    PBCL4_2_MINUS("PbCl4^2-"),
    FE_OH3_SOLID("Fe(OH)3(s)"),
    MNO2_SOLID("MnO2(s)"),
    CU_OH2_SOLID("Cu(OH)2(s)"),
    CD_OH2_SOLID("Cd(OH)2(s)"),
    NI_OH2_SOLID("Ni(OH)2(s)"),
    NI_TETRAAMMINE_2_PLUS("[Ni(NH3)4]2+"),
    CU_TETRAAMMINE_2_PLUS("[Cu(NH3)4]2+"),
    CD_TETRAAMMINE_2_PLUS("[Cd(NH3)4]2+"),
    ZN_TETRAAMMINE_2_PLUS("[Zn(NH3)4]2+"),
    SN_OH4_SOLID("Sn(OH)4(s)"),
    AL_OH3_SOLID("Al(OH)3(s)"),
    SNCL6_2_MINUS("SnCl6^2-"),
    SNO3_2_MINUS("SnO3^2-"),
    ALO2_MINUS("AlO2-"),
    ZNO2_2_MINUS("ZnO2^2-"),
    FE_PRUSSIAN_BLUE_SOLID("Fe4[Fe(CN)6]3(s)"),
    FE_SCN_BLOOD_RED_COMPLEX("Fe-SCN blood-red complex"),
    MNO4_MINUS("MnO4-"),
    NI_DMG2_SOLID("Ni(C4H7N2O2)2(s)"),
    CU2_FE_CN6_SOLID("Cu2[Fe(CN)6](s)"),
    CD2_FE_CN6_SOLID("Cd2[Fe(CN)6](s)"),
    ZN2_FE_CN6_SOLID("Zn2[Fe(CN)6](s)"),
    HG0_SOLID("Hg0(s)"),
    AG_DIAMMINE_PLUS("[Ag(NH3)2]+"),
    PB_CRO4_SOLID("PbCrO4(s)"),
    SB_LILAC_COMPLEX("Sb lilac complex");

    private final String notation;

    ChemicalSpecies(String notation) {
        this.notation = requireNotation(notation);
    }

    public String notation() {
        return notation;
    }

    @Override
    public String toString() {
        return notation;
    }

    private static String requireNotation(String notation) {
        Objects.requireNonNull(notation, "chemical notation cannot be null");
        String trimmed = notation.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("chemical notation cannot be blank");
        }
        return trimmed;
    }
}
