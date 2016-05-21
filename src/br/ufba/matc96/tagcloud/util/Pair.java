package br.ufba.matc96.tagcloud.util;

public class Pair<F,S>
{
	public final F first;
	public final S second;
	
	public Pair(F first, S second)
	{
		this.first = first;
		this.second = second;
	}
	
	public F getFirst()
	{
		return this.first;
	}
	
	public S getSecond()
	{
		return this.second;
	}

	@Override
    public boolean equals(Object o) {
		if (o == null || !(o instanceof Pair))
		{
		    return false;
		}
		Pair<F,S> p = (Pair<F, S>) o;
		return (this.first == p.first || (this.first != null && this.first.equals(p.first))) &&
		    (this.second == p.second || (this.second != null && this.second.equals(p.second)));
    }

	@Override
    public int hashCode()
	{
		return ((this.first == null) ? 0 : first.hashCode()) ^
				((this.second == null) ? 0 : second.hashCode());
    }

	@Override
    public String toString() {
		return "{" + this.first + ", " + this.second + "}";
    }
}