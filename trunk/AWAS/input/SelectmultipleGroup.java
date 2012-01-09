package input;
import java.util.ArrayList;


public class SelectmultipleGroup {
	
	private ArrayList<int[]> indices;
	private String name;
	
	public SelectmultipleGroup()
	{
		indices = new ArrayList<int[]>();
		name = "";
	}
	
	public SelectmultipleGroup(String name, ArrayList<int[]> combos)
	{
		this.indices = combos;
		this.name = name;
	}	

	public int size()
	{
		return indices.size();
	}
	
	public int[] getSelectmultipleCombinations(int index)
	{
		return indices.get(index);
	}
	
	public String getName()
	{
		return name;
	}
	
	public void printSMCList()
	{
		int count = 1;
		for(int[] index : indices)
		{
			System.out.print("\n"+count+": ");
			for(int i = 0; i < index.length; ++i)
			{
				System.out.print(" "+index[i]);	
			}
			++count;
		}
	}
}
