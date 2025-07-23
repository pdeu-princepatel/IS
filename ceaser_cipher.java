import java.util.HashMap;
import java.util.Scanner;

class Control{
public static void main(String[] args) {
    String text;
    System.out.println("Enter the text to Encrypt:");
    Scanner sc = new Scanner(System.in);
    text =sc.nextLine();
    System.out.println("Enter what type of Ceaser Cypher you want to implement:");
    System.out.println("1. Normal");
    System.out.println("2. Revised");
    int ch =sc.nextInt();
    switch (ch) {
        case 1:
            Normal n1 = new Normal();
            n1.encrypt(text);

            break;
        case 2:

            break;
        default:

            break;
    }
}
}
final class values{    
    HashMap<Integer,Character> n = new HashMap<>(); 
    void insert(){
    char c = 'A';
    for(int i =1;i<27;i++){
        n.put(i, c);
        c++;
    }
    c='z';
    for(int i =1;i<27;i++){
        n.put(i+26, c);
        c--;
    }
    }
    char out(){
        char ch;
        return ch;
    }
}
class Normal{
        String encrypt(String text){
            int count =0;
            String fial = "";
            while(count<text.length()){
                char ch =text.charAt(count);
                fial = fial+(char)((int)ch+2);
                count++;
            }
            return fial;          
        }
        String decrypt(String text){
             int count =0;
            String fial = "";
            while(count<text.length()){
                char ch =text.charAt(count);
                fial = fial+(char)((int)ch-2);
                count++;
            }
            return fial;
        }
}
class Revise{

}