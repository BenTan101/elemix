package sample.model;

public interface Balanceable {
    default void balance(Compound compound1, Compound compound2) {
        int LCM = getLCM(Math.abs(compound1.getCharge()), Math.abs(compound2.getCharge()));
        compound1.setMoles(LCM / Math.abs(compound1.getCharge()));
        compound2.setMoles(LCM / Math.abs(compound2.getCharge()));
    }

    //Finds LCM of charges to balance equations.
    default int getLCM(int n1, int n2) {
        int lcm = Math.max(n1, n2);

        while (lcm % n1 != 0 || lcm % n2 != 0) {
            lcm++;
        }
        return lcm;
    }
}
