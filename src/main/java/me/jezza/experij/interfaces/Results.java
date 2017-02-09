package me.jezza.experij.interfaces;

import java.util.Iterator;
import java.util.ListIterator;

import me.jezza.experij.Execution;

/**
 * @author Jezza
 */
public interface Results extends Iterable<Execution> {
	int size();

	boolean isEmpty();

	Execution get(int index);

	int indexOf(Execution o);

	int lastIndexOf(Execution o);

	@Override
	Iterator<Execution> iterator();

	default ListIterator<Execution> listIterator() {
		return listIterator(0);
	}

	ListIterator<Execution> listIterator(int index);

	Results subList(int fromIndex, int toIndex);

	Execution[] toArray();

	<T> T[] toArray(T[] a);
}
