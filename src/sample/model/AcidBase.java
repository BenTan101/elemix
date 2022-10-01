package sample.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public abstract class AcidBase extends Compound implements Balanceable {
    private int ionisation;
    private Compound conjugate;
    private static ArrayList<AcidBase> acidBases;

    protected AcidBase(String name, String formula, int ionisation, String conjugate) {
        super(name, formula);
        this.ionisation = ionisation;
        this.conjugate = new Compound(conjugate);
    }

    public static void initialiseAcidBases() {
        acidBases = new ArrayList<AcidBase>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/AcidsAndBases.csv"));
            String line;
            //[name],[compound],[ionisation] (H+/OH- released),[conjugate base/acid] (charge not indicated)
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens[0].contains("Acid"))
                    acidBases.add(new Acid(tokens[0], tokens[1], Integer.parseInt(tokens[2]), tokens[3]));
                else
                    acidBases.add(new Base(tokens[0], tokens[1], Integer.parseInt(tokens[2]), tokens[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<AcidBase> getAcidBases() {
        ArrayList<AcidBase> deepCopy = new ArrayList<AcidBase>(acidBases.size());
        for (AcidBase ab : acidBases) {
            if (ab instanceof Acid)
                deepCopy.add(new Acid(ab));
            else
                deepCopy.add(new Base(ab));
        }
        return deepCopy;
    }

    protected int getIonisation() {
        return ionisation;
    }

    ;

    protected Compound getConjugate() {
        return new Compound(conjugate);
    }


    public Compound[] neutralise(AcidBase ab) {
        //For a neutralisation reaction, an acid and a base is necessary
        if ((this instanceof Acid && ab instanceof Acid) || (this instanceof Base && ab instanceof Base))
            return null;

        String cationFormula = "";
        String anionFormula = "";
        String saltFormula = "";
        AcidBase base = (this instanceof Base) ? this : ab;
        AcidBase acid = (this instanceof Acid) ? this : ab;

        Compound baseConjugate = base.conjugate;
        baseConjugate.setCharge(base.ionisation);
        Compound acidConjugate = acid.conjugate;
        acidConjugate.setCharge(acid.ionisation);

        balance(baseConjugate, acidConjugate);

        base.setMoles(baseConjugate.getMoles());
        acid.setMoles(acidConjugate.getMoles());

        if (baseConjugate.getMoles() > 1 && baseConjugate.isPolyatomic()) {
            cationFormula += "(" + baseConjugate.getFormula() + ")";
        } else {
            cationFormula += baseConjugate.getFormula();
        }
        if (baseConjugate.getMoles() > 1)
            cationFormula += baseConjugate.getMoles();

        if (acidConjugate.getMoles() > 1 && acidConjugate.isPolyatomic()) {
            anionFormula += "(" + acidConjugate.getFormula() + ")";
        } else {
            anionFormula += acidConjugate.getFormula();
        }
        if (acidConjugate.getMoles() > 1)
            anionFormula += acidConjugate.getMoles();

        saltFormula = cationFormula + anionFormula;
        Compound cation = new Compound(cationFormula);
        Compound salt = new Compound(saltFormula);

        //To balance stoichiometry on both sides.
        int factor = base.getMolesOfElement(base.getFormulaArray().get(0).getSymbol())
                / cation.getMolesOfElement(cation.getFormulaArray().get(0).getSymbol());

        acid.setMoles(acid.getMoles() * factor);
        salt.setMoles(salt.getMoles() * factor);

        //Using mass balance of O
        String oxygen = "O";
        int waterMoles = 0;
        waterMoles += acid.getMolesOfElement(oxygen);
        waterMoles += base.getMolesOfElement(oxygen);
        waterMoles -= salt.getMolesOfElement(oxygen);

        Compound water = new Compound("H2O");
        water.setMoles(waterMoles);
        return new Compound[]{salt, water};
    }
}
