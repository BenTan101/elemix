package sample.model;

import java.util.ArrayList;

public class Acid extends AcidBase {

    //CONSTRUCTORS
    public Acid(AcidBase ab) {
        super(ab.getName(), ab.getFormula(), ab.getIonisation(), ab.getConjugate().getFormula());
        getConjugate().setCharge(-getIonisation());
    }

    public Acid(String name, String formula, int ionisation, String conjugate) {
        super(name, formula, ionisation, conjugate);
        this.getConjugate().setCharge(-ionisation);
    }

    //GETTER
    public static ArrayList<AcidBase> getAcids() {
        ArrayList<AcidBase> acids = new ArrayList<AcidBase>();
        for (AcidBase ab : getAcidBases()) {
            if (ab instanceof Acid)
                acids.add(ab);
        }
        return acids;
    }
}
