package rommie.modules.RandomNumber;

import java.util.Random;

public class RandomNumber {

    public static int generateRandom(int max) {
        Random randomGenerator = new Random();
        //noinspection LoopStatementThatDoesntLoop
        for (int idx = 1; true; ++idx) {
            return randomGenerator.nextInt(max);
        }
        return 3;
    }
}
