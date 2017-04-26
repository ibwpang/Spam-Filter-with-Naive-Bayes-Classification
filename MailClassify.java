import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import ICTCLAS.I3S.AC.ICTCLAS50;

public class MailClassify {

	static double[] hamvect;//
	static double[] spamvect;//
	static double pSpam;//
	static ArrayList<String> wordList;//
	static double valI;//
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//input valI
		Scanner sc = new Scanner(System.in);
		
		MailClassify mc = new MailClassify();
		try {
			//
			System.out.println("=====start to train=====");
			mc.Train();
			System.out.println("=====training finished=====");

			for(int i=0;i<wordList.size();i++)
				System.out.println(wordList.get(i)+"\n"+"ham: "+Math.exp(hamvect[i])+"\n"+"spam: "+Math.exp(spamvect[i]));
			
			
			System.out.println("=====start to test=====");
			while(true){
				System.out.println("please input the spam probability threshold i:(double e.g. 0.85 i<1 and i>=0) [exit input -1]");
				valI=sc.nextDouble();
				if(valI==-1)
					return;
				while(!(valI<1&&valI>=0)){
					System.out.println("i<1 and i>=0,please input again:");
					valI=sc.nextDouble();
					if(valI==-1)
						return;
				}
				
				mc.Test();
				System.out.println("=====testing finished=====");
			}
//			//
//			String ts=sc.nextLine();
//			String ts2=sc.nextLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	//
	private void Train() throws IOException{
		int hamNum=ReadFiles.GetFileNum("dataset\\training set\\ham");//
		int spamNum=ReadFiles.GetFileNum("dataset\\training set\\spam");//
		wordList=ReadFiles.GetWordsList("dataset\\training set");
		int wordNum=wordList.size();//
		
		int mailNum=hamNum+spamNum;//
		pSpam=spamNum/(mailNum+0.0);//
		
		//using array of int to store the frequency of each words
		MyArray hamArray=new MyArray(wordNum);//
		hamArray.InitArray(1);
		
		MyArray spamArray=new MyArray(wordNum);//
		spamArray.InitArray(1);
		
		//System.out.println(wordList);
		
		int hamWords=2;
		int spamWords=2;
		
		//
		ArrayList<MyArray> trainMatrix=ReadFiles.GetMatrix("dataset\\training set", wordList);
		//
		for(int i=0;i<hamNum;i++)
		{
			hamArray.Add(trainMatrix.get(i));//
			hamWords+=trainMatrix.get(i).NumOfOne();//
		}
		
		//
		for(int i=hamNum;i<mailNum;i++)
		{
			spamArray.Add(trainMatrix.get(i));//
			spamWords+=trainMatrix.get(i).NumOfOne();//
		}
		
		hamvect=new double[wordNum];//
		spamvect=new double[wordNum];//
		
		//
		//
		for(int i=0;i<wordNum;i++)
		{
			hamvect[i]=Math.log(hamArray.getArray()[i]/(hamWords+0.0));
			spamvect[i]=Math.log(spamArray.getArray()[i]/(spamWords+0.0));
		}
		
		
/*		for(int i=0;i<wordNum;i++)
		{
			System.out.print(hamvect[i]+"  ");
			if(i%20==0)
				System.out.println();
				
		}*/
	}
	
	//
	public void Test() throws IOException {
		int tHamNum=ReadFiles.GetFileNum("dataset\\test set\\ham");//
		int tSpamNum=ReadFiles.GetFileNum("dataset\\test set\\spam");//
		int tMailNum=tHamNum+tSpamNum;//
		int tRightNum=0;//
		int tRightSpamNum=0;//
		int tSumSpamNum=0;//
		int tFalsePositive=0;
		//
		ArrayList<MyArray> testMatrix=ReadFiles.GetMatrix("dataset\\test set", wordList);
		//System.out.println(testMatrix.size());
		//
		ArrayList<String> fileName=ReadFiles.GetFileName("dataset\\test set");
		//System.out.println(fileName.size());
		for(int i=0;i<tMailNum;i++)
		{
			double tPHam=0;//
			double tPSpam=0;//
			
			//
			for(int j=0;j<wordList.size();j++)
			{
				tPHam+=testMatrix.get(i).getArray()[j]*hamvect[j];
				tPSpam+=testMatrix.get(i).getArray()[j]*spamvect[j];
			}
			//System.out.println(tPHam+"--"+tPSpam);
			//
			tPHam+=Math.log(1-pSpam);
			tPSpam+=Math.log(pSpam);
			
			
			double theta=valI/(1-valI);
			
			
			if((tPSpam-tPHam)>Math.log(theta))
			{
				System.out.println("spam email: "+fileName.get(i));
				tSumSpamNum++;
				if(i>=tHamNum)
				{
					tRightNum++;
					tRightSpamNum++;
				}
				else{
					tFalsePositive++;
				}
			}
			else
			{
				System.out.println("ham email: "+fileName.get(i));
				if(i<tHamNum)
					tRightNum++;
			}
			System.out.println("Exp(tPHam): "+Math.exp(tPHam)+" Exp(tPSpam):"+Math.exp(tPSpam)+" tPHam: "+tPHam+" tPSpam: "+tPSpam);
			System.out.println("");
			
		}
		
		//System.out.println("spam accuracy = "+tRightSpamNum/(tSumSpamNum+0.0));
		//System.out.println("call back rate = "+tRightSpamNum/(tSpamNum+0.0));
		System.out.println("overall accuracy = "+tRightNum/(tMailNum+0.0));
		System.out.println("false positive = "+tFalsePositive/(tHamNum+0.0));
	}

}
