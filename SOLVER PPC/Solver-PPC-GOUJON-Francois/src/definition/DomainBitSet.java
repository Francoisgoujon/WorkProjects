package definition;
import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

//import definition.DomainBitSet.LexicoIterator;


public class DomainBitSet implements Domain {

    BitSet values; // un bitset represente le domaine (voir api java pour plus d'infos)
    int min;
    int max;
    Iterator<Integer> iterateur;

    // Construit un domaine dont les valeurs sont comprises entre min et max (inclus)
    public DomainBitSet(int min, int max) {
        this.values = new BitSet();
        this.values.set(min,max+1);
        this.min = min;
        this.max = max;
    }

    public DomainBitSet(DomainBitSet dom) {
        this.values = (BitSet) dom.values.clone();
        this.min = dom.min;
        this.max = dom.max;
    }

    public DomainBitSet clone() {
        return new DomainBitSet(this);
    }

    // retourne la taille du domaine, ie le nombre de valeurs dans le domaine
    public int size() {
        return this.values.cardinality();
    }

    // retourne la premiere valeur du domaine
    public int firstValue() {
        return values.nextSetBit(min);
    }

    // retourne la derniere valeur du domaine
    public int lastValue() {
        return values.previousSetBit(max);
    }

    public String toString() {
        return this.values.toString();
    }

    public Iterator<Integer> iterator() {
        return new LexicoIterator(this);
    }

    public boolean contains(int v) {
        return values.get(v);
    }

    public void remove(int v) {
        values.clear(v);
    }

    public void remove(int mini, int maxi) {
        values.clear(mini, maxi+1);
    }

    public void removeAll() {
        values.clear();
    }

    public void instantiate(int v) {
        values.clear();
        values.set(v);;
    }

}

class LexicoIterator implements Iterator<Integer>{
    int idx;
    DomainBitSet dom;

    public LexicoIterator(DomainBitSet dom) {
        this.dom = dom;
        this.idx = dom.min;
    }

    public boolean hasNext() {
        return dom.values.nextSetBit(idx) != -1 ;
    }

    public Integer next() {
        int nextIndex = dom.values.nextSetBit(idx);
        if (nextIndex != -1) {
            idx = nextIndex+1;
            return nextIndex;
        }
        throw new NoSuchElementException();
    }

    public String toString() {
        return ""+idx+ ", "+hasNext();
    }

}


