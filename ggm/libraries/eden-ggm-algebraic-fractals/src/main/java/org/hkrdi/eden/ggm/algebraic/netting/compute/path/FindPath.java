package org.hkrdi.eden.ggm.algebraic.netting.compute.path;

import org.hkrdi.eden.ggm.algebraic.Connector;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class FindPath<T> {

	public List<List<T>> findPath(final T source, T dest, List<Connector> connectors){
		List<Store<T>> result = Collections.synchronizedList(new ArrayList<Store<T>>());
		connectors.parallelStream().filter(p->source.equals(p.getHead().getIndex())).forEach(c->{
			Store<T> r = new Store<T>(0);
			r.add(source);
			r.add((T)new Integer(c.getTail().getIndex()));
			result.add(r);
		});
		findPath(source, dest, connectors, result, 1);
		return result.stream().filter(p->dest.equals(p.last)).map(m->m.getAsList()).collect(Collectors.toList());
	}
	
	private boolean isEqual(LinkedHashSet c1, LinkedHashSet c2) {
		return c1.size()==c2.size() && c1.containsAll(c2);
	}
	
	private void findPath(T source, T dest, List<Connector> connectors, List<Store<T>> result, int depth){
		Arrays.asList(result.toArray(new Store[0])).stream().filter(f-> f!= null && !dest.equals(f.getLast())).
			forEach(s->{
				T last = (T)s.getLast();
				LinkedHashSet<T> set = s.getSet();
				AtomicBoolean atLeastOne = new AtomicBoolean(false);
				connectors.stream().filter(
						p->last.equals(p.getHead().getIndex()) && 
						!set.contains(p.getTail().getIndex())).forEach(c->{
					Store<T> r = new Store<T>(depth);
					r.getSet().addAll(s.getSet());
					r.add((T)new Integer(c.getTail().getIndex()));
					if (Arrays.asList(result.toArray(new Store[0])).stream().filter(d->isEqual(d.getSet(), r.getSet()) ).count() == 0) {
						result.add(r);
						atLeastOne.set(true);
					}
				});
				if (atLeastOne.get()) {
					result.remove(s);
				}
			});
		
		if (result.stream().filter(p->
				p.getLastDepthUpdated()< depth || dest.equals(p.getLast())
			).count() == result.size()) {
			return;
		}
		findPath(source, dest, connectors, result, depth+1);
	}
	
	public class Store<T>{
		LinkedHashSet<T> set = new LinkedHashSet<T>();
		T last;
		int lastDepthUpdated;
		
		public Store(int lastDepthUpdated) {
			this.lastDepthUpdated = lastDepthUpdated;
		}
		
		public LinkedHashSet<T> getSet() {
			return set;
		}
		
		public List<T> getAsList(){
			return set.stream().collect(Collectors.toList());
		}

		public T getLast() {
			return last;
		}

		public synchronized void add(T value) {
			last = value;
			set.add(value);
		}
		
		public int getLastDepthUpdated() {
			return lastDepthUpdated;
		}

		public void setLastDepthUpdated(int lastDepthUpdated) {
			this.lastDepthUpdated = lastDepthUpdated;
		}

	}
}
