package sample.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Element {
    private int atomicNumber = 0;
    private String name = "";
    private String symbol = "";
    private double atomicMass = 0;
    private int moles = 1;

    public static ArrayList<Element> periodicTable;

    public static void initialisePeriodicTable() {
        periodicTable = new ArrayList<Element>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/Elements.csv"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                //atomic number, name, symbol, atomic mass, state
                periodicTable.add(new Element(Integer.parseInt(tokens[0]), tokens[1], tokens[2], Double.parseDouble(tokens[3])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //CONSTRUCTORS
    public Element(int atomicNumber, String name, String symbol, double atomicMass) {
        this.atomicNumber = atomicNumber;
        this.name = name;
        this.symbol = symbol;
        this.atomicMass = atomicMass;
    }

    public Element(String symbol) {
        setAtomicNumber(symbol);
        setName(symbol);
        this.symbol = symbol;
        setAtomicMass(symbol);
    }

    public Element(String symbol, int moles) {
        this(symbol);
        this.moles = moles;
    }

    public Element(Element e) {
        this(e.symbol, e.moles);
    }

    //GETTERS
    public static ArrayList<Element> getPeriodicTable() {
        ArrayList<Element> deepCopy = new ArrayList<Element>();
        for (Element e : periodicTable) {
            deepCopy.add(new Element(e));
        }
        return deepCopy;
    }

    public double getAtomicMass() {
        return atomicMass;
    }

    public int getMoles() {
        return moles;
    }

    public int getAtomicNumber() {
        return atomicNumber;
    }

    //SETTERS
    public void setAtomicNumber(String symbol) {
        for (Element e : periodicTable) {
            if (e.getSymbol().equals(symbol))
                atomicNumber = e.atomicNumber;
        }
    }

    public void setName(String symbol) {
        for (Element e : periodicTable) {
            if (e.getSymbol().equals(symbol))
                name = e.name;
        }
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setAtomicMass(String symbol) {
        for (Element e : periodicTable) {
            if (e.getSymbol().equals(symbol)) {
                atomicMass = e.atomicMass;
            }
        }
    }

    @Override
    public String toString() { return symbol;}
}
