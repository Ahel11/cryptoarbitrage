package scratch;

public class Result implements  Comparable{
    private double costToBuy = 0;
    private double tokenNr = 0;
    private double profit = 0;

    @Override
    public String toString() {
        return "Result{" +
                "costToBuy=" + costToBuy +
                ", tokenNr=" + tokenNr +
                ", profit=" + profit +
                '}';
    }

    public double getCostToBuy() {
        return costToBuy;
    }

    public void setCostToBuy(double costToBuy) {
        this.costToBuy = costToBuy;
    }

    public double getTokenNr() {
        return tokenNr;
    }

    public void setTokenNr(double tokenNr) {
        this.tokenNr = tokenNr;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    @Override
    public int compareTo(Object o) {
        Result curr = (Result)o;

        if(this.profit > curr.getProfit()) {
            return 1;
        } else if(this.profit < curr.getProfit()) {
            return -1;
        }

        return 0;
    }
}































