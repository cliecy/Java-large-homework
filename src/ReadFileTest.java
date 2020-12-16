import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.*;
import java.math.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadFileTest{

	
	Vector vec = new Vector();//读取文件vec，将每一行存入Vector中
	Vector vecuser=new Vector();//读取文件vecuser，将每一行存入Vector中


	
   public void load(String path) {

	   LineNumberReader reader = null;
	   FileReader in = null;
	   String key = null;
	   int value = 0;
	   String[] pairs = new String[4];


	   try {
		   in = new FileReader(path);
		   reader = new LineNumberReader(in);
		   while (true) {
			   String str = reader.readLine();
			   if (str == null)
				   break;
			   //System.out.println(str);
			   pairs = str.split(" ");
			   String strLine = pairs[0] + " " + pairs[1] + " " + pairs[2] + " " + pairs[3];
			   vec.add(strLine);

		   }

	   } catch (FileNotFoundException e) {
		   e.printStackTrace();
	   } catch (IOException e) {
		   e.printStackTrace();
	   } finally {
		   try {
			   if (reader != null)
				   reader.close();
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
		   try {
			   if (in != null)
				   in.close();
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
	   }
   }




	   public void loadforuser(String path){

		   LineNumberReader reader = null;
		   FileReader in = null;
		   String key = null;
		   int value = 0;
		   String [] pairs = new String[4];


		   try {
			   in = new FileReader(path);
			   reader = new LineNumberReader(in);
			   while (true) {
				   String str = reader.readLine();
				   if (str == null)
					   break;
				   //System.out.println(str);
				   vecuser.add(str);
			   }

		   } catch (FileNotFoundException e) {
			   e.printStackTrace();
		   } catch (IOException e) {
			   e.printStackTrace();
		   } finally {
			   try {
				   if (reader != null)
					   reader.close();
			   } catch (IOException e) {
				   e.printStackTrace();
			   }
			   try {
				   if (in != null)
					   in.close();
			   } catch (IOException e) {
				   e.printStackTrace();
			   }
		   }


	   }
   
   
   public static void main(String args[]){
	   
	   
	   ReadFileTest test = new ReadFileTest();
	   test.load("C:\\Users\\guanj\\Desktop\\The large homework\\links.txt");

	   System.out.println("link number:"+test.vec.size());

	   test.loadforuser("C:\\Users\\guanj\\Desktop\\The large homework\\alluserlist.txt");

	   System.out.println("user number:"+test.vecuser.size());

	   //上面是两个读取方法，是将所有点的编号和所有弧的信息分别存到了test类中的vecuser和vec中，一行作为一个个体。



	   HashMap<Integer,Node>mapTest=new HashMap<>();//生成哈希图

	   for(int i=0;i<test.vecuser.size();i++) {//将结点全部输入到hashmap之中。
		   mapTest.put(Integer.parseInt( (String)test.vecuser.get(i)),new Node(Integer.parseInt((String)test.vecuser.get(i))));
	   }

	   String[]pairs;
	   	for(int j=0;j<test.vec.size();j++){//为每一个弧，寻找位置，将其赋到起始节点中。

			pairs=((String)test.vec.get(j)).split(" ");
			mapTest.get(Integer.parseInt(pairs[0])).vece.add((String)test.vec.get(j));
		}

	   	//上述操作耗时大概不到1秒，下面是操作耗时极大的method


	   	Vector<Integer>pre;
	   	pre=Method.MainMethod(mapTest,10);//此处的n标识了问题规模，也就是我最终想要获得的点集的大小。

	   for(int i=0;i<10;i++) {
		   System.out.println(pre.get(i));
	   }
   }

}



class Node{//结点类

	int number;//点的key
	int status;//激活状态，1代表正激活，-1代表负激活，0代表未激活。
	Vector<String> vece=new Vector<String>();
	Vector<Integer> b=new Vector<Integer>();
	Vector<Vector<Integer>> vec=new Vector<Vector<Integer>>();
	int whetheractedother;// 1代表已经激活过其他结点了，0代表还未激活，这个是为了方便确定，当前这个结点，是不是新被激活的结点，所有的新被
	//激活的节点，都只会尝试对它周围的结点激活一次，在下一次激活的时候，就标记位1，不再让它继续激活邻居
	double plus;//加入这个点所能造成的影响力提升，就是相比于不增加这个点进现有集合，加入它所能造成的影响力提升，用于辅助贪心算法

	public Node(int a){
		number=a;
		status=0;
		whetheractedother=0;
		plus =0;
	}
	public int getnumofline(){
		return vec.size();
	}//获取该点的所有以它为起点的弧的数量

	public Vector<Vector<Integer>> get(int n)
	{
		String[]pairs;
		pairs=((String)vece.get(n)).split(" ");
		for(int i=0;i<4;i++)
		b.add(Integer.parseInt(pairs[i]));
		vec.add(b);
		return vec;
	}
	public Integer getend(int n){//获得指定行的目标点的key

		return vec.get(n).get(1);
	}

	public Integer getSta(int n){//获取激活的正负性

		return vec.get(n).get(2);
	}

	public double getProba(int n){//获取激活概率

		return vec.get(n).get(3);
	}


	public Node(){
		number=0;
		status=0;
		whetheractedother=0;
		plus =0;
	}
}

class Method {//集成核心算法，获取目标集合


	public static Vector<Integer> MainMethod(HashMap<Integer,Node> a,int n){//主调用函数

		double pre=0;
		Vector<Integer> NodeWeChoose=new Vector<>();//这个vector代表了我们最终选出的拥有最大影响力的点集合
		while(NodeWeChoose.size()<=n) {//不断增加这个集合的大小直到达到我们输入的n规模
			pre=AddBestNodeInVec(a,NodeWeChoose,pre);
		}
		return NodeWeChoose;//返回vector

	}




	public static double AddBestNodeInVec(HashMap<Integer,Node> a,Vector<Integer> ChoosedNode,double precount){
		double[]count ={0};//////修改的部分
		double[]j={0};
		/*Iterator<Map.Entry<Integer,Node>> iterator = a.entrySet().iterator();*/
		a.entrySet().parallelStream().forEach((entry)->{
			Node pre=entry.getValue();
			ChoosedNode.add(pre.number);
			for(;j[0]<10000000;j[0]++){
				count[0]+=NumOfOneVecGet(a,ChoosedNode);
			}
			if(count[0]>=j[0])
			count[0]=count[0]/j[0];//求平均值
			pre.plus=count[0];
			ChoosedNode.removeElement(pre.number);
		});

		/*for(int i=0;i<a.size();i++) {

			Node pre=iterator.next().getValue();//利用迭代器遍历hashmap,对于每一个结点都要进行这个操作
			ChoosedNode.add(pre.number);//将目前的结点加入

			for(int j=0;j<1;j++){//重复实验若干次,老师要求5000次到10000次
				count+=NumOfOneVecGet(a,ChoosedNode);//对于一个已激活点集的算法
			}

			count=count/1;//求平均值
			pre.plus=count-precount;
			ChoosedNode.removeElement(pre.number);
		}*/

		/*Iterator<Map.Entry<Integer,Node>> iterator2 = a.entrySet().iterator();*/
		 double theplusoutput[]= {Double.NEGATIVE_INFINITY};
		 int theoutputnumber[] = {0};
		a.entrySet().parallelStream().forEach((entry)-> {

			Node pre=entry.getValue();

			if(ChoosedNode.contains(pre.number))return;

			if(pre.plus>theplusoutput[0]) {
				theplusoutput[0] = pre.plus;
				theoutputnumber[0]=pre.number;
			}
		});


		/*for(int i=0;i<a.size();i++) {

			Node pre=iterator2.next().getValue();
			if(pre.plus>theplusoutput) {
				theplusoutput = pre.plus;
				theoutputnumber=pre.number;
			}

		}*/

		ChoosedNode.add( theoutputnumber[0]);
		return theplusoutput[0];
		}





	public static int NumOfOneVecGet(HashMap<Integer,Node> a,Vector<Integer> ChoosedNode){//这个算法半秒才算一次！，实际上大概

		if(!ChoosedNode.isEmpty()) {
			for (int i = 0; i < ChoosedNode.size(); i++) {
				a.get((Integer) ChoosedNode.get(i)).status = 1;//为每一个已经选择过的点，赋上正激活态
			}
		}

		/*Iterator<Map.Entry<Integer,Node>> iterator = a.entrySet().iterator();//迭代器，用于遍历collection类及其子类*/
		a.entrySet().parallelStream().forEach((entry)-> {/////修改的部分
			Node pre=entry.getValue();
			switch (pre.status){
				case 1:
					if(pre.whetheractedother==0) {

						for(int i=0;i<pre.getnumofline();i++) {

							if(whetheracted(pre.getProba(i))) {
								a.get(pre.getend(i)).status=1*pre.getSta(i);
								break;
							}
						}
						pre.whetheractedother=1;
						break;
					}
					else break;
				case -1:
					if(pre.whetheractedother==0) {

						for(int i=0;i<pre.getnumofline();i++) {

							if(whetheracted(pre.getProba(i))) {
								a.get(pre.getend(i)).status=-1*pre.getSta(i);
								break;
							}

						}

						pre.whetheractedother=1;

						break;
					}
					else break;

				case 0:break;


			}

		});

		/*while (!whetherfinished(a)) {
			Node pre=iterator.next().getValue();
			if(!iterator.hasNext()) iterator= a.entrySet().iterator();
			switch (pre.status){
				case 1:
					if(pre.whetheractedother==0) {

						for(int i=0;i<pre.getnumofline();i++) {

							if(whetheracted(pre.getProba(i))) {
								a.get(pre.getend(i)).status=1;
							}
						}
						pre.whetheractedother=1;
						break;
					}
					else break;
				case -1:
					if(pre.whetheractedother==0) {

						for(int i=0;i<pre.getnumofline();i++) {

							if(whetheracted(pre.getProba(i))) {
								a.get(pre.getend(i)).status=-1;
							}

						}

						pre.whetheractedother=1;

						break;
					}
					else break;

				case 0:break;


			}
		}*/

		int output=getNumofPosPoint(a)-getNumofNegPoint(a);//最终结果是将全图的正激活点减去负激活点

		InitZeroForMap(a);//初始化整个图，使得其status和plus都变为0

		return output;//将正影响力点数减负影响力点，获得最终的影响力
	}








	public static void InitZeroForMap(HashMap<Integer,Node> a){
		Iterator<Map.Entry<Integer,Node>> iterator = a.entrySet().iterator();
		while(iterator.hasNext()) {
			Node pre=(iterator.next().getValue());
			pre.status=0;
			pre.plus=0;
			pre.whetheractedother=0;

		}
		/*a.entrySet().parallelStream().forEach((entry)-> {
			Node pre = entry.getValue();
			pre.status = 0;
			pre.plus = 0;
			pre.whetheractedother = 0;
		});*/
	}

	public static boolean whetherfinished(HashMap<Integer,Node> a) {
		Iterator<Map.Entry<Integer,Node>> iterator = a.entrySet().iterator();


		while(iterator.hasNext()) {
			Node pre=iterator.next().getValue();

			if(pre.status==1&&pre.whetheractedother==0) return false;

			if(pre.status==-1&&pre.whetheractedother==0) return false;
		}
		return true;

	}


	public static boolean whetheracted(double e){
		//判断是否激活，只需要输入一个double概率即可

		double pre=Math.random();

		if(pre<e) return true;
		else return false;

	}


	public static int getNumofPosPoint(HashMap<Integer,Node> a) {
		Iterator<Map.Entry<Integer,Node>> iterator = a.entrySet().iterator();
		int count=0;
		while(iterator.hasNext()) {
			if((iterator.next()).getValue().status==1) count++;
		}
		return count;
		/*final int []count={0};
		a.entrySet().parallelStream().forEach((entry)-> {
			if(entry.getValue().status==1)
				count[0]++;
		});

		return count[0];*/


   }

	public static int getNumofNegPoint(HashMap<Integer,Node> a) {
		Iterator<Map.Entry<Integer,Node>> iterator = a.entrySet().iterator();
		int count=0;
		while(iterator.hasNext()) {
			if(iterator.next().getValue().status==-1) count++;
		}

		return count;
		/*final int []count={0};
		a.entrySet().parallelStream().forEach((entry)-> {
			if(entry.getValue().status==-1)
				count[0]++;
		});

		return count[0];*/

	}





}

 /* for(int i=0;i<test.vec.size();i++){

		   System.out.println(test.vec.get(i).toString());
		   count++;

	   }*/
