public class Test {
    public static boolean whetheracted(double e){
        //判断是否激活，只需要输入一个double概率即可

        double pre=Math.random();

        if(pre<e) return true;
        else return false;

    }

    public static void main(String[]args){
        for(int i=0;i<10;i++) {
            System.out.println(whetheracted(0.1));
        }
    }
}
