package sample.model;

import java.util.*;

public class Compound {
    private String name = "";
    private String formula = "";
    private double molecularMass = 0;
    private int charge = 0;
    private int moles = 1;
    private HashMap<String, Integer> formulaHashMap = new HashMap<String, Integer>();
    private ArrayList<Element> formulaArray = new ArrayList<Element>();
    private ArrayList<String> constituents;

    //FORMULA PARSING CONSTANTS
    private final String[] OPEN_BRACKETS = new String[]{"(", "["};
    private final String[] CLOSED_BRACKETS = new String[]{")", "]"};
    protected final String VALID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890()[]+-";
    protected final char[] SUPERSCRIPT_NUMBERS = new char[]{'⁰', '¹', '²', '³', '⁴', '⁵', '⁶', '⁷', '⁸', '⁹'};
    protected final char SUPERSCRIPT_PLUS = '⁺';
    protected final char SUPERSCRIPT_MINUS = '⁻';
    protected final char[] SUBSCRIPT_NUMBERS = new char[]{'₀', '₁', '₂', '₃', '₄', '₅', '₆', '₇', '₈', '₉'};

    //CONSTRUCTORS
    public Compound() {
    }

    public Compound(Compound c) {
        this(c.name, c.formula, c.charge, c.moles);
    }

    public Compound(String formula) {
        this.formula = formula;
        parseFormula(formula);
    }

    public Compound(String name, String formula) {
        this(formula);
        this.name = name;
    }

    public Compound(String name, String formula, int charge) {
        this(name, formula);
        this.charge = charge;
    }

    public Compound(String name, String formula, int charge, int moles) {
        this(name, formula, charge);
        this.moles = moles;
    }

    //CHARGE PARSING
    private boolean isCharged(String formula) {
        return formula.charAt(formula.length() - 1) == '+' || formula.charAt(formula.length() - 1) == '-';
    }

    private void parseCharge(String formula) {
        boolean isPositive;
        isPositive = formula.charAt(formula.length() - 1) == '+';

        if (isCharged(formula)) {
            formula = formula.substring(0, formula.length() - 1);
            try {
                String chargeMagnitude = "";
                while (Character.isDigit(formula.charAt(formula.length() - 1))) {
                    chargeMagnitude = formula.charAt(formula.length() - 1) + chargeMagnitude;
                    formula = formula.substring(0, formula.length() - 1);
                }
                charge = (isPositive) ? Integer.parseInt(chargeMagnitude) : -Integer.parseInt(chargeMagnitude);
            } catch (NumberFormatException e) {
                charge = (isPositive) ? 1 : -1;
            }
        }
    }

    //FORMULA PARSING
    private void parseFormula(String formula) {
        parseCharge(formula);
        if (isCharged(formula)) {
            formula = formula.substring(0, formula.length() - 1);
            if (Math.abs(charge) != 1)
                formula = formula.substring(0, formula.length() - (Math.abs(charge) + "").length());
        }
        this.formula = formula;
        toConstituents(formula);
        removeAllBrackets();
    }

    //Gets next element
    private String getNextElement(String formula) {
        if (formula.length() == 1)
            return formula.substring(0, 1);
        if ((formula.charAt(1) + "").matches("[a-z]")) {
            return formula.substring(0, 2);
        } else
            return formula.substring(0, 1);
    }

    //Gets next coefficient
    private String getNextCoefficient(String formula) {
        String placeholderMoles = "-";
        while (formula.length() > 0 && Character.isDigit(formula.charAt(0))) {
            if (placeholderMoles.equals("-"))
                placeholderMoles = "";

            placeholderMoles += formula.charAt(0);
            formula = formula.substring(1);
        }
        if (placeholderMoles.equals("-"))
            return "1";
        else
            return placeholderMoles;
    }

    private String getNextCoefficient(List<String> constituents) {
        if (!Character.isDigit((constituents.get(0)).charAt(0)))
            return "1";
        else
            return constituents.get(0);
    }

    //Splits String formula into chars e.g. Ca(OH)2 -> ["Ca", "(", "O", "H", ")", "2"]
    private void toConstituents(String formula) {
        constituents = new ArrayList<String>();
        for (int i = 0; i < formula.length(); i++) {
            if (Character.isLetter(formula.charAt(i))) {
                constituents.add(getNextElement(formula.substring(i)));
                i += getNextElement(formula.substring(i)).length() - 1;
            } else if (Character.isDigit(formula.charAt(i))) {
                constituents.add(getNextCoefficient(formula.substring(i)));
                i += getNextCoefficient(formula.substring(i)).length() - 1;
            } else {
                constituents.add(formula.charAt(i) + "");
            }
        }
    }

