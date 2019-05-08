// Author: Przemysław Sałęga

package backend.key;

public class CharSwap {

    private char from;
    private char to;

    public CharSwap(char from, char to){
        this.from = from;
        this.to = to;
    }

    public char getFrom(){
        return from;
    }

    public char getTo(){
        return to;
    }

    @Override
    public String toString() {
        return "Zamiana z " + from + " na " + to;
    }
}
