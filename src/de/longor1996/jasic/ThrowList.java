package de.longor1996.jasic;

import java.util.AbstractList;

class ThrowList<E> extends AbstractList<E>
{
	public ThrowList(){}
	
	@Override
	public E get(int index) {
		return null;
	}
	
	@Override
	public int size() {
		return 0;
	}
	
	@Override
    public void add(int index, E element) {
        // Just don't do ANYTHING.
    }
	
}