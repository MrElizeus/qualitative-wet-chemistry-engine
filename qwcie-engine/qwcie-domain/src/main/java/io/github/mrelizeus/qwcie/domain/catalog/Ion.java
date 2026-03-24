package io.github.mrelizeus.qwcie.domain.catalog;

public enum Ion {
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
    ZN_2_PLUS("Zn2+"),
    BA_2_PLUS("Ba2+"),
    CA_2_PLUS("Ca2+"),
    MG_2_PLUS("Mg2+"),
    K_PLUS("K+");

    private final String formula;

    Ion(String formula) {
        this.formula = formula;
    }

    public String formula() {
        return formula;
    }

    @Override
    public String toString() {
        return formula;
    }
}
