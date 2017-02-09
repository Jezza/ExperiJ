package me.jezza.experij;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

import me.jezza.experij.interfaces.Results;

/**
 * @author Jezza
 */
final class ExperimentResults extends ArrayList<Execution> {
	private transient ImmutableView immutableView;

	ExperimentResults(String experimentName) {
		if (ExperiJ.DEBUG)
			System.out.println("Creating results for '" + experimentName + '\'');
	}

	Results immutable() {
		return immutableView != null ? immutableView : (immutableView = new ImmutableView(this));
	}

	private static class ImmutableView implements Results {
		private final List<Execution> results;

		ImmutableView(List<Execution> results) {
			this.results = results;
		}

		@Override
		public int size() {
			return results.size();
		}

		@Override
		public boolean isEmpty() {
			return results.isEmpty();
		}

		@Override
		public Execution get(int index) {
			return results.get(index);
		}

		@Override
		public int indexOf(Execution o) {
			return results.indexOf(o);
		}

		@Override
		public int lastIndexOf(Execution o) {
			return results.lastIndexOf(o);
		}

		public Iterator<Execution> iterator() {
			return new Iterator<Execution>() {
				private final Iterator<? extends Execution> i = results.iterator();

				public boolean hasNext() {
					return i.hasNext();
				}

				public Execution next() {
					return i.next();
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}

				@Override
				public void forEachRemaining(Consumer<? super Execution> action) {
					// Use backing collection version
					i.forEachRemaining(action);
				}
			};
		}

		@Override
		public ListIterator<Execution> listIterator(int index) {
			return new ListIterator<Execution>() {
				private final ListIterator<? extends Execution> i = results.listIterator(index);

				public boolean hasNext() {
					return i.hasNext();
				}

				public Execution next() {
					return i.next();
				}

				public boolean hasPrevious() {
					return i.hasPrevious();
				}

				public Execution previous() {
					return i.previous();
				}

				public int nextIndex() {
					return i.nextIndex();
				}

				public int previousIndex() {
					return i.previousIndex();
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}

				public void set(Execution e) {
					throw new UnsupportedOperationException();
				}

				public void add(Execution e) {
					throw new UnsupportedOperationException();
				}

				@Override
				public void forEachRemaining(Consumer<? super Execution> action) {
					i.forEachRemaining(action);
				}
			};
		}

		@Override
		public Results subList(int fromIndex, int toIndex) {
			return new ImmutableView(results.subList(fromIndex, toIndex));
		}

		@Override
		public Execution[] toArray() {
			return results.toArray(new Execution[0]);
		}

		@Override
		public <T> T[] toArray(T[] a) {
			return results.toArray(a);
		}

		@Override
		public String toString() {
			return results.toString();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			ImmutableView that = (ImmutableView) o;
			return results.equals(that.results);
		}

		@Override
		public int hashCode() {
			return results.hashCode();
		}
	}
}
