package sample.model;

import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Cation extends Compound implements Balanceable {
    private Color solutionColor;
    private Color precipitateColor;
    private double reductionPotential;
    private Compound reducedForm;
    private boolean isReduced = false;
    private static ArrayList<Cation> cations;

    //CONSTRUCTORS
    public Cation(Cation c) {
        this(c.getName(), c.getFormula(), c.getCharge(), c.getMoles(), c.solutionColor, c.precipitateColor, c.reductionPotential, new Compound(c.reducedForm));
    }

    public Cation(String name, String formula, int charge, Color solutionColor, Color precipitateColor, double reductionPotential, Compound reducedForm) {
        super(name, formula, charge);
        this.solutionColor = solutionColor;
        this.precipitateColor = precipitateColor;
        this.reductionPotential = reductionPotential;
        this.reducedForm = reducedForm;
    }

    public Cation(String name, String formula, int charge, int moles, Color solutionColor, Color precipitateColor, double reductionPotential, Compound reducedForm) {
        super(name, formula, charge, moles);
        this.solutionColor = solutionColor;
        this.precipitateColor = precipitateColor;
        this.reductionPotential = reductionPotential;
        this.reducedForm = reducedForm;
    }


    public static void initialiseCations() {
        cations = new ArrayList<Cation>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/Cations.csv"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                //[name],[compound],[charge],[solution colour] rr,gg,bb,[precipitate colour] rr,gg,bb,[reduction potential]
                cations.add(new Cation(tokens[0], tokens[1], Integer.parseInt(tokens[2]),
                        Color.color(Integer.parseInt(tokens[3]) / 255.0, Integer.parseInt(tokens[4]) / 255.0, Integer.parseInt(tokens[5]) / 255.0),
                        Color.color(Integer.parseInt(tokens[6]) / 255.0, Integer.parseInt(tokens[7]) / 255.0, Integer.parseInt(tokens[8]) / 255.0), Double.parseDouble(tokens[9]),
                        //name, formula, molecular mass
                        new Compound(tokens[0], tokens[10])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //GETTERS
    public Color getSolutionColor() {
        return solutionColor;
    }

    public Color getPrecipitateColor() {
        return precipitateColor;
    }

    public String getSolutionHex() {
        return String.format("#%02x%02x%02x", (int) (solutionColor.getRed() * 255), (int) (solutionColor.getGreen() * 255), (int) (solutionColor.getBlue() * 255));
    }

    public String getPrecipitateHex() {
        return String.format("#%02x%02x%02x", (int) (precipitateColor.getRed() * 255), (int) (precipitateColor.getGreen() * 255), (int) (precipitateColor.getBlue() * 255));
    }

    public static ArrayList<Cation> getCations() {
        ArrayList<Cation> deepCopy = new ArrayList<>(cations.size());
        for (Cation c : cations) {
            deepCopy.add(new Cation(c));
        }
        return deepCopy;
    }

    @Override
    public String getFormattedFormula() {
        String formattedCharge = "";

        if (!isReduced) {
            if (getCharge() == 1) {
                formattedCharge += SUPERSCRIPT_PLUS;
            } else if (getCharge() == -1) {
                formattedCharge += SUPERSCRIPT_MINUS;
            } else if (getCharge() != 0) {
                String stringCharge = getCharge() + "";
                for (int i = 0; i < stringCharge.length(); i++) {
                    if (Character.isDigit(stringCharge.charAt(i)))
                        formattedCharge += SUPERSCRIPT_NUMBERS[Integer.parseInt(stringCharge.charAt(i) + "")];
                }
                formattedCharge += (getCharge() > 0) ? SUPERSCRIPT_PLUS : SUPERSCRIPT_MINUS;
            }
        }

        String formattedFormula = "";
        for (int i = 0; i < getFormula().length(); i++) {
            if (Character.isDigit(getFormula().charAt(i))) {
                formattedFormula += SUBSCRIPT_NUMBERS[Integer.parseInt(getFormula().charAt(i) + "")];
            } else {
                formattedFormula += getFormula().charAt(i);
            }
        }

        return formattedFormula + formattedCharge;
    }

    public Cation getReducedForm() {
        Cation copyCation = new Cation(this);
        copyCation.setReduced(true);
        return copyCation;
    }

    public boolean isReduced() {return isReduced;}

    //SETTERS
    private void setReduced(boolean isReduced) {
        this.isReduced = isReduced;
    }

    public Cation[] displace(Cation c) {
        //i.e. c is (aq), this is (s)
        if (this.isReduced && !c.isReduced) {
            if (c.reductionPotential > this.reductionPotential) {
                balance(this, c);
                Cation reducedCation = new Cation(c);
                reducedCation.setReduced(true);
                return new Cation[]{new Cation(this), reducedCation};
            }
        } // i.e. c is (s), this is (aq)
        else if (!this.isReduced && c.isReduced) {
            balance(this, c);
            Cation reducedCation = new Cation(this);
            reducedCation.setReduced(true);
            return new Cation[]{reducedCation, new Cation(c)};
        }
        //displacement reaction does not occur
        return null;
    }

    @Override
    public String toString() {
        if (isReduced) {
            return reducedForm.toString();
        } else {
           return super.toString();
        }
    }
}
