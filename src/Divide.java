//Klasa implementująca interfejs IArithmeticDiv
public class Divide implements IArithmeticDiv {
    // implementacja metody Division z interfejsu IArithmeticDiv
    // Dzielenie przez zero bez obsługi wyjątku O.o
    @Override
    public double Division(double A, double B) {
        return A / B;
    }
}
