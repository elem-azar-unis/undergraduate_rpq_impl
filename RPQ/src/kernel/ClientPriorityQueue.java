package kernel;

import java.util.Arrays;

/**
 * The kernel priority queue for client. It uses ElementTable to find an element in constant time.
 * 
 * <p>This is an unbounded priority queue based on heap. This is a maximam priority queue.
 * The priority is provided by user difined Comparable.
 * 
 * <p>The basic operations: Insert, Alter, Delete max, Get max
 * 
 * <p>Also can get or remove one element given the identifier.
 * @param <K> The type of identifier.
 * @param <V> The type of value of priority.
 * @param <T> The type of element in the priority queue.
 */
public class ClientPriorityQueue<K,V extends Comparable<V>,T extends Element<K,V>>
{
	private static final int INITIAL_CAPACITY=8;
	/**
     * Priority queue represented as a balanced binary heap: the two
     * children of queue[n] are queue[2*n+1] and queue[2*n+2].  The
     * priority queue is ordered by elements' decreasing ordering. 
     * For each node n in the heap and each descendant d of n, n >= d.  
     * The element with the highest value is in queue[0], assuming the queue is nonempty.
     */	
	@SuppressWarnings("unchecked")
	private T[] elements=(T[]) new Object[INITIAL_CAPACITY];
	/**
     * The size of the Priority Queue (the number of elements it contains).
     *
     * @serial
     */
    private int size=0;
    /**
     * The hash table of the element.
     */
    private ElementTable<K,T> table=new ElementTable<K,T>();
    /**
     * Increases the capacity by doubling it.
     */
    private void grow()
    {
    	elements=Arrays.copyOf(elements,2*elements.length);
    }
    /**
     * Fix the heap from position k (let's call it x). Maintaining heap invariant by
     * promoting x up the tree until it is less than or equal to its parent, or is the root.
     * @param k The position to be fixed.
     */
    private void shiftUP(int k)
    {
    	T e=elements[k];
    	while(k>0)
    	{
    		int parent=(k-1)>>>1;
    		if(e.compareTo(elements[parent])<=0)
    			break;
    		elements[k]=elements[parent];
    		elements[k].index=k;
    		k=parent;
    	}
    	elements[k]=e;
    	e.index=k;
    }
    /**
     * Fix the heap from position k (let's call it x). Maintaining heap invariant by
     * demoting x down the tree until it is greater than or equal to its children, or is a leaf.
     * @param k The position to be fixed.
     */
    private void shiftDown(int k)
    {
    	T e=elements[k];
    	int half=size>>>1;            // loop while a non-leaf
    	while(k<half)
    	{
    		int child=(k<<1)+1;       // assume left child is largest
    		int right=child+1;
    		if(right<size && elements[child].compareTo(elements[right])<0)
    			child=right;
    		if(e.compareTo(elements[child])>=0)
    			break;
    		elements[k]=elements[child];
    		elements[k].index=k;
    		k=child;
    	}
    	elements[k]=e;
    	e.index=k;
    }
    /**
     * Insert a new element in the priority queue.
     * @param e The element to be inserted.
     */
    public void insert(T e)
    {
    	table.add(e);
    	if(size==elements.length)
    		grow();
    	elements[size]=e;
    	e.index=size;
    	size++;
    	shiftUP(e.index);
    }
    /**
     * Change the proiroty of the element having the identifier "key".
     * @param key the identifier.
     * @param value the disierd value.
     */
    public void alter(K key,V value)
    {
    	int index=table.get(key).index;
    	if(elements[index].priority.compareTo(value)>0)
    	{
    		elements[index].priority=value;
    		shiftDown(index);
    	}
    	else
    	{
    		elements[index].priority=value;
    		shiftUP(index);
    	}
    }
    /**
     * Get the element having the max priority.
     * @return The element having the max priority
     */
    public T getMax()
    {
    	return size==0? null : elements[0];
    }
    /**
     * Delete the element having the max priority.
     * @return The element having the max priority
     */
    public T deleteMax()
    {
    	if(size==0)return null;
    	T rtn=elements[0];
    	size--;
    	elements[0]=elements[size];
    	shiftDown(0);
    	table.remove(rtn);
    	return rtn;
    }
    /**
     * Get the element having the identifier "key"
     * @param key The identifier
     * @return The element whose identifier is "key"
     */
    public T get(K key)
    {
    	return table.get(key);
    }
    /**
     * remove the element having the identifier "key"
     * @param key The identifier
     * @return The element whose identifier is "key"
     */
    public T remove(K key)
    {
    	T rtn=table.get(key);
    	int index=rtn.index;
    	table.remove(rtn);
    	size--;
    	elements[index]=elements[size];
    	if(rtn.compareTo(elements[index])>0)
    		shiftDown(index);
    	else
			shiftUP(index);
    	return rtn;
    }
}