    //BRACKET PARSING
    public boolean isBracket(String bracket) {
        return isOpenBracket(bracket) || isClosedBracket(bracket);
    }

    private boolean isOpenBracket(String bracket) {
        boolean isOpenBracket = false;
        for (String b : OPEN_BRACKETS) {
            isOpenBracket = b.equals(bracket);
            if (isOpenBracket)
                return true;
        }
        return false;
    }

    private boolean isClosedBracket(String bracket) {
        boolean isClosedBracket = false;
        for (String b : CLOSED_BRACKETS) {
            isClosedBracket = b.equals(bracket);
            if (isClosedBracket)
                return true;
        }

        return false;
    }

    private String getOppositeBracket(String bracket) {
        String opposite = "";
        if (isOpenBracket(bracket)) {
            for (int i = 0; i < OPEN_BRACKETS.length; i++) {
                if (OPEN_BRACKETS[i].equals(bracket))
                    opposite = CLOSED_BRACKETS[i];
            }
        } else {
            for (int i = 0; i < CLOSED_BRACKETS.length; i++) {
                if (CLOSED_BRACKETS[i].equals(bracket))
                    opposite = OPEN_BRACKETS[i];
            }
        }
        return opposite;
    }

    //Inner refers to portion within brackets
    private ArrayList<String> getInnerPortion(int index) {
        List<String> splicedConstituents = constituents.subList(index, constituents.size());
        ArrayList<String> portionInBrackets = new ArrayList<String>();
        int bracketIndex = 3;
        for (int b = 0; b < OPEN_BRACKETS.length; b++) {
            if (OPEN_BRACKETS[b].equals(splicedConstituents.get(0)))
                bracketIndex = b;
        }
        int splicedIndex = 1;
        while (!splicedConstituents.get(splicedIndex).equals(CLOSED_BRACKETS[bracketIndex])) {
            portionInBrackets.add(splicedConstituents.get(splicedIndex));
            splicedIndex++;
        }
        return portionInBrackets;
    }

    private String getInnerCoefficient(int index) {
        String innerCoefficient = "1";
        try {
            List<String> subConstituents = constituents.subList(index, constituents.size());
            innerCoefficient = getNextCoefficient(subConstituents.subList(getInnerPortion(index).size() + 2, subConstituents.size()));
        } catch (IndexOutOfBoundsException e) {
            return innerCoefficient;
        }
        return innerCoefficient;
    }

    //Main parser that helps to remove all the brackets
    private void removeAllBrackets() {
        addCoefficientsOfOne();
        int index = 0;
        while (index < constituents.size()) {
            if (isOpenBracket(constituents.get(index))) {
                String openBracket = constituents.get(index) + "";
                int innerCoefficient = Integer.parseInt(getInnerCoefficient(index));

                boolean inBracket = false;
                for (int i = index + 1; i < getInnerPortion(index).size() + index + 1; i++) {
                    if (isBracket(constituents.get(i)))
                        inBracket = !inBracket;
                    if (!inBracket && Character.isDigit(constituents.get(i).charAt(0)))
                        constituents.set(i, Integer.parseInt(constituents.get(i)) * innerCoefficient + "");
                }

                //change brackets to "", which will be removed later
                int indexOfOppositeBracket = constituents.indexOf(getOppositeBracket(openBracket));
                constituents.set(indexOfOppositeBracket, "|");

                if (innerCoefficient != 1)
                    constituents.set(indexOfOppositeBracket + 1, "|");

                constituents.set(index, "|");
            }
            index++;
        }
        constituents.removeAll(Collections.singleton("|"));
        for (int i = 0; i < constituents.size() - 1; i++) {
            if (Character.isLetter(constituents.get(i).charAt(0))) {
                addElementAndMolecularMass(constituents.get(i), Integer.parseInt(constituents.get(i + 1)));
            }
        }
    }

    //transforms [N, H, 4] to [N, 1, H, 4] to aid in parsing
    private void addCoefficientsOfOne() {
        if (Character.isLetter(constituents.get(constituents.size() - 1).charAt(0)))
            constituents.add("1");
        for (int i = 0; i < constituents.size() - 1; i++) {
            if (Character.isLetter(constituents.get(i).charAt(0)) &&
                    !(Character.isDigit(constituents.get(i + 1).charAt(0))))
                constituents.add(i + 1, "1");
        }
    }

    private void addElementAndMolecularMass(String atom, int moles) {
        Element e = new Element(atom, moles);
        formulaArray.add(e);
        formulaHashMap.put(e.getSymbol(), e.getMoles());
        molecularMass += e.getAtomicMass() * moles;
    }

