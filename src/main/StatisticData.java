package main;

public class StatisticData {
    String date;
    int known_number;
    int repeat_number;
    int learn_number;

    public String getDate() {
        return date;
    }

    public int getKnown_number() {
        return known_number;
    }

    public int getRepeat_number() {
        return repeat_number;
    }

    public int getLearn_number() {
        return learn_number;
    }

    public StatisticData(String date, int known_number, int repeat_number, int learn_number) {
        this.date = date;
        this.known_number = known_number;
        this.repeat_number = repeat_number;
        this.learn_number = learn_number;
    }

    @Override
    public String toString() {
        return date +":" + known_number + ":" + repeat_number + ":" + learn_number + "\n";
    }
}
