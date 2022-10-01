package sample.model;

import java.util.ArrayList;

public class Base extends AcidBase {

    //CONSTRUCTORS
    public Base(AcidBase ab) {
        super(ab.getName(), ab.getFormula(), ab.getIonisation(), ab.getConjugate().getFormula());
        this.getConjugate().setCharge(getIonisation());
    }

    public Base(String name, String formula, int ionisation, String conjugate) {
        super(name, formula, ionisation, conjugate);
        this.getConjugate().setCharge(ionisation);
    }

    //GETTER
    public static ArrayList<AcidBase> getBases() {
        ArrayList<AcidBase> bases = new ArrayList<AcidBase>();
        for (AcidBase ab : getAcidBases()) {
            if (ab instanceof Base)
                bases.add(ab);
        }
        return bases;
    }
}
