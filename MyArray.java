import javax.swing.table.TableColumn;


//
public class MyArray {

	private int[] array;
	
	
	public int[] getArray() {
		return array;
	}

	//
	public void SetPos(int n)
	{
		array[n]=1;
	}

	public MyArray(int n)
	{
		array=new int[n];
	}
	
	//
	public void InitArray(int v)
	{
		for(int i=0;i<array.length;i++)
			array[i]=v;
	}
	
	//
	public void Add(MyArray arr2)
	{
		for(int i=0;i<array.length;i++)
			array[i]+=arr2.getArray()[i];
	}
	
	//
	public int NumOfOne()
	{
		int n=0;
		for(int i=0;i<array.length;i++)
		{
			if(array[i]==1)
				n++;
		}
		return n;
	}
	
	

}
