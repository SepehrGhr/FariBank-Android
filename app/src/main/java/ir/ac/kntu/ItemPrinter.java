package ir.ac.kntu;

@FunctionalInterface
public interface ItemPrinter<T> {
    void printItem(T item);
}

