package expression.generic.calculators;

@FunctionalInterface
public interface Converter<T> {
    T convert(String string);
}