    public boolean isValidCompound(String formula) {
        //if no capital letters at all, which would be impossible.
        if (formula.matches("[a-z()\\[\\]0-9]+")) return false;

        //Checks for correct bracket placement. Also ensures that brackets placed are necessary always.

        String singleBracketRegex = "([a-zA-Z0-9]*\\([A-Z][a-z]?[0-9]?[a-zA-Z0-9]*\\)[a-zA-Z0-9]*)";
        String doubleBracketRegex = "([a-zA-Z0-9]*\\[" + singleBracketRegex + "+\\][a-zA-Z0-9]*)";

        if (!formula.matches("[a-zA-Z0-9]+")) {
            if (!formula.matches(singleBracketRegex + "*"
                    + doubleBracketRegex + "*"
                    + singleBracketRegex + "*[-+]?")) return false;
        }

        parseCharge(formula);
        String chargelessFormula = formula.replaceAll("[0-9]*[-+]$", "");
        toConstituents(chargelessFormula);
        removeAllBrackets();

        for (int i = 0; i < chargelessFormula.length(); i++) {
            if (VALID_CHARACTERS.indexOf(chargelessFormula.charAt(i)) == -1) {
                return false;
            }
        }
        try {
            Integer.parseInt(constituents.get(0));
            return false;
        } catch (NumberFormatException e) {
        }

        boolean containsValidElement = false;
        for (String s : constituents) {
            if (Character.isLetter(s.charAt(0))) {
                containsValidElement = false;
                for (Element e : Element.getPeriodicTable()) {
                    if (e.getSymbol().equals(s)) {
                        containsValidElement = true;
                        break;
                    }
                }
                if (!containsValidElement) return false;
            } else {
                try {
                    int integer = Integer.parseInt(s);
                    if (integer == 0) return false;
                } catch (NumberFormatException e) {
                    //i.e. non-alphanumerical
                    return false;
                }
            }
        }

        return true;
    }

    //i.e. (Hg2)2+, (PO4)3-
    public boolean isPolyatomic() {
        if (!(formulaHashMap.size() == 1))
            return true;
        return formulaArray.get(0).getMoles() > 1;
    }

    //GETTERS
    public String getName() {
        return name;
    }

    public String getFormula() {
        return formula;
    }

    public double getMolecularMass() {
        return Double.parseDouble(String.format("%.3f", molecularMass));
    }

    public int getCharge() {
        return charge;
    }

    public int getMoles() {
        return moles;
    }

    public ArrayList<Element> getFormulaArray() {
        ArrayList<Element> deepCopy = new ArrayList<Element>(formulaArray.size());
        for (Element e : formulaArray) {
            deepCopy.add(new Element(e));
        }
        return deepCopy;
    }

    public String getFormulaWithCharge() {
        if (charge == 0)
            return formula;
        if (charge == 1)
            return formula + "+";
        if (charge == -1)
            return formula + "-";
        return formula + Math.abs(charge) + ((charge > 0) ? "+" : "-");
    }

    public String getFormattedFormula() {
        String formattedCharge = "";

        if (charge == 1) {
            formattedCharge += SUPERSCRIPT_PLUS;
        } else if (charge == -1) {
            formattedCharge += SUPERSCRIPT_MINUS;
        } else if (charge != 0) {
            String stringCharge = charge + "";
            for (int i = 0; i < stringCharge.length(); i++) {
                if (Character.isDigit(stringCharge.charAt(i)))
                    formattedCharge += SUPERSCRIPT_NUMBERS[Integer.parseInt(stringCharge.charAt(i) + "")];
            }
            formattedCharge += (charge > 0) ? SUPERSCRIPT_PLUS : SUPERSCRIPT_MINUS;
        }

        String formattedFormula = "";
        for (int i = 0; i < formula.length(); i++) {
            if (Character.isDigit(formula.charAt(i))) {
                formattedFormula += SUBSCRIPT_NUMBERS[Integer.parseInt(formula.charAt(i) + "")];
            } else {
                formattedFormula += formula.charAt(i);
            }
        }

        return formattedFormula + formattedCharge;
    }

    public String getFormattedFormulaWithMoles() {
        return (moles == 1) ? getFormattedFormula() : moles + " " + getFormattedFormula();
    }

    public int getMolesOfElement(String symbol) {
        return (formulaHashMap.get(symbol) == null) ? 0 : moles * formulaHashMap.get(symbol);
    }

    //SETTERS
    public void setMoles(int moles) {
        this.moles = moles;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFormula(String formula) {
        this.formula = formula;
        molecularMass = 0;
        parseFormula(formula);
    }

    @Override
    public String toString() {
        return getFormattedFormula();
    }
}
